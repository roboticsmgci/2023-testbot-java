package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class TurnPID extends CommandBase {
    private final double m_degrees;
    private final Drivetrain m_drivetrain;
    private final PIDController pid;

    private double initialAngle;
    private double targetAngle;

    private final double kp = 0.1;
    private final double ki = 0.02;
    private final double kd = 0.2;
    private final double error = 2;
    private final double errorD = 5;

    public TurnPID(double degrees, Drivetrain drivetrain) {
        m_drivetrain = drivetrain;
        m_degrees = degrees;

        pid = new PIDController(kp, ki, kd);
        pid.setTolerance(error, errorD);
        
        setName("Turn PID");
        addRequirements(m_drivetrain);
    }

    @Override
    public void initialize() {
        initialAngle = m_drivetrain.m_navX.getAngle();
        targetAngle = initialAngle + m_degrees;

        pid.setSetpoint(targetAngle);
    }

    @Override
    public void execute() {
        double speed = pid.calculate(m_drivetrain.m_navX.getAngle());
        m_drivetrain.drive(speed, -speed);
    }

    @Override
    public boolean isFinished() {
        return pid.atSetpoint();
    }
  
    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        m_drivetrain.drive(0, 0);
    }

}
