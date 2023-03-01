package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class Turn extends CommandBase {
    private double m_degrees;
    private Drivetrain m_drivetrain;

    private final double allowedError = 0.5;

    private boolean isFinished;

    public Turn(double degrees, Drivetrain drivetrain) {
        m_drivetrain = drivetrain;
        m_degrees = m_drivetrain.m_navX.getAngle() + degrees;

        isFinished = false;
        
        addRequirements(m_drivetrain);
    }

    @Override
    public void execute() {
        double currentDegrees = m_drivetrain.m_navX.getAngle();

        if (Math.abs(currentDegrees - m_degrees) < allowedError) {
            isFinished = true;
        } else if (currentDegrees < m_degrees) {
            m_drivetrain.drive(0.2, -0.2);
        } else {
            m_drivetrain.drive(-0.2, 0.2);
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
  
    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        m_drivetrain.drive(0, 0);
    }

}
