
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class Solve {
    Cubie[] cube;
    PApplet sketch;
    RubikCube RubikCube;
    Cubie[] whiteEdges = new Cubie[4];
    Cubie[] whiteCorners = new Cubie[4];
    Cubie[] midLayer = new Cubie[4];
    Cubie[] lastLayer = new Cubie[9];
    Cubie[] lastLayerEdges = new Cubie[4];
    Cubie[] lastLayerCorners = new Cubie[4];
    Cubie currentMidPiece;

    List<Character> moves = new ArrayList<Character>();
    boolean whiteRedComplete = false;
    boolean whiteBlueComplete = false;
    boolean whiteGreenComplete = false;
    boolean whiteOrangeComplete = false;
    boolean whiteCrossComplete = false;

    // white corners
    boolean whiteCornersComplete = false;
    boolean redBlueWhiteCornerComplete = false;
    boolean redGreenWhiteCornerComplete = false;
    boolean orangeBlueWhiteCornerComplete = false;
    boolean greenOrangeWhiteCornerComplete = false;

    // mid layer
    boolean redGreenComplete = false;
    boolean redBlueComplete = false;
    boolean blueOrangeComplete = false;
    boolean greenOrangeComplete = false;
    boolean midLayerComplete = false;
    //last layer
    boolean yellowCrossComplete = false;
    boolean orientateLLComplete = false;
    boolean yellowCornersComplete = false;
    boolean cubeComplete = false;

    public Solve(PApplet sketch, Cubie[] cube, RubikCube RubikCube) {
        this.cube = cube;
        this.sketch = sketch;
        this.RubikCube = RubikCube;
    }

    public void solve() {
        if (moves.size() <= 0) {
            whiteCross();

            if (whiteCrossComplete && !whiteCornersComplete) {
                whiteCorners();
            }
            if (whiteCrossComplete && whiteCornersComplete && !midLayerComplete) {
                midLayer();
            }

            if (whiteCrossComplete && whiteCornersComplete && midLayerComplete && !orientateLLComplete) {
                orientateLL();
            }
            if (whiteCrossComplete && whiteCornersComplete && midLayerComplete && orientateLLComplete && !yellowCornersComplete) {
                solveCornersLL();
            }
            if (whiteCrossComplete && whiteCornersComplete && midLayerComplete && orientateLLComplete && yellowCornersComplete && !cubeComplete) {
                solveEdgesLL();
            }

        } else {
            char newMove = moves.get(0);
            keyPress(newMove);
            moves.remove(0);
        }
    }

    private void findWhiteEdges() {
        int index = 0;
        for (int i = 0; i < cube.length; i++) {
            if (cube[i].yColor == sketch.color(255, 255, 255) && cube[i].pieceType.equals("edge")) {
                whiteEdges[index] = cube[i];
                index++;
            }
        }
    }

    private void findWhiteCorners() {
        int index = 0;
        for (int i = 0; i < cube.length; i++) {
            if (cube[i].yColor == sketch.color(255, 255, 255) && cube[i].pieceType.equals("corner")) {
                whiteCorners[index] = cube[i];
                index++;
            }
        }
    }


    public void checkMiddle() {
        // make sure middles are right
    }

    public void whiteCross() {
        if (whiteEdges[0] == null) {
            findWhiteEdges();
        }
        // steps to solve:
        // 1. Concentrate on one piece at a time
        // 2. put piece on yellow side(top)
        // 3. rotate up until the piece is above where it should go
        // 4 insert piece to correct orientation and position on white side
        if (whiteOrangeComplete && whiteGreenComplete & whiteBlueComplete && whiteRedComplete) {
            whiteCrossComplete = true;
            return;
        }
        // left (white red)
        if (!whiteRedComplete) {
            whiteRedEdge(whiteEdges[0]);
            return;
        }
        if (!whiteBlueComplete) {
            whiteBlueEdge(whiteEdges[1]);
            return;
        }
        if (!whiteGreenComplete) {
            whiteGreenEdge(whiteEdges[2]);
            return;
        }
        whiteOrangeEdge(whiteEdges[3]);
    }

    public void whiteCorners() {
        if (whiteCorners[0] == null) {
            findWhiteCorners();
        }
        if (redGreenWhiteCornerComplete && greenOrangeWhiteCornerComplete && orangeBlueWhiteCornerComplete && redBlueWhiteCornerComplete) {
            whiteCornersComplete = true;
        }
        if (!redBlueWhiteCornerComplete) {
            redBlueWhiteCorner(whiteCorners[0]);
            return;
        }
        if (!redGreenWhiteCornerComplete) {
            redGreenWhiteCorner(whiteCorners[1]);
            return;
        }
        if (!orangeBlueWhiteCornerComplete) {
            orangeBlueWhiteCorner(whiteCorners[2]);
            return;
        }
        if (!greenOrangeWhiteCornerComplete) {
            greenOrangeWhiteCorner(whiteCorners[3]);
        }
    }

    private void findMidLayer() {
        int index = 0;
        for (Cubie cubie : cube) {
            if (cubie.yMinColor != sketch.color(255, 255, 0) && cubie.yColor != sketch.color(255, 255, 255) && cubie.pieceType.equals("edge")) {
                midLayer[index] = cubie;
                index++;
            }
        }
    }

    public void midLayer() {
        if (midLayer[0] == null) {
            findMidLayer();
        }
        // find a mid layer piece on the top of the cube
        if (currentMidPiece == null) {
            for (Cubie cubie : midLayer) {
                if (cubie.y == -1) {
                    currentMidPiece = cubie;
                }
            }
            if (currentMidPiece == null && !midLayerComplete) {
                for (Cubie cubie : midLayer) {
                    if (!cubie.getIsSolved()) {
                        if (cubie.x == 1 && cubie.z == 1 && cubie.faces[2].normal.z != 1 && cubie.faces[0].normal.x != 1) { // make sure it is not complete
                            insertGreenOrange();
                            currentMidPiece = cubie;
                            return;

                        } else if (cubie.x == 1 && cubie.z == -1 && cubie.faces[0].normal.x != 1 && cubie.faces[5].normal.z != -1) {
                            insertBlueOrange();
                            currentMidPiece = cubie;
                            return;
                        } else if (cubie.x == -1 && cubie.z == -1 && cubie.faces[5].normal.z != -1 && cubie.faces[3].normal.x != -1) {
                            insertRedBlue();
                            currentMidPiece = cubie;
                            return;
                        } else if (cubie.x == -1 && cubie.z == 1 && cubie.faces[3].normal.x != -1 && cubie.faces[2].normal.z != 1) {
                            insertGreenRed();
                            currentMidPiece = cubie;
                            return;
                        } else {
                            cubie.setIsSolved(true);
                        }

                    }
                }
                midLayerComplete = true;
            }
        } else {
            solveMidPiece(currentMidPiece);

        }

    }

    private void findLL() {
        int index = 0;
        for (int i = 0; i < cube.length; i++) {
            if (cube[i].yMinColor == sketch.color(255, 255, 0)) {
                lastLayer[index] = cube[i];
                index++;
            }
        }
    }

    private void findEdgesLL() {
        int index = 0;
        for (int i = 0; i < cube.length; i++) {
            if (cube[i].yMinColor == sketch.color(255, 255, 0) && cube[i].pieceType.equals("edge")) {
                lastLayerEdges[index] = cube[i];
                index++;
            }
        }
    }

    private void findCornersLL() {
        int index = 0;
        for (Cubie cubie : cube) {
            if (cubie.yMinColor == sketch.color(255, 255, 0) && cubie.pieceType.equals("corner")) {
                lastLayerCorners[index] = cubie;
                index++;
            }
        }
    }

    private void orientateLL() {
        // get cross
        // - if single yellow center
        if (!yellowCrossComplete) {
            solveYellowCross();
        } else if (!orientateLLComplete) {
            rotateYellowCorners();
        }


    }

    // bellow are the helper functions for the white cross ========================

    private void whiteEdgeMidToTop(Cubie piece) {
        if (piece.x == 1) {
            if (piece.z == -1) {
                moves.add('R');
                moves.add('U');
                moves.add('r');
            } else {
                moves.add('r');
                moves.add('u');
                moves.add('R');
            }
        } else {
            if (piece.z == -1) {
                moves.add('l');
                moves.add('u');
                moves.add('L');
            } else {
                moves.add('L');
                moves.add('u');
                moves.add('l');
            }
        }
    }

    private void whiteBottomToTop(Cubie piece) {
        switch (piece.x) {
            case 0:
                if (piece.z == 1) {
                    RubikCube.turnF(2);
                } else {
                    RubikCube.turnB(2);
                }
                break;
            case -1:
                RubikCube.turnL(2);
                break;
            case 1:
                RubikCube.turnR(2);
                break;
        }
    }

    private void whiteOrangeEdge(Cubie piece) {

        // if correct
        if (piece.faces[1].normal.y == 1 && piece.x == 1) {
            whiteOrangeComplete = true;
            return;
        }
        // if on the bottom but in the wrong place
        if (piece.y == 1 && piece.faces[0].normal.x != 1) {
            whiteBottomToTop(piece);
            return;
        }
        if (piece.y == 0) {
            whiteEdgeMidToTop(piece);
            return;
        }
        // if top layer but not over location
        if (piece.y == -1 && piece.x != 1) {
            RubikCube.turnU(1);
            return;
        }
        // below assumes white pieces are on the top and above right position-------------

        // right orientation but on top
        if (piece.faces[1].normal.y == -1 && piece.faces[0].normal.x == 1) {
            RubikCube.turnR(2);
            return;
        }
        if (piece.faces[1].normal.x == 1 && piece.faces[0].normal.y == -1) {
            moves.add('u');
            moves.add('f');
            moves.add('R');
            moves.add('F');
            return;
        }
    }

    private void whiteRedEdge(Cubie piece) {

        // if correct
        if (piece.faces[1].normal.y == 1 && piece.x == -1) {
            whiteRedComplete = true;
            return;
        }
        // if on the bottom but in the wrong place
        if (piece.y == 1 && piece.faces[3].normal.x != -1) {
            whiteBottomToTop(piece);
            return;
        }
        // if piece is on mid.
        if (piece.y == 0) {
            whiteEdgeMidToTop(piece);
            return;
        }
        // if top layer but not over location
        if (piece.y == -1 && piece.x != -1) {
            RubikCube.turnU(1);
            return;
        }
        // below assumes white pieces are on the top and above right position-------------

        // right orientation but on top
        if (piece.faces[1].normal.y == -1 && piece.x == -1) {
            RubikCube.turnL(2);
            return;
        }

        // if white and red are on top but wrong orientation
        if (piece.faces[1].normal.x == -1 && piece.faces[3].normal.y == -1) {
            moves.add('U');
            moves.add('F');
            moves.add('l');
            moves.add('f');
            return;
        }
    }

    private void whiteBlueEdge(Cubie piece) {

        if (piece.faces[1].normal.y == 1 && piece.faces[5].normal.z == -1) {
            whiteBlueComplete = true;
            return;
        }
        // blue white (back) face[5] is blue
        // if on the bottom but in the wrong place
        if (piece.y == 1 && piece.faces[5].normal.z != -1) {
            whiteBottomToTop(piece);
            return;
        }
        // if piece is not on the bottom ----------------------------
        // if its on mid layer
        if (piece.y == 0) {
            whiteEdgeMidToTop(piece);
            return;
        }

        // if top layer but not over location
        if (piece.y == -1 && piece.z != -1) {
            RubikCube.turnU(1);
            return;
        }

        // right orientation but on top

        if (piece.faces[1].normal.y == -1 && piece.faces[5].normal.z == -1) {
            RubikCube.turnB(2);
            return;

        }

        if (piece.faces[1].normal.z == -1 && piece.faces[5].normal.y == -1) {

            moves.add('u');
            moves.add('r');
            moves.add('B');
            moves.add('R');
            return;
        }
    }

    private void whiteGreenEdge(Cubie piece) {

        if (piece.faces[1].normal.y == 1 && piece.faces[2].normal.z == 1) {
            whiteGreenComplete = true;
            return;
        }
        // green white edge piece face[2] is green
        // if on the bottom but in the wrong place
        if (piece.y == 1 && piece.faces[2].normal.z != 1) {
            whiteBottomToTop(piece);
            return;
        }
        // if piece is not on the bottom ----------------------------
        // if its on mid layer
        if (piece.y == 0) {
            whiteEdgeMidToTop(piece);
            return;
        }
        // if top layer but not over location
        if (piece.y == -1 && piece.z != 1) {
            RubikCube.turnU(1);
            return;
        }
        // right orientation but on top

        if (piece.faces[1].normal.y == -1 && piece.faces[2].normal.z == 1) {
            RubikCube.turnF(2);
            return;
        }

        if (piece.faces[1].normal.z == 1 && piece.faces[2].normal.y == -1) {
            moves.add('U');
            moves.add('R');
            moves.add('f');
            moves.add('r');
            return;
        }

    }

    // below are the helper functions for white corners ========================================
    private void rightBackCornerToTop() {
        moves.add('R');
        moves.add('U');
        moves.add('r');
    }

    private void rightFrontCornerToTop() {
        moves.add('r');
        moves.add('u');
        moves.add('R');
    }

    private void leftBackCornerToTop() {
        moves.add('l');
        moves.add('u');
        moves.add('L');
    }

    private void leftFrontCornerToTop() {
        moves.add('L');
        moves.add('U');
        moves.add('l');
    }

    private void cornerBottomToTop(Cubie piece) {
        if (piece.x == 1) {
            if (piece.z == -1) {
                rightBackCornerToTop();
            } else {
                rightFrontCornerToTop();
            }
        } else {
            if (piece.z == -1) {
                leftBackCornerToTop();
            } else {
                leftFrontCornerToTop();
            }
        }
    }

    private void redBlueWhiteCorner(Cubie piece) {
        // is it completed?
        if (piece.faces[1].normal.y == 1 && piece.faces[5].normal.z == -1 && piece.faces[3].normal.x == -1) {
            redBlueWhiteCornerComplete = true;
            return;
        }
        // if on bottom but wrong place
        // red and blue
        if (piece.y == 1 && (piece.faces[5].normal.z != -1 || piece.faces[3].normal.x != -1)) {
            cornerBottomToTop(piece);
            return;
        }
        // if on top but not above where it should be
        if (piece.y == -1 && (piece.x != -1 || piece.z != -1)) {
            RubikCube.turnU(1);
            return;
        }

        // put the corner in
        if (piece.y == -1 && piece.faces[1].normal.z == -1) {
            moves.add('B');
            moves.add('U');
            moves.add('b');
            return;
        }
        if (piece.y == -1 && piece.faces[1].normal.x == -1) {
            moves.add('l');
            moves.add('u');
            moves.add('L');
            return;

        }
        if (piece.y == -1 && piece.faces[1].normal.y == -1) {
            moves.add('l');
            moves.add('u');
            moves.add('u');
            moves.add('L');
            moves.add('U');
            moves.add('l');
            moves.add('u');
            moves.add('L');
            return;
        }
    }

    private void redGreenWhiteCorner(Cubie piece) {
        // is it complete?
        if (piece.faces[1].normal.y == 1 && piece.faces[2].normal.z == 1 && piece.faces[3].normal.x == -1) {
            redGreenWhiteCornerComplete = true;
            return;
        }
        // if on bottom but wrong place
        // red and blue
        if (piece.y == 1 && (piece.faces[2].normal.z != 1 || piece.faces[3].normal.x != -1)) {
            cornerBottomToTop(piece);
            return;
        }
        // if on top but not above where it should be
        if (piece.y == -1 && (piece.x != -1 || piece.z != 1)) {
            RubikCube.turnU(1);
            return;
        }
        // put the corner in
        if (piece.y == -1 && piece.faces[1].normal.z == 1) {
            moves.add('f');
            moves.add('u');
            moves.add('F');
            return;
        }
        if (piece.y == -1 && piece.faces[1].normal.x == -1) {
            moves.add('L');
            moves.add('U');
            moves.add('l');
            return;

        }
        if (piece.y == -1 && piece.faces[1].normal.y == -1) {
            moves.add('L');
            moves.add('U');
            moves.add('U');
            moves.add('l');
            moves.add('u');
            moves.add('L');
            moves.add('U');
            moves.add('l');
            return;
        }
    }

    private void orangeBlueWhiteCorner(Cubie piece) {
        // is it completed?
        if (piece.faces[1].normal.y == 1 && piece.faces[5].normal.z == -1 && piece.faces[0].normal.x == 1) {
            orangeBlueWhiteCornerComplete = true;
            return;
        }
        // if on bottom but wrong place
        // red and blue
        if (piece.y == 1 && (piece.faces[5].normal.z != -1 || piece.faces[0].normal.x != 1)) {
            cornerBottomToTop(piece);
            return;
        }
        // if on top but not above where it should be
        if (piece.y == -1 && (piece.x != 1 || piece.z != -1)) {
            RubikCube.turnU(1);
            return;
        }
        // put the corner in
        if (piece.y == -1 && piece.faces[1].normal.z == -1) {
            moves.add('b');
            moves.add('u');
            moves.add('B');
            return;
        }
        if (piece.y == -1 && piece.faces[1].normal.x == 1) {
            moves.add('R');
            moves.add('U');
            moves.add('r');
            return;
        }
        if (piece.y == -1 && piece.faces[1].normal.y == -1) {
            moves.add('R');
            moves.add('u');
            moves.add('r');
            moves.add('U');
            moves.add('U');
            moves.add('R');
            moves.add('U');
            moves.add('r');
            return;
        }
    }

    private void greenOrangeWhiteCorner(Cubie piece) {
        // is it completed?
        if (piece.faces[1].normal.y == 1 && piece.faces[2].normal.z == 1 && piece.faces[0].normal.x == 1) {
            greenOrangeWhiteCornerComplete = true;
            return;
        }
        // if on bottom but wrong place
        // red and blue
        if (piece.y == 1 && (piece.faces[2].normal.z != 1 || piece.faces[0].normal.x != 1)) {
            cornerBottomToTop(piece);
            return;
        }
        // if on top but not above where it should be
        if (piece.y == -1 && (piece.x != 1 || piece.z != 1)) {
            RubikCube.turnU(1);
            return;
        }
        // put the corner in
        if (piece.y == -1 && piece.faces[1].normal.z == 1) {
            moves.add('F');
            moves.add('U');
            moves.add('f');
            return;
        }
        if (piece.y == -1 && piece.faces[1].normal.x == 1) {
            moves.add('r');
            moves.add('u');
            moves.add('R');
            return;
        }
        if (piece.y == -1 && piece.faces[1].normal.y == -1) {
            moves.add('r');
            moves.add('u');
            moves.add('u');
            moves.add('R');
            moves.add('U');
            moves.add('r');
            moves.add('u');
            moves.add('R');
            return;
        }
    }
    // solving mid layer =================================================================

    private void solveMidPiece(Cubie piece) {
        // if orange side
        if (piece.xColor == sketch.color(255, 150, 0) && piece.faces[0].normal.y != -1 && piece.faces[0].normal.x != 1) {
            RubikCube.turnU(1);
            return;
        }
        // green side
        if (piece.zColor == sketch.color(0, 255, 0) && piece.faces[2].normal.y != -1 && piece.faces[2].normal.z != 1) {
            RubikCube.turnU(1);
            return;
        }
        // red side
        if (piece.xMinColor == sketch.color(255, 0, 0) && piece.faces[3].normal.y != -1 && piece.faces[3].normal.x != -1) {
            RubikCube.turnU(1);
            return;
        }
        // blue side
        if (piece.zMinColor == sketch.color(0, 0, 255) && piece.faces[5].normal.y != -1 && piece.faces[5].normal.z != -1) {
            RubikCube.turnU(1);
            return;
        }

        // should be above the right place.
        // put in mid layer

        // if orange
        if (piece.xColor == sketch.color(255, 150, 0)) {
            if (piece.zMinColor == sketch.color(0, 0, 255)) {
                insertOrangeBlue();
                blueOrangeComplete = true;
                piece.setIsSolved(true);
                currentMidPiece = null;
            } else {
                insertOrangeGreen();
                greenOrangeComplete = true;
                piece.setIsSolved(true);
                currentMidPiece = null;
            }
            return;
        }
        // green side
        if (piece.zColor == sketch.color(0, 255, 0)) {
            if (piece.xColor == sketch.color(255, 150, 0)) {
                insertGreenOrange();
                greenOrangeComplete = true;
                piece.setIsSolved(true);
                currentMidPiece = null;
            } else {
                insertGreenRed();
                redGreenComplete = true;
                piece.setIsSolved(true);
                currentMidPiece = null;
            }
            return;
        }
        // red
        if (piece.xMinColor == sketch.color(255, 0, 0)) {
            if (piece.zColor == sketch.color(0, 255, 0)) {
                insertRedGreen();
                redGreenComplete = true;
                piece.setIsSolved(true);
                currentMidPiece = null;
            } else {
                insertRedBlue();
                redBlueComplete = true;
                piece.setIsSolved(true);
                currentMidPiece = null;
            }
            return;
        }
        // blue side
        if (piece.zMinColor == sketch.color(0, 0, 255)) {
            if (piece.xColor == sketch.color(255, 150, 0)) {
                insertBlueOrange();
                blueOrangeComplete = true;
                piece.setIsSolved(true);
                currentMidPiece = null;
            } else {
                insertBlueRed();
                piece.setIsSolved(true);
                redBlueComplete = true;
                currentMidPiece = null;
            }
            return;
        }

    }

    private void insertOrangeBlue() {
        moves.add('u');
        moves.add('b');
        moves.add('U');
        moves.add('B');
        moves.add('U');
        moves.add('R');
        moves.add('u');
        moves.add('r');
    }

    private void insertOrangeGreen() {
        moves.add('U');
        moves.add('F');
        moves.add('u');
        moves.add('f');
        moves.add('u');
        moves.add('r');
        moves.add('U');
        moves.add('R');
    }

    private void insertGreenOrange() {
        moves.add('u');
        moves.add('r');
        moves.add('U');
        moves.add('R');
        moves.add('U');
        moves.add('F');
        moves.add('u');
        moves.add('f');
    }

    private void insertGreenRed() {
        moves.add('U');
        moves.add('L');
        moves.add('u');
        moves.add('l');
        moves.add('u');
        moves.add('f');
        moves.add('U');
        moves.add('F');
    }

    private void insertRedGreen() {
        moves.add('u');
        moves.add('f');
        moves.add('U');
        moves.add('F');
        moves.add('U');
        moves.add('L');
        moves.add('u');
        moves.add('l');
    }

    private void insertRedBlue() {
        moves.add('U');
        moves.add('B');
        moves.add('u');
        moves.add('b');
        moves.add('u');
        moves.add('l');
        moves.add('U');
        moves.add('L');
    }

    private void insertBlueOrange() {
        moves.add('U');
        moves.add('R');
        moves.add('u');
        moves.add('r');
        moves.add('u');
        moves.add('b');
        moves.add('U');
        moves.add('B');
    }

    private void insertBlueRed() {
        moves.add('u');
        moves.add('l');
        moves.add('U');
        moves.add('L');
        moves.add('U');
        moves.add('B');
        moves.add('u');
        moves.add('b');
    }
    //
    //orientate last layer helper functions ========================================================

    private void solveYellowCross() {
        findEdgesLL();
        int numberFacingUp = 0;
        int index = 0;
        int[][] coordinates = new int[4][2];


        for (Cubie lastLayerEdge : lastLayerEdges) {

            if (lastLayerEdge.faces[4].normal.y == -1) {
                numberFacingUp += 1;
                coordinates[index][0] = lastLayerEdge.x;
                coordinates[index][1] = lastLayerEdge.z;
                index++;
            }
        }
        switch (numberFacingUp) {
            case 0:
                moves.add('f');
                moves.add('r');
                moves.add('u');
                moves.add('R');
                moves.add('U');
                moves.add('s');
                moves.add('r');
                moves.add('u');
                moves.add('R');
                moves.add('U');
                moves.add('F');
                moves.add('S');
                break;

            case 2:
                // yellow makes a line
                if (coordinates[0][1] == 0 && coordinates[1][1] == 0) { // horizontal line
                    moves.add('u');
                    //
                    yellowCrossAlgor();
                } else if (coordinates[0][0] == 0 && coordinates[1][0] == 0) { // vertical line
                    yellowCrossAlgor();
                } else if ((coordinates[0][1] == 1 || coordinates[0][0] == 1) && (coordinates[1][0] == 1 || coordinates[1][1] == 1)) { // front and right are yellow
                    moves.add('s');
                    //
                    yellowCrossAlgor();
                    //
                    moves.add('S');
                } else if ((coordinates[0][1] == -1 || coordinates[0][0] == 1) && (coordinates[1][1] == -1 || coordinates[1][0] == 1)) { // back and right are yellow
                    moves.add('u');
                    moves.add('s');
                    //
                    yellowCrossAlgor();

                    //
                    moves.add('S');
                } else if ((coordinates[0][0] == -1 || coordinates[0][1] == -1) && (coordinates[1][0] == -1 || coordinates[1][1] == -1)) { // back and left
                    moves.add('f');
                    moves.add('u');
                    moves.add('r');
                    moves.add('U');
                    moves.add('R');
                    moves.add('F');
                } else if ((coordinates[0][0] == -1 || coordinates[0][1] == 1) && (coordinates[1][0] == -1 || coordinates[1][1] == 1)) { // left and front
                    moves.add('U');
                    moves.add('s');
                    //
                    yellowCrossAlgor();
                    //
                    moves.add('S');
                }
                break;

            case 4:
                // complete
                yellowCrossComplete = true;
        }
    }

    private void yellowCrossAlgor() {
        moves.add('f');
        moves.add('r');
        moves.add('u');
        moves.add('R');
        moves.add('U');
        moves.add('F');
    }

    private void rotateYellowCorners() {
        findCornersLL();
        int numberFacingUp = 0;
        int index = 0;
        int[][] coordinates = new int[4][2]; // coordinates of the correctly rotated yellow pieces
        int xTotal = 0; // of yellow faces in that direction
        int zTotal = 0; // of yellow faces in that direction
        int absXCount = 0; // of yellow faces in that direction
        int absZCount = 0; // of yellow faces in that direction
        for (Cubie lastLayerCorner : lastLayerCorners) {

            xTotal += lastLayerCorner.faces[4].normal.x;
            zTotal += lastLayerCorner.faces[4].normal.z;
            if (Math.abs(lastLayerCorner.faces[4].normal.x) == 1) {
                absXCount++;
            } else if (Math.abs(lastLayerCorner.faces[4].normal.z) == 1) {
                absZCount++;
            }
            if (lastLayerCorner.faces[4].normal.y == -1) {
                numberFacingUp += 1;
                coordinates[index][0] = lastLayerCorner.x;
                coordinates[index][1] = lastLayerCorner.z;
                index++;
            }
        }

        switch (numberFacingUp) {
            case 4:
                orientateLLComplete = true;
                break;
            case 0:

                if (absXCount == 4) {
                    noCornerOppFacing();
                } else if (absZCount == 4) {
                    moves.add('u');
                    noCornerOppFacing();
                } else if (absXCount == 2 && absZCount == 2) {
                    if (xTotal == -2 && zTotal == 0) {
                        noCornerTwoSame();
                    } else if (zTotal == -2 && xTotal == 0) {
                        moves.add('U');
                        noCornerTwoSame();
                    } else if (xTotal == 2 && zTotal == 0) {
                        moves.add('U');
                        moves.add('U');
                        noCornerTwoSame();
                    } else if (zTotal == 2 && xTotal == 0) {
                        moves.add('u');
                        noCornerTwoSame();
                    }
                }
                break;
            case 1:
                // two possible algorithms 
                int xCo = coordinates[0][0];
                int zCo = coordinates[0][1];
                if (zTotal == 0) {
                    if (zCo == 1 && xCo == -1) {
                        oneCornerRightHanded();
                    } else if (zCo == -1 && xCo == 1) {
                        moves.add('u');
                        moves.add('u');
                        oneCornerRightHanded();
                    } else if (zCo == 1 && xCo == 1) {
                        oneCornerLeftHanded();
                    } else {
                        moves.add('u');
                        moves.add('u');
                        oneCornerLeftHanded();
                    }
                } else if (xTotal == 0) {
                    if (xCo == 1 && zCo == 1) {
                        moves.add('u');
                        oneCornerRightHanded();
                    } else if (xCo == -1 && zCo == -1) {
                        moves.add('U');
                        oneCornerRightHanded();
                    } else if (xCo == 1 && zCo == -1) {
                        moves.add('u');
                        oneCornerLeftHanded();
                    } else {
                        moves.add('U');
                        oneCornerLeftHanded();
                    }
                }
                break;
            case 2:
                // three possible algorithms

                // diagonal
                if (coordinates[0][0] != coordinates[1][0] && coordinates[0][1] != coordinates[1][1]) {
                    if (zTotal != 1 && xTotal != 1) {
                        RubikCube.turnU(1);
                    } else {
                        twoCornerDiagonal();
                    }
                } else if (zTotal == 0 && xTotal == 0) { // both other yellow pieces are facing away
                    if (absZCount != 2 && coordinates[0][0] != 1) {
                        RubikCube.turnU(1);
                    } else {
                        twoCornerOppFacing();
                    }
                } else if (absXCount == 2 || absZCount == 2) { // other two yellow pieces are facing the same way
                    if (zTotal != 2) {
                        RubikCube.turnU('1');
                    } else {
                        twoCornerSameFacing();
                    }
                }
                break;
        }
    }

    private void solveCornersLL() {
        // two possible algorithms
        // detect which algorithm to use by seeing if two colors on any side match
        // if true use verticalCornerSwap();
        //
        // Don't think i need to finds corners again as it never resets
        findCornersLL();
        int orangeIndex = 0;
        int[][] orangeNormalDir = new int[2][2];
        int greenIndex = 0;
        int[][] greenNormalDir = new int[2][2];
        int redIndex = 0;
        int[][] redNormalDir = new int[2][2];
        int blueIndex = 0;
        int[][] blueNormalDir = new int[2][2];
        int[] greenRedCoordinates = new int[2];


        for (Cubie lastLayerCorner : lastLayerCorners) {
            // orange side
            if (lastLayerCorner.xColor == sketch.color(255, 150, 0)) {
                orangeNormalDir[orangeIndex][0] = (int) lastLayerCorner.faces[0].normal.x;
                orangeNormalDir[orangeIndex][1] = (int) lastLayerCorner.faces[0].normal.z;
                orangeIndex++;
            }
            if (lastLayerCorner.zColor == sketch.color(0, 255, 0)) {
                greenNormalDir[greenIndex][0] = (int) lastLayerCorner.faces[2].normal.x;
                greenNormalDir[greenIndex][1] = (int) lastLayerCorner.faces[2].normal.z;
                greenIndex++;
            }
            if (lastLayerCorner.xMinColor == sketch.color(255, 0, 0)) {
                redNormalDir[redIndex][0] = (int) lastLayerCorner.faces[3].normal.x;
                redNormalDir[redIndex][1] = (int) lastLayerCorner.faces[3].normal.z;
                redIndex++;
            }
            if (lastLayerCorner.xMinColor == sketch.color(0, 0, 255)) {
                blueNormalDir[blueIndex][0] = (int) lastLayerCorner.faces[5].normal.x;
                blueNormalDir[blueIndex][1] = (int) lastLayerCorner.faces[5].normal.z;
                blueIndex++;
            }
            if (lastLayerCorner.zColor == sketch.color(0, 255, 0) && lastLayerCorner.xMinColor == sketch.color(255, 0, 0)) {
                greenRedCoordinates[0] = lastLayerCorner.x;
                greenRedCoordinates[1] = lastLayerCorner.z;
            }

        }

        if (orangeNormalDir[0][0] == orangeNormalDir[1][0] && redNormalDir[0][0] == redNormalDir[1][0]) {
            if (orangeNormalDir[0][0] != 1) {
                RubikCube.turnU(1);
            } else {
                yellowCornersComplete = true;
            }
        } else if ((orangeNormalDir[0][0] == orangeNormalDir[1][0] && orangeNormalDir[0][0] != 0) || (orangeNormalDir[0][1] == orangeNormalDir[1][1] && orangeNormalDir[0][1] != 0)) {
            helperTShape(orangeNormalDir);
        } else if ((greenNormalDir[0][0] == greenNormalDir[1][0] && greenNormalDir[0][0] != 0) || (greenNormalDir[0][1] == greenNormalDir[1][1] && greenNormalDir[0][1] != 0)) {
            helperTShape(greenNormalDir);
        } else if ((redNormalDir[0][0] == redNormalDir[1][0] && redNormalDir[0][0] != 0) || (redNormalDir[0][1] == redNormalDir[1][1] && redNormalDir[0][1] != 0)) {
            helperTShape(redNormalDir);
        } else if ((blueNormalDir[0][0] == blueNormalDir[1][0] && blueNormalDir[0][0] != 0) || (blueNormalDir[0][1] == blueNormalDir[1][1] && blueNormalDir[0][1] != 0)) {
            helperTShape(blueNormalDir);
        } else {
            if (greenRedCoordinates[0] == -1 && greenRedCoordinates[1] == 1) {
                diagonalCornerSwap();
            } else if (greenRedCoordinates[0] == -1 && greenRedCoordinates[1] == -1) {
                moves.add('U');
                diagonalCornerSwap();
            } else if (greenRedCoordinates[0] == 1 && greenRedCoordinates[1] == -1) {
                moves.add('U');
                moves.add('U');
                diagonalCornerSwap();
            } else if (greenRedCoordinates[0] == 1 && greenRedCoordinates[1] == 1) {
                moves.add('u');
                diagonalCornerSwap();
            }
        }
    }

    private void helperTShape(int[][] normalDir) {
        if (normalDir[0][0] == -1) {
            verticalCornerSwap();
        } else if (normalDir[0][0] == 1) {
            moves.add('u');
            moves.add('u');
            verticalCornerSwap();
        } else if (normalDir[0][1] == 1) {
            moves.add('u');
            verticalCornerSwap();
        } else if (normalDir[0][1] == -1) {
            moves.add('U');
            verticalCornerSwap();
        }
    }

    private void solveEdgesLL() {
        findEdgesLL();
        boolean orangeCorrect = false;
        boolean greenCorrect = false;
        boolean redCorrect = false;
        boolean blueCorrect = false;
        int numberEdgesWrong = 0;
        Cubie orangeYellow = lastLayerEdges[0];
        Cubie greenYellow = lastLayerEdges[0];
        Cubie redYellow = lastLayerEdges[0];
        Cubie blueYellow = lastLayerEdges[0];

        for (Cubie lastLayerEdge : lastLayerEdges) {
            if (lastLayerEdge.xColor == sketch.color(255, 150, 0)) {
                orangeYellow = lastLayerEdge;
                if (lastLayerEdge.faces[0].normal.x == 1) { // correct
                    orangeCorrect = true;
                } else {
                    numberEdgesWrong++;
                }
            } else if (lastLayerEdge.xMinColor == sketch.color(255, 0, 0)) {
                redYellow = lastLayerEdge;
                if (lastLayerEdge.faces[3].normal.x == -1) { // correct
                    redCorrect = true;
                } else {
                    numberEdgesWrong++;
                }
            } else if (lastLayerEdge.zColor == sketch.color(0, 255, 0)) {
                greenYellow = lastLayerEdge;
                if (lastLayerEdge.faces[2].normal.z == 1) { // correct
                    greenCorrect = true;
                } else {
                    numberEdgesWrong++;
                }
            } else if (lastLayerEdge.zMinColor == sketch.color(0, 0, 255)) {
                blueYellow = lastLayerEdge;
                if (lastLayerEdge.faces[5].normal.z == -1) { // correct
                    blueCorrect = true;
                } else {
                    numberEdgesWrong++;
                }
            }
        }
        if (numberEdgesWrong == 0) {
            cubeComplete = true;
        } else if (numberEdgesWrong == 4) {
            // two scenarios
            if (blueYellow.faces[5].normal.z == 1 && greenYellow.faces[2].normal.z == -1) {
                middleOppositeSwap();
            } else if (redYellow.z == 1) {            // two options for the diagonal swap
                middleDiagonalSwap();
            } else if (orangeYellow.z == 1) {
                moves.add('u');
                middleDiagonalSwap();
                moves.add('U');
            }
        } else if (numberEdgesWrong == 3) {
            if (orangeCorrect) {
                if (greenYellow.x == -1) { // anti clockwise
                    moves.add('U');
                    middleCounterClockwise();
                    moves.add('u');
                } else {
                    moves.add('U');
                    middleClockwise();
                    moves.add('u');
                }
            } else if (greenCorrect) {
                if (redYellow.z == -1) { // anti clockwise
                    moves.add('u');
                    moves.add('u');
                    middleCounterClockwise();
                    moves.add('u');
                    moves.add('u');
                } else {
                    moves.add('u');
                    moves.add('u');
                    middleClockwise();
                    moves.add('u');
                    moves.add('u');
                }

            } else if (redCorrect) {
                if (blueYellow.x == 1) { // means anti clockwise
                    moves.add('u');
                    middleCounterClockwise();
                    moves.add('U');
                } else { // must mean clockwise
                    moves.add('u');
                    middleClockwise();
                    moves.add('U');
                }
            } else if (blueCorrect) {
                if (orangeYellow.z == 1) {
                    middleCounterClockwise();
                } else {
                    middleClockwise();
                }
            }
        }
    }

    // OLL algorithms ====================================================================
    private void noCornerOppFacing() {
        moves.add('r');
        moves.add('u');
        moves.add('R');
        moves.add('u');
        moves.add('r');
        moves.add('U');
        moves.add('R');
        moves.add('u');
        moves.add('r');
        moves.add('u');
        moves.add('u');
        moves.add('R');
    }

    private void noCornerTwoSame() {
        moves.add('r');
        moves.add('u');
        moves.add('u');
        moves.add('r');
        moves.add('r');
        moves.add('U');
        moves.add('r');
        moves.add('r');
        moves.add('U');
        moves.add('r');
        moves.add('r');
        moves.add('u');
        moves.add('u');
        moves.add('r');
    }

    private void oneCornerRightHanded() {
        moves.add('r');
        moves.add('u');
        moves.add('R');
        moves.add('u');
        moves.add('r');
        moves.add('u');
        moves.add('u');
        moves.add('R');
    }

    private void oneCornerLeftHanded() {
        moves.add('L');
        moves.add('U');
        moves.add('l');
        moves.add('U');
        moves.add('L');
        moves.add('u');
        moves.add('u');
        moves.add('l');
    }

    private void twoCornerOppFacing() {
        moves.add('m');
        moves.add('r');
        moves.add('u');
        moves.add('R');
        moves.add('U');
        moves.add('R');
        moves.add('M');
        moves.add('f');
        moves.add('r');
        moves.add('F');
    }

    private void twoCornerSameFacing() {
        moves.add('r');
        moves.add('r');
        moves.add('d');
        moves.add('R');
        moves.add('u');
        moves.add('u');
        moves.add('r');
        moves.add('D');
        moves.add('R');
        moves.add('u');
        moves.add('u');
        moves.add('R');
    }

    private void twoCornerDiagonal() {
        moves.add('f');
        moves.add('R');
        moves.add('F');
        moves.add('r');
        moves.add('m');
        moves.add('u');
        moves.add('r');
        moves.add('U');
        moves.add('R');
        moves.add('M');
    }

    // pll algorithms =======================================================

    // corners
    private void diagonalCornerSwap() {
        System.out.println("diagonalCornerSwap");
        moves.add('f');
        moves.add('r');
        moves.add('U');
        moves.add('R');
        moves.add('U');
        moves.add('r');
        moves.add('u');
        moves.add('R');
        moves.add('F');
        moves.add('r');
        moves.add('u');
        moves.add('R');
        moves.add('U');
        moves.add('R');
        moves.add('f');
        moves.add('r');
        moves.add('F');
    }

    private void verticalCornerSwap() {
        System.out.println("vertical corner swap");
        moves.add('r');
        moves.add('u');
        moves.add('R');
        moves.add('U');
        moves.add('R');
        moves.add('f');
        moves.add('r');
        moves.add('r');
        moves.add('U');
        moves.add('R');
        moves.add('U');
        moves.add('r');
        moves.add('u');
        moves.add('R');
        moves.add('F');
    }

    // middles ===========================================================================
    private void middleOppositeSwap() {
        System.out.println("middle opposite swap");
        moves.add('m');
        moves.add('m');
        moves.add('u');
        moves.add('m');
        moves.add('m');
        moves.add('u');
        moves.add('u');
        moves.add('m');
        moves.add('m');
        moves.add('u');
        moves.add('m');
        moves.add('m');
    }

    private void middleCounterClockwise() {
        System.out.println("middle anti clockwise");
        moves.add('r');
        moves.add('U');
        moves.add('r');
        moves.add('u');
        moves.add('r');
        moves.add('u');
        moves.add('r');
        moves.add('U');
        moves.add('R');
        moves.add('U');
        moves.add('r');
        moves.add('r');
    }

    private void middleClockwise() {
        System.out.println("middle clockwise");
        moves.add('r');
        moves.add('r');
        moves.add('u');
        moves.add('r');
        moves.add('u');
        moves.add('R');
        moves.add('U');
        moves.add('R');
        moves.add('U');
        moves.add('R');
        moves.add('u');
        moves.add('R');
    }

    private void middleDiagonalSwap() {
        System.out.println("middle diagonal swap");
        moves.add('M');
        moves.add('u');
        moves.add('m');
        moves.add('m');
        moves.add('u');
        moves.add('m');
        moves.add('m');
        moves.add('u');
        moves.add('M');
        moves.add('u');
        moves.add('u');
        moves.add('m');
        moves.add('m');
        moves.add('U');
    }

    // all the moves =====================================================================
    private void keyPress(char key) {
        switch (key) {
            case 'b':
                RubikCube.turnB(-1);
                break;
            case 'B':
                RubikCube.turnB(1);
                break;
            case 'f':
                RubikCube.turnF(1);
                break;
            case 'F':
                RubikCube.turnF(-1);
                break;
            case 'u':
                RubikCube.turnU(1);
                break;
            case 'U':
                RubikCube.turnU(-1);
                break;
            case 'd':
                RubikCube.turnD(1);
                break;
            case 'D':
                RubikCube.turnD(-1);
                break;
            case 'l':
                RubikCube.turnL(1);
                break;
            case 'L':
                RubikCube.turnL(-1);
                break;
            case 'r':
                RubikCube.turnR(1);
                break;
            case 'R':
                RubikCube.turnR(-1);
                break;
            case 'm':
                RubikCube.turnM(1);
                break;

            case 'M':
                RubikCube.turnM(-1);
                break;
            case 's':
                RubikCube.turnS(1);
                break;
            case 'S':
                RubikCube.turnS(-1);
                break;
            case 'e':
                RubikCube.turnE(1);
                break;
            case 'E':
                RubikCube.turnE(-1);
                break;
        }
    }

    public void reset() {
        whiteRedComplete = false;
        whiteBlueComplete = false;
        whiteGreenComplete = false;
        whiteOrangeComplete = false;
        whiteCrossComplete = false;
        whiteCornersComplete = false;

        redBlueWhiteCornerComplete = false;
        redGreenWhiteCornerComplete = false;
        orangeBlueWhiteCornerComplete = false;
        greenOrangeWhiteCornerComplete = false;

        redGreenComplete = false;
        redBlueComplete = false;
        blueOrangeComplete = false;
        greenOrangeComplete = false;

        yellowCrossComplete = false;
        orientateLLComplete = false;

        yellowCornersComplete = false;

        findMidLayer();
        for (Cubie cubie : midLayer) {
            cubie.setIsSolved(false);
        }
    }
}