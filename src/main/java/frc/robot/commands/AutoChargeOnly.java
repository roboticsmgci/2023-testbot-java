package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

public class AutoChargeOnly extends SequentialCommandGroup {
    private Drivetrain m_drivetrain;

    public AutoChargeOnly(Drivetrain drivetrain) {
        m_drivetrain = drivetrain;

        setName("Auto Charge Only");
        addCommands(new Climb(drivetrain));
        //addCommands(new DriveDistance(0.5, 0.4, m_drivetrain));
        addCommands(new Balance(drivetrain));
        addCommands(new DriveTime(900, -0.4, drivetrain));
        addCommands(new Turn(90, m_drivetrain));
    }
}
