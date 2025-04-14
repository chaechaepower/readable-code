package cleancode.minesweeper.tobe.minesweeper.gamelevel;

public class Middle implements GameLevel {
    @Override
    public int getRowSize() {
        return 100;
    }

    @Override
    public int getColSize() {
        return 100;
    }

    @Override
    public int getLandMineCount() {
        return 5;
    }
}
