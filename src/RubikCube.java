import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;

public class RubikCube extends PApplet {
    PeasyCam cam;
    // initialize an 3 dimensional array where each array will be of length 3
    int numOfSides = 3;
    Move currentMove = new Move(0, 0, 0, 0, 1, 'x', this);
    Cubie[] cube = new Cubie[numOfSides * numOfSides * numOfSides];
    boolean solving = false;
    Solve solve = new Solve(this, cube, this);

    public void setup() {
        cam = new PeasyCam(this, 400);
        int index = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    PMatrix3D matrix = new PMatrix3D();
                    matrix.translate(x, y, z);
                    if ((abs(x) == abs(y)) && (abs(x) == abs(z) && abs(x) != 0)) {
                        cube[index] = new Cubie(this, matrix, x, y, z, "corner");
                    } else if (abs(x) + abs(y) + abs(z) == 1) {
                        cube[index] = new Cubie(this, matrix, x, y, z, "center");
                    } else if (abs(x) + abs(y) + abs(z) != 0) {
                        cube[index] = new Cubie(this, matrix, x, y, z, "edge");
                    } else {
                        cube[index] = new Cubie(this, matrix, x, y, z, "cubeCenter");
                    }
                    index++;
                }
            }
        }


    }


    public void turnZ(int index, int dir) {
        for (int i = 0; i < cube.length; i++) {
            Cubie qb = cube[i];
            if (cube[i].z == index) {
                PMatrix2D zMatrix = new PMatrix2D();
                zMatrix.rotate(HALF_PI * dir);
                zMatrix.translate(qb.x, qb.y);
                qb.update(round(zMatrix.m02), round(zMatrix.m12), qb.z);
                qb.turnFacesZ(dir);
            }
        }
    }

    public void turnY(int index, int dir) {
        for (int i = 0; i < cube.length; i++) {
            Cubie qb = cube[i];
            if (cube[i].y == index) {
                PMatrix2D zMatrix = new PMatrix2D();
                zMatrix.rotate(HALF_PI * dir);
                zMatrix.translate(qb.x, qb.z);
                qb.update(round(zMatrix.m02), qb.y, round(zMatrix.m12));
                qb.turnFacesY(dir);
            }
        }
    }

    public void turnX(int index, int dir) {
        for (int i = 0; i < cube.length; i++) {
            Cubie qb = cube[i];
            if (cube[i].x == index) {
                PMatrix2D zMatrix = new PMatrix2D();
                zMatrix.rotate(HALF_PI * dir);
                zMatrix.translate(qb.y, qb.z);
                qb.update(qb.x, round(zMatrix.m02), round(zMatrix.m12));
                qb.turnFacesX(dir);
            }
        }
    }

    private void genericTurn(float angle, int x, int y, int z, int dir, char turn) {
        if (currentMove.getFinished()) {
            currentMove = new Move(angle, x, y, z, dir, turn, this);

            currentMove.start();

        }
    }

    public void turnR(int dir) {
        genericTurn(0, 1, 0, dir, dir, 'x');

    }

    public void turnL(int dir) {
        genericTurn(0, -1, 0, -1 * dir, -1 * dir, 'x');
    }

    public void turnF(int dir) {
        genericTurn(0, 0, 0, 1, dir, 'z');
    }

    public void turnB(int dir) {
        genericTurn(0, 0, 0, -1, dir, 'z');
    }

    public void turnU(int dir) {
        genericTurn(0, 0, -1, dir, dir, 'y');
    }

    public void turnD(int dir) {
        genericTurn(0, 0, 1, -1 * dir, -1 * dir, 'y');
    }

    public void turnM(int dir) {
        genericTurn(0, 0, 0, dir, dir, 'x');
    }

    public void turnS(int dir) {
        genericTurn(0, 0, 0, 0, dir, 'z');
    }

    public void turnE(int dir) {
        genericTurn(0, 0, 0, dir, dir, 'y');
    }


    public void draw() {
        background(51);
        scale(50);
        if (solving) {
            if (frameCount % 12 == 0) {
                solve.solve();
            }
        }
        currentMove.update();

        for (int i = 0; i < cube.length; i++) {
            push();
            if (cube[i].z == currentMove.z && currentMove.turn == 'z') {
                rotateZ(currentMove.angle);
            } else if (cube[i].x == currentMove.x && currentMove.turn == 'x') {
                rotateX(currentMove.angle);
            } else if (cube[i].y == currentMove.y && currentMove.turn == 'y') {
                rotateY(-currentMove.angle);
            }
            cube[i].show();
            pop();
        }


    }


    public void keyPressed() {
        switch (key) {
            case 'b':
                turnB(-1);
                break;
            case 'B':
                turnB(1);
                break;
            case 'f':
                turnF(1);
                break;
            case 'F':
                turnF(-1);
                break;
            case 'u':
                turnU(1);
                break;
            case 'U':
                turnU(-1);
                break;
            case 'd':
                turnD(1);
                break;
            case 'D':
                turnD(-1);
                break;
            case 'l':
                turnL(1);
                break;
            case 'L':
                turnL(-1);
                break;
            case 'r':
                turnR(1);
                break;
            case 'R':
                turnR(-1);
                break;
//            case 'm':
//                turnM(1);
//                break;
//
//            case 'M':
//                turnM(-1);
//                break;
//            case 's':
//                turnS(1);
//                break;
//            case 'S':
//                turnS(-1);
//                break;
//            case 'e':
//                turnE(1);
//                break;
//            case 'E':
//                turnE(-1);
//                break;
            case ' ':
                if (solving) {
                    solve.reset();
                    solving = false;
                    System.out.println(solving);
                } else {
                    solving = true;
                    solve.reset();
                    System.out.println(solving);

                }
                break;


        }
    }


    public void settings() {
        size(800, 800, P3D);
    }

}
