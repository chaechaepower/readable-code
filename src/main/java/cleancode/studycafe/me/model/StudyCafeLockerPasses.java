package cleancode.studycafe.me.model;

import cleancode.studycafe.me.io.StudyCafeFileHandler;

import java.util.List;

public class StudyCafeLockerPasses {
    private final List<StudyCafeLockerPass> lockerPasses;

    private StudyCafeLockerPasses(List<StudyCafeLockerPass> lockerPasses) {
        this.lockerPasses = lockerPasses;
    }

    public static StudyCafeLockerPasses of(List<StudyCafeLockerPass> lockerPasses) {
        return new StudyCafeLockerPasses(lockerPasses);
    }

    public static StudyCafeLockerPasses from(StudyCafeFileHandler studyCafeFileHandler) {
        List<StudyCafeLockerPass> lockerPasses = studyCafeFileHandler.readLockerPasses();
        return of(lockerPasses);
    }

    public StudyCafeLockerPass getPassBy(StudyCafePass pass) {
        return lockerPasses.stream()
            .filter(option -> option.getPassType() == pass.getPassType()
                && option.getDuration() == pass.getDuration()
            )
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("해당 이용권은 사물함을 사용할 수 없습니다."));
    }
}
