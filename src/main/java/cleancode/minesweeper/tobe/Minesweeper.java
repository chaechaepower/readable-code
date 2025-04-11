package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.config.GameConfig;
import cleancode.minesweeper.tobe.game.GameInitalizable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.user.UserAction;


public class Minesweeper implements GameInitalizable, GameRunnable {

    private final GameBoard gameBoard;
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;
    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public Minesweeper(GameConfig gameConfig) {
        gameBoard = new GameBoard(gameConfig.getGameLevel());
        this.inputHandler=gameConfig.getInputHandler();
        this.outputHandler=gameConfig.getOutputHandler();
    }

    @Override
    public void initialize() {
        gameBoard.initializeGame();
    }

    @Override
    public void run() {
        outputHandler.showGameStartComments();

        try {
            while (true) {
                outputHandler.showBoard(gameBoard);

                if (doesUserWinTheGame()) {
                    outputHandler.showGameWinningComment();
                    break;
                }
                if (doesUserLoseTheGame()) {
                    outputHandler.showGameLosingComment();
                    break;
                }

                CellPosition cellPosition = getCellInputFromUser(); // a1
                UserAction userActionInput = getUserActionInput(); // 1. 깃발 꽂기 2. 셀 오픈
                actOnCell(cellPosition, userActionInput);
            }
        } catch (GameException e) { //의도적으로 프로그램 내에서 던진 예외 catch
            outputHandler.showExceptionMessage(e);
        } catch (Exception e) { //예상치 못한 예외
            outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
        }
    }

    private void actOnCell(CellPosition cellPosition, UserAction userActionInput) {
         if (doseUserChooseToPlantFlag(userActionInput)) {
            gameBoard.flagAt(cellPosition); //전치사 포함한 메서드명으로 변경
            checkIfGameIsOver();
            return;
        }

        if (doseUserChooseToOpenCell(userActionInput)) {
            if (gameBoard.isLandMineCellAt(cellPosition)) {
                gameBoard.openAt(cellPosition); //셀을 연다.
                changeGameStatusToLose();
                return;
            }

            gameBoard.openSurroundedCells(cellPosition);
            checkIfGameIsOver();
            return;
        }

        throw new GameException("잘못된 번호를 선택하셨습니다.");
    }

    private void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private boolean doseUserChooseToOpenCell(UserAction userActionInput) {
        return userActionInput==UserAction.OPEN;
    }

    private boolean doseUserChooseToPlantFlag(UserAction userActionInput) {
        return userActionInput==UserAction.FLAG;
    }


    private UserAction getUserActionInput() {
        outputHandler.showCommentForUserAction();
        return inputHandler.getUserActionFromUser();
    }

    private CellPosition getCellInputFromUser() {
        outputHandler.showCommentForSelectingCell();
        CellPosition cellPosition = inputHandler.getCellPositionFromUser();
        if (gameBoard.isInvalidCellPosition(cellPosition)) {
            throw new GameException("잘못된 좌표를 선택하셨습니다.");
        }

        return cellPosition;
    }

    private boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private void checkIfGameIsOver() {
        if (gameBoard.isAllCellChecked()) {
            changeGameStatusToWin();
        }
    }

    private void changeGameStatusToWin() {
        gameStatus = 1;
    }

}
