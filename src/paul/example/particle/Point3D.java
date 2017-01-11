package paul.example.particle;

import processing.core.PApplet;

public class Point3D {

    private float x;
    private float y;
    private float z;
    
    private float mTime;
    private float mDistance;
    private float mCurTime;
    //    private float noiseScale = 0.005;
    private float noiseZ = 0;
    private float ori_x;
    private float ori_y;    
    private float ori_z;    
    private float move_x;
    private float move_y;
    private float ran_x;    
    private float ran_y;    
    
    PApplet parent;
	
	Point3D (PApplet p){
		this();
		parent = p;
	}
    
    public Point3D() {
        x = 0;
        y = 0;
        z = 0;
    }
  
    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void setX(float x) {
        this.x = x;
        ori_x = x;
    }
    public void setY(float y) {
        this.y = y;
        ori_y = y;
    }
    public void setZ(float z) {
        this.z = z;
        ori_z = z;
    }
    
    public float getX() {
        return x;      
    }
    public float getY() {
        return y;      
    }
    public float getZ() {
        return z;      
    }  
    
    public void setIn(boolean is) {
        mCurTime = 0;
    }  
    
    public void setTime(float time) {
        mTime = time * 20;

//        println(ran_x + ran_y);
    }
  
    public void setDistance(float d) {
        mDistance = d;
    }  
    
    public void display_go(float mx, float my) {
        if ( true ) {
            if ( mTime == 0 || mDistance == 0 )  
                return;
//            x = x - (mDistance / mTime) - noiseFloat_x;
//            moving_x();
//            moving_y();            
            x = x + moving_x();
            y = y + moving_y();
            //x = x + PApplet.cos(parent.noise(x*parent.random((float)-0.0005, (float)0.0005), 
            //		y*parent.random((float)-0.0005, (float)0.0005), 0)*2*PConstants.PI) - parent.random((float)-1, (float)3);
            //y = y + PApplet.sin(parent.noise(x*parent.random((float)-0.0005, (float)0.0005), 
            //		y*parent.random((float)-0.0005, (float)0.0005), 0)*2*PConstants.PI) - parent.random((float)-1, (float)3);
          
            mCurTime++;
            if ( mCurTime >= mTime ) {
                mTime = 0;
                mDistance = 0;
                x = ori_x;
                y = ori_y;
                z = ori_z;
                mCurTime = 0;
                move_x = 0;
                move_y = 0;
          //      backTime = true;
         //       println("backTime = true");
            }
        }
    }
    
    public float moving_x()  {
      float noiseScale = parent.random((float)-0.005, (float)0.005); //parent.random(-1, 1);
      float noiseVal = parent.noise(x*noiseScale, x*noiseScale, noiseZ);
      float angle = noiseVal*2*(float)Math.PI;
      ran_x = parent.random(-1, 3);
      move_x = (float)Math.cos(angle) - ran_x;
    
      return move_x; 
    }
    public float moving_y()  {
      float noiseScale = parent.random((float)-0.005, (float)0.005); //parent.random(-1, 1);
      float noiseVal = parent.noise(y*noiseScale, y*noiseScale, noiseZ);
      float angle = noiseVal*2*(float)Math.PI;
      ran_y = parent.random(-3, 3);        
      move_y = (float)Math.sin(angle) - ran_y/2;      
      
      return move_y; 
    }
    
    public void test() {    	
    	System.out.println(x + ", " + y);
    }

/*    
    public void display_back()  {
      if (backTime == true)  {
        if ( bTime == 0 || bDistance == 0 )  
             return;
           //  x = x - (bDistance / bTime);
           //  y = y - (bDistance / bTime);
            x = x - (mDistance / mTime);
            y = y - (mDistance / mTime);
           
             bCurTime++;
        if ( bCurTime >= bTime)  {
          backTime = false;
          bTime = 0;
          bDistance = 0;
          bCurTime = 0;
        }
      }
    }
    */
    public void display_point(float x, float y, float z)  {
    	parent.stroke(255,50,0,150);
    	parent.point(x, y, z);
    } 
}
