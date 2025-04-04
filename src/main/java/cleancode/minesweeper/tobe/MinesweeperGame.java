package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    public static final Scanner SCANNER = new Scanner(System.in);
    private static final int BOARD_ROW_SIZE=8;
    private static final int BOARD_COL_SIZE=10;
    private static final Cell[][] BOARD = new Cell[BOARD_ROW_SIZE][BOARD_COL_SIZE]; //점진적 리팩터링
    public static final int LAND_MINE_COUNT = 10;

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        initializeGame();

        try {
            while (true) {
                showBoard();

                if (doesUserWinTheGame()) {
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }
                if (doesUserLoseTheGame()) {
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }

                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInput();
                actOnCell(cellInput, userActionInput);
            }
        } catch (AppException e) { //의도적으로 프로그램 내에서 던진 예외 catch
            System.out.println(e.getMessage());
        } catch (Exception e) { //예상치 못한 예외
            System.out.println("프로그램에 문제가 생겼습니다.");
            e.printStackTrace(); //실무에서는 안티 패턴임. 대신 로그를 남겨서 조치를 취함.
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doseUserChooseToPlantFlag(userActionInput)) {
            BOARD[selectedRowIndex][selectedColIndex].flag(); //깃발을 꽂음.(셀 내부 상태 변경)
            checkIfGameIsOver();
            return;
        }

        if (doseUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) {
                BOARD[selectedRowIndex][selectedColIndex].open(); //셀을 연다.
                changeGameStatusToLose();
                return;
            }

            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }

        throw new AppException("잘못된 번호를 선택하셨습니다.");

    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return BOARD[selectedRowIndex][selectedColIndex].isLandMine();
    }

    private static boolean doseUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doseUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        return convertRowFrom(cellInputRow);
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        return convertColFrom(cellInputCol);
    }

    private static String getUserActionInput() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
    }

    private static boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private static boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private static void checkIfGameIsOver() {
        boolean isAllChecked = isAllCellChecked();
        if (isAllChecked) {
            changeGameStatusToWin();
        }
    }

    private static void changeGameStatusToWin() {
        gameStatus = 1;
    }

    // 셀을 모두 열었는지 -> 셀을 모두 체크(엶/닫혔지만 깃발을 꽂음)했는지
    private static boolean isAllCellChecked() {
        return Arrays.stream(BOARD) //Stream<String[]>
                .flatMap(Arrays::stream) //Stream<String> 그냥 map이면 Stream<Stream<String>>
                .allMatch(Cell::isChecked);
    }

    private static int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) - 1;
        if (rowIndex >= BOARD_ROW_SIZE) {
            throw new AppException("잘못된 입력입니다.");
        }
        return rowIndex;
    }

    private static int convertColFrom(char cellInputCol) {
        switch (cellInputCol) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                throw new AppException("잘못된 입력입니다."); //비정상적인 값(-1)을 던지지 말고, 예외를 던지자. 메시지도 담자.
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                System.out.print(BOARD[row][col].getSign() + " ");
            }
            
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                BOARD[row][col] = Cell.create(); // 기존의 닫힌 셀이 아닌, 빈 셀 보드에 할당
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            BOARD[row][col].turnOnLandMine(); //지뢰를 킴.
        }

        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                if (isLandMineCell(row, col)) {
                    //이미 최초 셀 할당 시 주변 지뢰 개수 0으로 초기화했음.
                    continue; //early return처럼
                }
                int count = countNearbyLandMines(row, col);
                BOARD[row][col].updateNearbyLandMineCount(count); // 지뢰 개수 저장
            }
        }
    }

    private static int countNearbyLandMines(int row, int col) {
        int count = 0;

        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMineCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < BOARD_COL_SIZE && isLandMineCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMineCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) {
            return;
        }
        if (BOARD[row][col].isOpened()) {
            return;
        }
        if (isLandMineCell(row, col)) {
            return;
        }

        BOARD[row][col].open();

        if (BOARD[row][col].hasLandMineCount()) { // getter로 꺼내서 0이랑 비교하지말고 메서드로 물어보기
            return;
        }

        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
