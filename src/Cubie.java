import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PMatrix3D;
import processing.core.PVector;

public class Cubie {
    private PApplet sketch;
    PMatrix3D matrix;
    int x = 0;
    int y = 0;
    int z = 0;

    String pieceType;
    Face[] faces = new Face[6];
    boolean isSolved;
    int color;

    int xColor;
    int xMinColor;
    int yColor;
    int yMinColor;
    int zColor;
    int zMinColor;


    public Cubie(PApplet sketch, PMatrix3D m, int x, int y, int z, String pieceType) {
        this.sketch = sketch;
        this.matrix = m;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pieceType = pieceType;

        color = sketch.color(0);
        xColor = color;
        xMinColor = color;
        yColor = color;
        yMinColor = color;
        zColor = color;
        zMinColor = color;

        if (this.x == 1) {
            xColor = sketch.color(255, 150, 0);
        } else if (this.x == -1) {
            xMinColor = sketch.color(255, 0, 0);
        }
        if (this.y == 1) {
            yColor = sketch.color(255, 255, 255);
        } else if (this.y == -1) {
            yMinColor = sketch.color(255, 255, 0);
        }
        if (this.z == 1) {
            zColor = sketch.color(0, 255, 0);
        } else if (this.z == -1) {
            zMinColor = sketch.color(0, 0, 255);
        }

        faces[0] = new Face(this.sketch, new PVector(1, 0, 0), xColor); // orange
        faces[1] = new Face(this.sketch, new PVector(0, 1, 0), yColor); // white
        faces[2] = new Face(this.sketch, new PVector(0, 0, 1), zColor); // green
        faces[3] = new Face(this.sketch, new PVector(-1, 0, 0), xMinColor); // red
        faces[4] = new Face(this.sketch, new PVector(0, -1, 0), yMinColor); // yellow
        faces[5] = new Face(this.sketch, new PVector(0, 0, -1), zMinColor); // blue


    }


    public void show() {

        sketch.noFill();
        sketch.stroke(0);
        sketch.strokeWeight(0.1f);
        sketch.pushMatrix();
        sketch.applyMatrix(matrix);
        sketch.box(1);
        for (Face f : faces) {
            f.show();
        }
        sketch.popMatrix();
    }

    public void update(int x, int y, int z) {
        matrix.reset();
        matrix.translate(x, y, z);
        this.x = x;
        this.y = y;
        this.z = z;

    }

    public void turnFacesZ(int dir) {
        for (Face f : faces) {
            f.turnZ(dir * PConstants.HALF_PI);
        }
    }

    public void turnFacesX(int dir) {
        for (Face f : faces) {
            f.turnX(dir * PConstants.HALF_PI);
        }
    }

    public void turnFacesY(int dir) {
        for (Face f : faces) {
            f.turnY(dir * PConstants.HALF_PI);
        }
    }

    public boolean getIsSolved() {
        return isSolved;
    }

    public void setIsSolved(boolean isSolved) {
        this.isSolved = isSolved;
    }
}
