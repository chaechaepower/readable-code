package cleancode.studycafe.me;

import cleancode.studycafe.me.config.MachineConfig;
import cleancode.studycafe.me.io.InputHandler;
import cleancode.studycafe.me.io.OutputHandler;
import cleancode.studycafe.me.io.StudyCafeFileHandler;

public class StudyCafeApplication {

    public static void main(String[] args) {
        MachineConfig machineConfig=MachineConfig.of(
                new InputHandler(),
                new OutputHandler(),
                new StudyCafeFileHandler()
        );

        StudyCafePassMachine studyCafePassMachine = new StudyCafePassMachine(machineConfig);
        studyCafePassMachine.run();
    }

}
