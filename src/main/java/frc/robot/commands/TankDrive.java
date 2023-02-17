package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class TankDrive extends CommandBase {
    private final Supplier<Double> m_left;
    private final Supplier<Double> m_right;
    private final Drivetrain m_drivetrain;

    public TankDrive(Supplier<Double> left, Supplier<Double> right, Drivetrain drivetrain) {
        m_left = left;
        m_right = right;
        m_drivetrain = drivetrain;

        setName("TankDrive");
        addRequirements(m_drivetrain);
    }

    @Override
    public void execute() {
        m_drivetrain.drive(m_left.get(), m_right.get());
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
