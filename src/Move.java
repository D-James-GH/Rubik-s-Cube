import processing.core.PConstants;

public class Move {
    float angle = 0;
    int x = 0;
    int y = 0;
    int z = 0;
    int dir;
    private boolean animating = false;
    private boolean finished = true;
    RubikCube cube;
    char turn;
    float doubleOrSingleTurn;


    public Move(float angle, int x, int y, int z, int dir, char turn, RubikCube cube) {
        this.angle = angle;
        this.dir = dir;
        this.x = x;
        this.y = y;
        this.z = z;
        this.turn = turn;
        this.cube = cube;
    }

    public void start() {
        animating = true;
        finished = false;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean getFinished() {
        return finished;
    }

    public void update() {
        if (animating) {
            angle += dir * 0.16;
            doubleOrSingleTurn = Math.abs(dir) == 1 ? PConstants.HALF_PI : PConstants.PI;

            if (Math.abs(angle) > doubleOrSingleTurn) {
                angle = 0;
                animating = false;
                finished = true;

                switch (turn) {
                    case 'z':
                        cube.turnZ(z, dir);
                        break;
                    case 'x':
                        cube.turnX(x, dir);
                        break;
                    case 'y':
                        cube.turnY(y, dir);
                        break;
                }

            }
        }
    }

}
