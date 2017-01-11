package paul.example.flocking;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import remixlab.proscene.InteractiveAvatarFrame;
import remixlab.proscene.Quaternion;
import remixlab.proscene.Scene;

public class Boid {
	
	private FlockMain flock;
	//private Scene scene;
	InteractiveAvatarFrame frame;
	Quaternion q;
	int grabsMouseColor;//color
	int avatarColor;

	// fields
	PVector pos, vel, acc, ali, coh, sep; // pos, velocity, and acceleration in
	// a vector datatype
	float neighborhoodRadius; // radius in which it looks for fellow boids
	float maxSpeed = 4; // maximum magnitude for the velocity vector
	float maxSteerForce = .1f; // maximum magnitude of the steering vector
	float sc = 3; // scale factor for the render of the boid
	float flap = 0;
	float t = 0;

	// constructors
	public Boid(FlockMain flock, PVector inPos) {
		this.flock = flock;
		//scene = s; 
		
		grabsMouseColor = flock.color(0,0,255);
		avatarColor = flock.color(255,0,0);		
		pos = new PVector();
		pos.set(inPos);
		frame = new InteractiveAvatarFrame(flock.scene);	
		frame.setPosition(pos);
		frame.setAzimuth(-PApplet.HALF_PI);
		frame.setInclination(PApplet.PI*(4/5));
		frame.setTrackingDistance(flock.scene.radius()/10);
		vel = new PVector(flock.random(-1, 1), flock.random(-1, 1), flock.random(1, -1));
		acc = new PVector(0, 0, 0);
		neighborhoodRadius = 100;
	}

	public Boid(FlockMain flock, Scene s, PVector inPos, PVector inVel, float r) {
		this.flock = flock;
		//scene = s;
		
		grabsMouseColor = flock.color(0,0,255);
		avatarColor = flock.color(255,0,0);
		pos = new PVector();
		pos.set(inPos);
		frame = new InteractiveAvatarFrame(flock.scene);
		frame.setPosition(pos);
		frame.setAzimuth(-PApplet.HALF_PI);
		frame.setTrackingDistance(flock.scene.radius()/10);
		vel = new PVector();
		vel.set(inVel);
		acc = new PVector(0, 0);
		neighborhoodRadius = r;
	}

	public void run(ArrayList<Boid> bl, boolean avoidWalls, int flockWidth, int flockHeight, int flockDepth) {
		t += .1;
		flap = 10 * PApplet.sin(t);
		// acc.add(steer(new PVector(mouseX,mouseY,300),true));
		// acc.add(new PVector(0,.05,0));
		if (avoidWalls) {
			acc.add(PVector.mult(avoid(new PVector(pos.x, flockHeight, pos.z), true), 5));
			acc.add(PVector.mult(avoid(new PVector(pos.x, 0, pos.z), true), 5));
			acc.add(PVector.mult(avoid(new PVector(flockWidth, pos.y, pos.z),	true), 5));
			acc.add(PVector.mult(avoid(new PVector(0, pos.y, pos.z), true), 5));
			acc.add(PVector.mult(avoid(new PVector(pos.x, pos.y, 0), true), 5));
			acc.add(PVector.mult(avoid(new PVector(pos.x, pos.y, flockDepth), true), 5));
		}
		flock(bl);
		move();
		checkBounds(flockWidth, flockHeight, flockDepth);
		//render();
	}

	// ///-----------behaviors---------------
	void flock(ArrayList<Boid> bl) {
		ali = alignment(bl);
		coh = cohesion(bl);
		sep = seperation(bl);
		acc.add(PVector.mult(ali, 1));
		acc.add(PVector.mult(coh, 3));
		acc.add(PVector.mult(sep, 1));
	}

	void scatter() {

	}

	// //------------------------------------

	void move() {
		vel.add(acc); // add acceleration to velocity
		vel.limit(maxSpeed); // make sure the velocity vector magnitude does not
		// exceed maxSpeed
		pos.add(vel); // add velocity to position
		frame.setPosition(pos);
		acc.mult(0); // reset acceleration
	}

	private void checkBounds(int flockWidth, int flockHeight, int flockDepth) {
		if (pos.x > flockWidth)
			pos.x = 0;
		if (pos.x < 0)
			pos.x = flockWidth;
		if (pos.y > flockHeight)
			pos.y = 0;
		if (pos.y < 0)
			pos.y = flockHeight;
		if (pos.z > flockDepth)
			pos.z = 0;
		if (pos.z < 0)
			pos.z = flockDepth;
	}

	// check if this boid's frame is the avatar
	boolean isAvatar() {
		if ( flock.scene.avatar() == null )
			return false;
		if ( flock.scene.avatar().equals(frame) )
			return true;
		return false;
	}

