package cleancode.studycafe.me;

import cleancode.studycafe.me.config.MachineConfig;
import cleancode.studycafe.me.exception.StudyCafeException;
import cleancode.studycafe.me.io.InputHandler;
import cleancode.studycafe.me.io.OutputHandler;
import cleancode.studycafe.me.io.StudyCafeFileHandler;
import cleancode.studycafe.me.model.*;

import java.util.List;
import java.util.Optional;

public class StudyCafePassMachine {

    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;
    private final StudyCafePasses passes;
    private final StudyCafeLockerPasses lockerPasses;

    public StudyCafePassMachine(MachineConfig machineConfig) {
        this.inputHandler = machineConfig.getInputHandler();
        this.outputHandler = machineConfig.getOutputHandler();

        StudyCafeFileHandler studyCafeFileHandler = machineConfig.getStudyCafeFileHandler();
        this.passes = StudyCafePasses.from(studyCafeFileHandler);
        this.lockerPasses = StudyCafeLockerPasses.from(studyCafeFileHandler);
    }

    public void run() {
        try {
            outputHandler.showWelcomeMessage();
            outputHandler.showAnnouncement();

            StudyCafePass selectedPass = getPassFromUser();
            StudyCafeLockerPass selectedLockerPass = getLockerPassFromUser(selectedPass).orElseGet(null);

            PassOrder passOrder = PassOrder.of(selectedPass, selectedLockerPass);
            outputHandler.showPassOrderSummary(passOrder);

        } catch (StudyCafeException e) {
            outputHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }

    private Optional<StudyCafeLockerPass> getLockerPassFromUser(StudyCafePass selectedPass) {
        if (doesUserSelectFixed(selectedPass)) {
            StudyCafeLockerPass lockerPass = lockerPasses.getPassBy(selectedPass);

            if (doesUserUseLocker(lockerPass)) {
                return Optional.of(lockerPass);
            }
        }
        return Optional.empty();
    }

    private StudyCafePass getPassFromUser() {
        StudyCafePassType selectedPassType = getPassTypeFromUser();
        List<StudyCafePass> passListByType = passes.findPassesBy(selectedPassType);
        outputHandler.showPassListForSelection(passListByType);
        return inputHandler.getSelectPass(passListByType);
    }

    private boolean doesUserUseLocker(StudyCafeLockerPass lockerPass) {
        outputHandler.askLockerPass(lockerPass);
        return inputHandler.getLockerSelection();
    }

    private static boolean doesUserSelectFixed(StudyCafePass pass) {
        return pass.isSameType(StudyCafePassType.FIXED);
    }

    private StudyCafePassType getPassTypeFromUser() {
        outputHandler.askPassTypeSelection();
        return inputHandler.getPassTypeSelectingUserAction();
    }
}
