package cleancode.minesweeper.tobe.minesweeper.board.cell;

public class CellState {

    public boolean isFlagged;
    public boolean isOpened;

    private CellState(boolean isFlagged, boolean isOpened) {
        this.isFlagged=isFlagged;
        this.isOpened=isOpened;
    }

    //셀 생성 시 초기 상태
    public static CellState initialize() {
        return new CellState(false,false);
    }

    public void flag() {
        this.isFlagged=true;
    }

    public void open() {
        this.isOpened=true;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public boolean isFlagged() {
        return isFlagged;
    }
}