	public void render(float hue) {
		flock.pushStyle();
		flock.stroke(hue);
		flock.noFill();
		flock.noStroke();
		flock.fill(hue);		

		q = Quaternion.multiply(new Quaternion( new PVector(0,1,0),  PApplet.atan2(-vel.z, vel.x)), 
				new Quaternion( new PVector(0,0,1),  PApplet.asin(vel.y / vel.mag())) );		
		frame.setRotation(q);

		flock.pushMatrix();
		// Multiply matrix to get in the frame coordinate system.
		frame.applyTransformation();

		// highlight boids under the mouse
		if (frame.grabsMouse()) {
			flock.fill( grabsMouseColor);
			// additionally, set the boid's frame as the avatar if the mouse is pressed
			if (flock.mousePressed == true) 
				flock.scene.setAvatar(frame);			
		}

		// highlight the boid if its frame is the avatar
		if ( isAvatar() ) {
			flock.fill( avatarColor );
		}

		//draw boid
		flock.beginShape(PApplet.TRIANGLES);
		flock.vertex(3 * sc, 0, 0);
		flock.vertex(-3 * sc, 2 * sc, 0);
		flock.vertex(-3 * sc, -2 * sc, 0);

		flock.vertex(3 * sc, 0, 0);
		flock.vertex(-3 * sc, 2 * sc, 0);
		flock.vertex(-3 * sc, 0, 2 * sc);

		flock.vertex(3 * sc, 0, 0);
		flock.vertex(-3 * sc, 0, 2 * sc);
		flock.vertex(-3 * sc, -2 * sc, 0);

		flock.vertex(-3 * sc, 0, 2 * sc);
		flock.vertex(-3 * sc, 2 * sc, 0);
		flock.vertex(-3 * sc, -2 * sc, 0);
		flock.endShape();		

		flock.popMatrix();

		flock.popStyle();		
	}

	// steering. If arrival==true, the boid slows to meet the target. Credit to
	// Craig Reynolds
	PVector steer(PVector target, boolean arrival) {
		PVector steer = new PVector(); // creates vector for steering
		if (!arrival) {
			steer.set(PVector.sub(target, pos)); // steering vector points
			// towards target (switch
			// target and pos for
			// avoiding)
			steer.limit(maxSteerForce); // limits the steering force to
			// maxSteerForce
		} 
		else {
			PVector targetOffset = PVector.sub(target, pos);
			float distance = targetOffset.mag();
			float rampedSpeed = maxSpeed * (distance / 100);
			float clippedSpeed = PApplet.min(rampedSpeed, maxSpeed);
			PVector desiredVelocity = PVector.mult(targetOffset,
					(clippedSpeed / distance));
			steer.set(PVector.sub(desiredVelocity, vel));
		}
		return steer;
	}

	// avoid. If weight == true avoidance vector is larger the closer the boid
	// is to the target
	PVector avoid(PVector target, boolean weight) {
		PVector steer = new PVector(); // creates vector for steering
		steer.set(PVector.sub(pos, target)); // steering vector points away from
		// target
		if (weight)
			steer.mult(1 / PApplet.sq(PVector.dist(pos, target)));
		// steer.limit(maxSteerForce); //limits the steering force to
		// maxSteerForce
		return steer;
	}

	PVector seperation(ArrayList<Boid> boids) {
		PVector posSum = new PVector(0, 0, 0);
		PVector repulse;
		for (int i = 0; i < boids.size(); i++) {
			Boid b = (Boid) boids.get(i);
			float d = PVector.dist(pos, b.pos);
			if (d > 0 && d <= neighborhoodRadius) {
				repulse = PVector.sub(pos, b.pos);
				repulse.normalize();
				repulse.div(d);
				posSum.add(repulse);
			}
		}
		return posSum;
	}

	PVector alignment(ArrayList<Boid> boids) {
		PVector velSum = new PVector(0, 0, 0);
		int count = 0;
		for (int i = 0; i < boids.size(); i++) {
			Boid b = (Boid) boids.get(i);
			float d = PVector.dist(pos, b.pos);
			if (d > 0 && d <= neighborhoodRadius) {
				velSum.add(b.vel);
				count++;
			}
		}
		if (count > 0) {
			velSum.div((float) count);
			velSum.limit(maxSteerForce);
		}
		return velSum;
	}

	PVector cohesion(ArrayList<Boid> boids) {
		PVector posSum = new PVector(0, 0, 0);
		PVector steer = new PVector(0, 0, 0);
		int count = 0;
		for (int i = 0; i < boids.size(); i++) {
			Boid b = (Boid) boids.get(i);
			float d = PApplet.dist(pos.x, pos.y, b.pos.x, b.pos.y);
			if (d > 0 && d <= neighborhoodRadius) {
				posSum.add(b.pos);
				count++;
			}
		}
		if (count > 0) {
			posSum.div((float) count);
		}
		steer = PVector.sub(posSum, pos);
		steer.limit(maxSteerForce);
		return steer;
	}
}
