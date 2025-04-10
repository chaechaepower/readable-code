package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.cell.*;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;

import java.util.Arrays;
import java.util.Random;

public class GameBoard {

    public final int landMineCount;
    private final Cell[][] board; //점진적 리팩터링

    public GameBoard(GameLevel gameLevel) {
        int rowSize = gameLevel.getRowSize();
        int colSize = gameLevel.getColSize();
        board = new Cell[rowSize][colSize];

        landMineCount = gameLevel.getLandMineCount();
    }

    public void flag(int rowIdx, int colIndex) {
        Cell cell = findCell(rowIdx, colIndex);
        cell.flag();
    }

    public void open(int rowIdx, int colIdx) {
        Cell cell = findCell(rowIdx, colIdx);
        cell.open();
    }

    public void openSurroundedCells(int row, int col) {
        if (row < 0 || row >= getRowSize() || col < 0 || col >= getColSize()) {
            return;
        }
        if (isOpenedCell(row, col)) {
            return;
        }
        if (doesCellHaveLandMineCount(row, col)) {
            return;
        }

        open(row, col);

        if (findCell(row, col).hasLandMineCount()) { // getter로 꺼내서 0이랑 비교하지말고 메서드로 물어보기
            return;
        }

        openSurroundedCells(row - 1, col - 1);
        openSurroundedCells(row - 1, col);
        openSurroundedCells(row - 1, col + 1);
        openSurroundedCells(row, col - 1);
        openSurroundedCells(row, col + 1);
        openSurroundedCells(row + 1, col - 1);
        openSurroundedCells(row + 1, col);
        openSurroundedCells(row + 1, col + 1);
    }

    private boolean doesCellHaveLandMineCount(int row, int col) {
        return isLandMineCell(row, col);
    }

    private boolean isOpenedCell(int row, int col) {
        return findCell(row, col).isOpened();
    }


    public boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        Cell cell = findCell(selectedRowIndex, selectedColIndex);
        return cell.isLandMine();
    }

    // 셀을 모두 열었는지 -> 셀을 모두 체크(엶/닫혔지만 깃발을 꽂음)했는지
    public boolean isAllCellChecked() {
        return Arrays.stream(board) //Stream<String[]>
                .flatMap(Arrays::stream) //Stream<String> 그냥 map이면 Stream<Stream<String>>
                .allMatch(Cell::isChecked);
    }

    public void initializeGame() {
        int rowSize = getRowSize();
        int colSize = getColSize();

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                board[row][col] = new EmptyCell(); // 기존의 닫힌 셀이 아닌, 빈 셀 보드에 할당
            }
        }

        for (int i = 0; i < landMineCount; i++) {
            int landMineCol = new Random().nextInt(colSize);
            int landMineRow = new Random().nextInt(rowSize);
            board[landMineRow][landMineCol]= new LandMineCell();
        }

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                if (doesCellHaveLandMineCount(row, col)) {
                    //이미 최초 셀 할당 시 주변 지뢰 개수 0으로 초기화했음.
                    continue; //early return처럼
                }
                int count = countNearbyLandMines(row, col);
                if (count == 0) {
                    continue;
                }
                board[row][col]= new NumberCell(count);
            }
        }
    }

    public String getSign(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        return cell.getSign();
    }

    private Cell findCell(int rowIndex, int colIndex) {
        return board[rowIndex][colIndex];
    }

    public int getRowSize() {
        return board.length;
    }

    public int getColSize() {
        return board[0].length;
    }

    private int countNearbyLandMines(int row, int col) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        int count = 0;

        if (row - 1 >= 0 && col - 1 >= 0 && doesCellHaveLandMineCount(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && doesCellHaveLandMineCount(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < colSize && doesCellHaveLandMineCount(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && doesCellHaveLandMineCount(row, col - 1)) {
            count++;
        }
        if (col + 1 < colSize && doesCellHaveLandMineCount(row, col + 1)) {
            count++;
        }
        if (row + 1 < rowSize && col - 1 >= 0 && doesCellHaveLandMineCount(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < rowSize && doesCellHaveLandMineCount(row + 1, col)) {
            count++;
        }
        if (row + 1 < rowSize && col + 1 < colSize && doesCellHaveLandMineCount(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

}
