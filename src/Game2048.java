import com.cs.engine.cell.Color;
import com.cs.engine.cell.Game;
import com.cs.engine.cell.Key;

public class Game2048 extends Game {

    private static final int SIDE = 4;
    private int[][] gameField;
    private int score;
    boolean isGameStopped;

    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
//        gameField[0][0]=2;
//        gameField[0][1]=4;
//        gameField[0][2]=8;
//        gameField[0][3]=16;
//        gameField[1][0]=32;
//        gameField[1][1]=64;
//        gameField[1][2]=128;
//        gameField[1][3]=256;
//        gameField[3][2]=512;
        drawScene();
    }

    private void drawScene() {
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                //setCellColor(x,y, Color.MINTCREAM);
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }

    private void setCellColoredNumber(int x, int y, int value) {
        if (value == 0) setCellValueEx(x, y, Color.MINTCREAM, "");
        else setCellValueEx(x, y, getColorByValue(value), "" + value);
    }

    private Color getColorByValue(int value) {
        Color col = switch (value) {
            case 2 -> Color.LIME;
            case 4 -> Color.YELLOW;
            case 8 -> Color.GREEN;
            case 16 -> Color.MAROON;
            case 32 -> Color.BLUE;
            case 64 -> Color.LIGHTBLUE;
            case 128 -> Color.LIGHTGREEN;
            case 256 -> Color.ORANGE;
            case 512 -> Color.PINK;
            case 1024 -> Color.OLIVE;
            case 2048 -> Color.SILVER;
            default -> Color.MINTCREAM;
        };
        return col;
    }

    private void createGame() {
        boolean isGameStopped=false;
        score = 0;
        gameField = new int[SIDE][SIDE];
//        gameField[0][0]= 4;
//        gameField[0][1]= 4;
//        gameField[0][3]= 2;
        createNewNumber();
        createNewNumber();
    }

    private void createNewNumber() {
        if (getMaxValue()==2048) win();
        int x;
        int y;
        do {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
        } while (gameField[y][x] != 0);
        if (getRandomNumber(10) == 0)
            gameField[y][x] = 4;
        else
            gameField[y][x] = 2;

    }

    private int getMaxValue() {
        int max=0;
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                if (gameField[y][x]>max) max = gameField[y][x];
            }
        }
            return 0;
    }

    @Override
    public void onKeyPress(Key key) {
        if (!isGameStopped){
        if (canUserMove()) {
            switch (key) {
                case LEFT:
                    moveLeft();
                    drawScene();
                    break;
                case RIGHT:
                    moveRight();
                    drawScene();
                    break;
                case UP:
                    moveUp();
                    drawScene();
                    break;
                case DOWN:
                    moveDown();
                    drawScene();
                    break;
            }
        } else {
            gameOver();
        }}else{
            if (key.equals(Key.SPACE)){
                createGame();
                drawScene();
            }
        }
    }

    private boolean canUserMove() {
        boolean result = false;
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                if (gameField[y][x] == 0) result = true;
            }
        }
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE - 1; x++) {
                if (gameField[y][x] == gameField[y][x + 1]) {
                    result = true;
                }
            }
        }
        for (int y = 0; y < SIDE-1; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (gameField[y][x] == gameField[y+1][x]) {
                    result = true;
                }
            }
        }
        return result;
    }

    private void moveLeft() {
        boolean move = false;
        for (int i = 0; i < SIDE; i++) {
            boolean c1 = compressRow(gameField[i]);
            boolean m = mergeRow(gameField[i]);
            boolean c2 = compressRow(gameField[i]);
            if (c1 || m || c2) move = true;
        }

        if (move) createNewNumber();
    }

    private boolean compressRow(int[] row) {
        boolean result = false;
        for (int i = 1; i < SIDE; i++) {
            if (row[i] != 0 && row[0] == 0) {
                result = true;
                row[0] = row[i];
                row[i] = 0;
            }
        }

        for (int i = 2; i < SIDE; i++) {
            if (row[i] != 0 && row[1] == 0) {
                result = true;
                row[1] = row[i];
                row[i] = 0;
            }
        }

        for (int i = 3; i < SIDE; i++) {
            if (row[i] != 0 && row[2] == 0) {
                result = true;
                row[2] = row[i];
                row[i] = 0;
            }
        }
        //TODO   0200,  0040, 0002
        /*
        0202  2200 t
        2222  2222 f
        0402  4200 t
        4202  4220 t
        2400  2400 f
         */
        return result;
    }

    private boolean mergeRow(int[] row) {
        boolean result = false;
        for (int i = 0; i < SIDE - 1; i++) {
            if (row[i] == row[i + 1] && row[i] != 0) {
                row[i] = row[i] + row[i + 1];
                row[i + 1] = 0;
                score += row[i];
                setScore(score);
                result = true;
            }
        }
        return result;
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();

    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();

    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {
        int[][] temp = new int[SIDE][SIDE];
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                temp[y][x] = gameField[y][x];
            }
        }
        gameField[0][3] = temp[0][0];
        gameField[1][3] = temp[0][1];
        gameField[2][3] = temp[0][2];
        gameField[3][3] = temp[0][3];

        gameField[0][2] = temp[1][0];
        gameField[1][2] = temp[1][1];
        gameField[2][2] = temp[1][2];
        gameField[3][2] = temp[1][3];

        gameField[0][1] = temp[2][0];
        gameField[1][1] = temp[2][1];
        gameField[2][1] = temp[2][2];
        gameField[3][1] = temp[2][3];

        gameField[0][0] = temp[3][0];
        gameField[1][0] = temp[3][1];
        gameField[2][0] = temp[3][2];
        gameField[3][0] = temp[3][3];
        //TODO Завершить поворот
    }

    private void gameOver() {
        isGameStopped=true;

        showMessageDialog(Color.RED, "Game Over", Color.YELLOW, 60);
    }

    private void win() {
        isGameStopped=true;

        showMessageDialog(Color.GREEN, "WIN", Color.YELLOW, 60);


    }
}
