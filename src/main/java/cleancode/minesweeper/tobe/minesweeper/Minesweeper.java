package cleancode.minesweeper.tobe.minesweeper;

import cleancode.minesweeper.tobe.minesweeper.board.GameBoard;
import cleancode.minesweeper.tobe.minesweeper.config.GameConfig;
import cleancode.minesweeper.tobe.minesweeper.exception.GameException;
import cleancode.minesweeper.tobe.game.GameInitalizable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.minesweeper.io.InputHandler;
import cleancode.minesweeper.tobe.minesweeper.io.OutputHandler;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.user.UserAction;


public class Minesweeper implements GameInitalizable, GameRunnable {

    private final GameBoard gameBoard;
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;

    public Minesweeper(GameConfig gameConfig) {
        gameBoard = new GameBoard(gameConfig.getGameLevel());
        this.inputHandler = gameConfig.getInputHandler();
        this.outputHandler = gameConfig.getOutputHandler();
    }

    @Override
    public void initialize() {
        gameBoard.initializeGame();
    }

    @Override
    public void run() {
        outputHandler.showGameStartComments();

        while (gameBoard.isInProgress()) {
            try {
                outputHandler.showBoard(gameBoard);

                CellPosition cellPosition = getCellInputFromUser(); // a1
                UserAction userActionInput = getUserActionInput(); // 1. 깃발 꽂기 2. 셀 오픈
                actOnCell(cellPosition, userActionInput);
            } catch (GameException e) { //의도적으로 프로그램 내에서 던진 예외 catch
                outputHandler.showExceptionMessage(e);
            } catch (Exception e) { //예상치 못한 예외
                outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
            }
        }

        outputHandler.showBoard(gameBoard);

        if (gameBoard.isWinStatus()) {
            outputHandler.showGameWinningComment();
        }
        if (gameBoard.isLoseStatus()) {
            outputHandler.showGameLosingComment();
        }
    }

    private CellPosition getCellInputFromUser() {
        outputHandler.showCommentForSelectingCell();
        CellPosition cellPosition = inputHandler.getCellPositionFromUser();
        if (gameBoard.isInvalidCellPosition(cellPosition)) {
            throw new GameException("잘못된 좌표를 선택하셨습니다.");
        }

        return cellPosition;
    }

    private UserAction getUserActionInput() {
        outputHandler.showCommentForUserAction();
        return inputHandler.getUserActionFromUser();
    }

    private void actOnCell(CellPosition cellPosition, UserAction userActionInput) {
        if (doseUserChooseToPlantFlag(userActionInput)) {
            gameBoard.flagAt(cellPosition); //전치사 포함한 메서드명으로 변경
            return;
        }

        if (doseUserChooseToOpenCell(userActionInput)) {
            gameBoard.openAt(cellPosition);
            return;
        }

        throw new GameException("잘못된 번호를 선택하셨습니다.");
    }

    private boolean doseUserChooseToPlantFlag(UserAction userActionInput) {
        return userActionInput == UserAction.FLAG;
    }

    private boolean doseUserChooseToOpenCell(UserAction userActionInput) {
        return userActionInput == UserAction.OPEN;
    }
}
