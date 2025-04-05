package cleancode.minesweeper.tobe;

/**
 * BOARD, NEARBY_LAND_MINE_COUNTS, LAND_MINES
 * 모두 셀에 대한 어떤 특성(문양, 주변 셀 개수, 지뢰인지 아닌지)을 가지고 있음.
 * Cell 객체를 생성하자.
 */

public class Cell {

    private static final String FLAG_SIGN = "⚑";
    private static final String LAND_MINE_SIGN = "☼";
    private static final String UNCHECKED_SIGN = "□";
    private static final String EMPTY_SIGN = "■"; //열었더니 빈

    private final String sign; //sign은 일단 한 번 만들어지면 안바뀐다고 가정. 불변 데이터는 변하지 않는 데이터라고 최대한 final로 명시해주 것이 좋음.
    private int nearbyLandMineCounts;
    private boolean isLandMine;
    private boolean isFlagged;
    private boolean isOpened;

    private Cell(String sign, int nearbyLandMineCounts, boolean isLandMine, boolean isFlagged, boolean isOpened) {
        this.sign=sign;
        this.nearbyLandMineCounts=nearbyLandMineCounts;
        this.isLandMine=isLandMine;
        this.isFlagged=isFlagged;
        this.isOpened=isOpened;
    }

    //정적 팩터리 메서드를 통해 이름을 줄 수 있음.
    public static Cell of(String sign, int nearbyLandMineCounts, boolean isLandMine, boolean isFlagged, boolean isOpened) {
        return new Cell(sign, nearbyLandMineCounts, isLandMine, isFlagged, isOpened);
    }

    //맨 처음 보드 초기화 시 생성할 빈 셀
    public static Cell create() {
        return of("", 0, false,false, false);
    }

    public void turnOnLandMine() {
        this.isLandMine=true;
    }

    public void updateNearbyLandMineCount(int count) {
        this.nearbyLandMineCounts=count;
    }

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

    public boolean isLandMine() {
        return isLandMine;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public boolean hasLandMineCount() {
        return nearbyLandMineCounts!=0;
    }

    //처음부터 getter,setter를 만들지 않는다.
    public String getSign() {
        //열린 경우
        if(isOpened){
            if (isLandMine) {
                return LAND_MINE_SIGN;
            }
            if (hasLandMineCount()) {
                return String.valueOf(nearbyLandMineCounts);
            }
            return EMPTY_SIGN;
        }

        //닫힌 경우
        if (isFlagged) {
            return FLAG_SIGN;
        }

        return UNCHECKED_SIGN;
    }
}
