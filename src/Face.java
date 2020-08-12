import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Face {
    PVector normal;
    PApplet sketch;
    int color;

    public Face(PApplet sketch, PVector normal, int color) {
        this.sketch = sketch;
        this.normal = normal;
        this.color = color;
    }

    public void turnX(float angle){
        PVector n2 = new PVector();
        n2.x = Math.round(normal.x);
        n2.y = Math.round(normal.y * Math.cos(angle)- normal.z * Math.sin(angle));
        n2.z = Math.round(normal.y * Math.sin(angle) + normal.z * Math.cos(angle));
        normal = n2;
    }
    public void turnY(float angle){
        PVector n2 = new PVector();
        n2.x =  Math.round(normal.x * Math.cos(angle) - normal.z * Math.sin(angle));
        n2.y = Math.round(normal.y);
        n2.z = Math.round(normal.x * Math.sin(angle) + normal.z * Math.cos(angle));
        normal = n2;
    }
    public void turnZ(float angle){
        PVector n2 = new PVector();
        n2.x =  Math.round(normal.x * Math.cos(angle) - normal.y * Math.sin(angle));
        n2.y = Math.round(normal.x * Math.sin(angle) + normal.y * Math.cos(angle));
        n2.z = Math.round(normal.z);
        normal = n2;
    }
    public void show(){
        sketch.pushMatrix();
        sketch.fill(color);
        sketch.rectMode(PConstants.CENTER);
        sketch.translate(0.5f * normal.x, 0.5f * normal.y, 0.5f * normal.z);
        if(Math.abs(normal.x) > 0){
            sketch.rotateY(PConstants.HALF_PI);
        } else if (Math.abs(normal.y) > 0) {
            sketch.rotateX(PConstants.HALF_PI);
        }
        sketch.square(0,0,1);
        sketch.popMatrix();
    }
}
