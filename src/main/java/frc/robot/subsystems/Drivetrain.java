package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
    
    private static final int leftLeadDeviceID = 2, leftFollowDeviceID = 4, rightLeadDeviceID = 3, rightFollowDeviceID = 1;
    private CANSparkMax m_leftLeadMotor = new CANSparkMax(leftLeadDeviceID, CANSparkMax.MotorType.kBrushed);
    private CANSparkMax m_rightLeadMotor = new CANSparkMax(rightLeadDeviceID, CANSparkMax.MotorType.kBrushed);
    private CANSparkMax m_leftFollowMotor = new CANSparkMax(leftFollowDeviceID, CANSparkMax.MotorType.kBrushed);
    private CANSparkMax m_rightFollowMotor = new CANSparkMax(rightFollowDeviceID, CANSparkMax.MotorType.kBrushed);

    private DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftLeadMotor, m_rightLeadMotor);

    // Gyro
    public AHRS m_navX = new AHRS(SPI.Port.kMXP);

    public Drivetrain() {

        // Restores factory defaults, does not persist
        m_leftLeadMotor.restoreFactoryDefaults();
        m_rightLeadMotor.restoreFactoryDefaults();
        m_leftFollowMotor.restoreFactoryDefaults();
        m_rightFollowMotor.restoreFactoryDefaults();

        // Inverts one side of the drivetrain
        m_leftLeadMotor.setInverted(true);
        // m_leftFollowMotor.setInverted(true);

        // Configures the motors to follow each other
        m_leftFollowMotor.follow(m_leftLeadMotor);
        m_rightFollowMotor.follow(m_rightLeadMotor);

        setName("Drivetrain");

    }

    public void drive(double left, double right) {

    }

    public void log() {

    }

    @Override
    public void periodic() {

    }

}
