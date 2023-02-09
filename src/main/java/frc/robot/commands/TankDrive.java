package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Speed;
import frc.robot.subsystems.Drivetrain;

public class TankDrive extends CommandBase {
    private Speed m_left;
    private Speed m_right;
    private Drivetrain m_drivetrain;

    public TankDrive(Speed left, Speed right, Drivetrain drivetrain) {
        m_left = left;
        m_right = right;
        m_drivetrain = drivetrain;

        setName("TankDrive");
        addRequirements(m_drivetrain);
    }

    @Override
    public void execute() {
        m_drivetrain.drive(m_left.speed(), m_right.speed());
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        m_drivetrain.drive(0, 0);
    }
}
