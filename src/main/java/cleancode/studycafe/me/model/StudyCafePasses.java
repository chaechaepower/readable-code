package cleancode.studycafe.me.model;

import cleancode.studycafe.me.io.StudyCafeFileHandler;

import java.util.List;

public class StudyCafePasses {
    private final List<StudyCafePass> passes;

    private StudyCafePasses(List<StudyCafePass> passes) {
        this.passes = passes;
    }

    public static StudyCafePasses of(List<StudyCafePass> passes) {
        return new StudyCafePasses(passes);
    }

    public static StudyCafePasses from(StudyCafeFileHandler studyCafeFileHandler) {
        List<StudyCafePass> studyCafePasses = studyCafeFileHandler.readStudyCafePasses();
        return of(studyCafePasses);
    }

    public List<StudyCafePass> findPassesBy(StudyCafePassType type) {
        return passes.stream()
                .filter(pass -> pass.getPassType()==type)
                .toList();
    }
}
