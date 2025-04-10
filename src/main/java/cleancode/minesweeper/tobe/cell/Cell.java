package cleancode.minesweeper.tobe.cell;

/**
 * BOARD, NEARBY_LAND_MINE_COUNTS, LAND_MINES
 * 모두 셀에 대한 어떤 특성(문양, 주변 셀 개수, 지뢰인지 아닌지)을 가지고 있음.
 * Cell 객체를 생성하자.
 */

public abstract class Cell {

    protected static final String FLAG_SIGN = "⚑";
    protected static final String UNCHECKED_SIGN = "□";

    protected boolean isFlagged;
    protected boolean isOpened;

    public abstract boolean isLandMine();

    public abstract boolean hasLandMineCount();

    //처음부터 getter,setter를 만들지 않는다.
    public abstract String getSign();

    public void flag() {
        this.isFlagged=true;
    }

    public void open() {
        this.isOpened=true;
    }

    //사용자가 셀을 체크했는지
    public boolean isChecked() {
        return isFlagged || isOpened; //깃발을 꽂았거나, 열었거나
    }

    public boolean isOpened() {
        return isOpened;
    }
}
