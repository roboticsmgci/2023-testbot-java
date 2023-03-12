package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class Turn extends CommandBase {
    private double m_degrees;
    private final Drivetrain m_drivetrain;

    private final double allowedError = 10;

    public Turn(double degrees, Drivetrain drivetrain) {
        m_drivetrain = drivetrain;
        m_degrees = m_drivetrain.m_navX.getAngle() + degrees;
        
        setName("Turn");
        addRequirements(m_drivetrain);
    }

    @Override
    public void execute() {
        double currentDegrees = m_drivetrain.m_navX.getAngle();

        if (currentDegrees < m_degrees) {
            m_drivetrain.drive(0.5, -0.5);
        } else {
            m_drivetrain.drive(-0.5, 0.5);
        }
    }

    @Override
    public boolean isFinished() {
        double currentDegrees = m_drivetrain.m_navX.getAngle();

        return Math.abs(currentDegrees - m_degrees) < allowedError;
    }
  
    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        m_drivetrain.drive(0, 0);
    }

}
