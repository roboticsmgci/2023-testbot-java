package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

public class Auto2 extends SequentialCommandGroup {
    private Drivetrain m_drivetrain;

    public Auto2(Drivetrain drivetrain) {
        m_drivetrain = drivetrain;

        setName("Auto2");
        addCommands(new DriveBack(drivetrain));
    }
}
