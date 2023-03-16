package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {
    public double angle = 0;

    private final CANSparkMax m_motor = new CANSparkMax(5, MotorType.kBrushless);

    public RelativeEncoder m_Encoder = m_motor.getEncoder();

    private SlewRateLimiter m_speedLimiter = new SlewRateLimiter(1);

    public Arm() {

        // Restores factory defaults, does not persist
        m_motor.restoreFactoryDefaults();

        m_Encoder.setPosition(0);

        // Set conversion ratios
        //m_leftLeadEncoder.setPositionConversionFactor(0.0443);

        setName("Drivetrain");
    }

    /**
     * Drives the robot with the given speed for each side.
     * 
     * @param left The speed of the left side.
     * @param right The speed of the right speed.
     */
    public void drive(double speed) {
        m_motor.set(speed);
    }

    /**
     * Logs information to the SmartDashboard.
     */

    @Override
    public void periodic() {
        
    }
}
