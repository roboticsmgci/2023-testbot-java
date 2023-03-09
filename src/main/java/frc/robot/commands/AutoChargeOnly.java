package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

public class AutoChargeOnly extends SequentialCommandGroup {
    private Drivetrain m_drivetrain;

    public AutoChargeOnly(Drivetrain drivetrain) {
        m_drivetrain = drivetrain;

        setName("Auto Charge Only");
        addCommands(new DriveBack(drivetrain));
    }
}
