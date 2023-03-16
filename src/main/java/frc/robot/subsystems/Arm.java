package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ArmConstants;

public class Arm extends SubsystemBase {
    public double angle = 0;

    private final CANSparkMax m_motor = new CANSparkMax(5, MotorType.kBrushless);

    public RelativeEncoder m_encoder = m_motor.getEncoder();

    private SlewRateLimiter m_speedLimiter = new SlewRateLimiter(1);

    

    public Arm() {

        // Restores factory defaults, does not persist
        m_motor.restoreFactoryDefaults();

        m_motor.setInverted(false); // may need to change
        m_motor.setIdleMode(IdleMode.kBrake);
        m_motor.setSmartCurrentLimit(ArmConstants.CURRENT_LIMIT_A);

        m_encoder.setPosition(0);

        // Set conversion ratios
        //m_leftLeadEncoder.setPositionConversionFactor(0.0443);

        setName("Arm");
    }

    /**
     * Set the arm output power. Positive is out, negative is in.
     * 
     * @param speed The power (0-1)
     */
    public void setMotor(double speed) {
        m_motor.set(speed);
    }

    /**
     * Logs information to the SmartDashboard.
     */

    @Override
    public void periodic() {
        
    }
}
