package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class DriveForward extends CommandBase {
    private final Drivetrain m_drivetrain;
    private int durationCounter;

    public DriveForward(Drivetrain drivetrain) {
        m_drivetrain = drivetrain;
        setName("MoveBack");
        addRequirements(m_drivetrain);
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        durationCounter = 0;
        // Get everything in a safe starting state.
        m_drivetrain.drive(0, 0);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        if (durationCounter < 150){
            m_drivetrain.drive(0.5, 0.5);
            durationCounter++;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        if(durationCounter >= 150){
            return true;
        }else{
        return false;
        }
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        m_drivetrain.drive(0, 0);
    }
}