package cleancode.minesweeper.tobe.cell;

public class EmptyCell extends Cell {

    private static final String EMPTY_SIGN = "■"; //열었더니 빈

    @Override
    public boolean isLandMine() {
        return false;
    }

    @Override
    public boolean hasLandMineCount() {
        return true;
    }

    @Override
    public String getSign() {
        if (isOpened) {
            return EMPTY_SIGN;
        }
        if (isFlagged) {
            return FLAG_SIGN;
        }
        return UNCHECKED_SIGN;
    }
}
