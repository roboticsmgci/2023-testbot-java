package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DriveDistance;
import frc.robot.commands.Turn;
import frc.robot.subsystems.Drivetrain;

public class AutoMoveOnly extends SequentialCommandGroup {
    private Drivetrain m_drivetrain;

    public AutoMoveOnly(Drivetrain drivetrain) {
        m_drivetrain = drivetrain;

        setName("AutoMoveOnly");
        addCommands(new DriveDistance(3, 0.4, drivetrain));
        
        // addCommands(new Turn(90, drivetrain));
    }
}
