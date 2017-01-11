package paul.example.particle;

import processing.core.*;


@SuppressWarnings("serial")
public class Runparticle extends PApplet {
	PApplet parent;
	
	Runparticle(PApplet p){
		parent = p;
	}

	Point3D[] pt;
	
	public Runparticle()	{
	
	}
	
	
	public void setup()
	{
		
	  size(1000, 1000, P3D);
	  //smooth();
	  frameRate(30);
	  //String path = Runparticle.class.getResource("/mml/run/testspere.csv").getPath();
	  String dir = System.getProperty("user.dir");
	  dir += "\\mml\\run\\testspere.csv";
	  String path = dir; //"H:\\Workspace\\Java Project\\runparticle\\bin\\mml\\run\\testspere.csv";
	  loadPoints(path);  
	  //loadPoints("testshpere_2.csv");    
	}

	float a;
	public void draw()
	{
		background(0);  
		translate(width/2, height/4);
	//  rotateY(a);
	//  a += 0.05;

	   for ( int i=0; i < pt.length; i++ )
	  {
//	    float x, y, z;
	    
	    pt[i].display_point(pt[i].getX(), pt[i].getY(), pt[i].getZ());
	// pt[i].moving_x();
	// pt[i].moving_y(); 
	    pt[i].display_go(moving_x(), moving_y());
//	    pt[i].display_back();
	    if ( i == 0 ) //pt.length-1 ) 
	    	pt[i].test();
	  }
	}

	    float moving_x()  {
	      float noiseScale = random((float)-1, (float)1);
	      float noiseVal = noise(noiseScale, noiseScale, 0);
	      float angle = noiseVal*2*(float)Math.PI;
	      float ran_x = random(-1, 3);
	      float move_x = cos(angle) - ran_x;
	    
	      return move_x; 
	    }
	    float moving_y()  {
	      float noiseScale = random(-2, 4);
	      float noiseVal = noise(noiseScale, noiseScale, 0);
	      float angle = noiseVal*2*(float)Math.PI;
	      float ran_y = random(-3, 3);        
	      float move_y = sin(angle) - ran_y/2;      
	      
	      return move_y; 
	    }


	public void mouseClicked()  {
	  int ran = (int)random(0, pt.length);
	  float sel_x = pt[ran].getX();
	  float sel_y = pt[ran].getY();
	  float sel_z = pt[ran].getZ();


	//  int ran_i = (int)random(100, 200);
	  for (int i = 0; i < pt.length; i++)  {

	    float dis = PApplet.sqrt(PApplet.pow((sel_x - pt[i].getX()), 2) + 
	    PApplet.pow((sel_y - pt[i].getY()), 2) + 
	    PApplet.pow((sel_z - pt[i].getZ()), 2));
	    if (dis < 200)
	    {  
	      
	      pt[i].setIn(true);
	      pt[i].setTime(random(5, 8));
	      pt[i].setDistance(random(500, 800));
	    }
	  }
	}

	Point3D[] loadPoints(String path) {
	  String[] raw = loadStrings(path);
	  pt = new Point3D[raw.length];
	  for (int i=0; i < raw.length; i++ ) {
	    String[] thisLine = split(raw[i], ",");
	    float x = new Float(thisLine[0]).floatValue();
	    float y = new Float(thisLine[1]).floatValue();
	    float z = new Float(thisLine[2]).floatValue();  

	    pt[i] = new Point3D(this);
	    pt[i].setX(x);
	    pt[i].setY(y);
	    pt[i].setZ(z);
	  }
	  println("Loaded: "+raw.length+" points");
	  return pt;
	}


}
