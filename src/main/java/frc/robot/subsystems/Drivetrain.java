package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class Drivetrain extends SubsystemBase {
    public double angle = 0;

    private final CANSparkMax m_leftLeadMotor = new CANSparkMax(DriveConstants.kLeftLeadDeviceID,
                                                                MotorType.kBrushless);
    private final CANSparkMax m_rightLeadMotor = new CANSparkMax(DriveConstants.kRightLeadDeviceID,
                                                                 MotorType.kBrushless);
    private final CANSparkMax m_leftFollowMotor = new CANSparkMax(DriveConstants.kLeftFollowDeviceID,
                                                                  MotorType.kBrushless);
    private final CANSparkMax m_rightFollowMotor = new CANSparkMax(DriveConstants.kRightFollowDeviceID,
                                                                   MotorType.kBrushless);

    private final CANSparkMax m_encoderMotor = new CANSparkMax(6, MotorType.kBrushless);

    private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftLeadMotor, m_rightLeadMotor);

    private double m_pitchError;
    // Gyro
    public AHRS m_navX = new AHRS(SerialPort.Port.kUSB1);

    // Encoders
    // public RelativeEncoder m_leftLeadEncoder = m_leftLeadMotor.getEncoder();
    // public RelativeEncoder m_rightLeadEncoder = m_rightLeadMotor.getEncoder();
    public RelativeEncoder m_Encoder = m_encoderMotor.getEncoder();

    private SlewRateLimiter m_leftLimiter = new SlewRateLimiter(1);
    private SlewRateLimiter m_rightLimiter = new SlewRateLimiter(1);

    private SlewRateLimiter m_leftBrakeLimiter = new SlewRateLimiter(1.5);
    private SlewRateLimiter m_rightBrakeLimiter = new SlewRateLimiter(1.5);

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

        // Set conversion ratios
        // m_leftLeadEncoder.setPositionConversionFactor(0.0443);
        // m_rightLeadEncoder.setPositionConversionFactor(0.0443);
        m_Encoder.setPositionConversionFactor(0.02);

        m_pitchError = m_navX.getRoll();

        setName("Drivetrain");
    }

    /**
     * Drives the robot with the given speed for each side.
     * 
     * @param left The speed of the left side.
     * @param right The speed of the right speed.
     */
    public void drive(double left, double right) {
        m_robotDrive.tankDrive(left, right, false);
        m_encoderMotor.set((left+right)/2);
    }

    public void drive(double left, double right, boolean limit) {
        if(limit){
            left = m_leftLimiter.calculate(left);
            right = m_rightLimiter.calculate(right);
        }else{
            left = m_leftBrakeLimiter.calculate(left);
            right = m_rightBrakeLimiter.calculate(right);
        }
        m_robotDrive.tankDrive(left, right, false);
        m_encoderMotor.set((left+right)/2);
    }

    /**
     * Logs information to the SmartDashboard.
     */
    public void log() {
        SmartDashboard.putNumber("Gyro", m_navX.getYaw());
        SmartDashboard.putNumber("Angle", m_navX.getAngle());
        SmartDashboard.putNumber("Pitch", getPitch());
        SmartDashboard.putNumber("target", angle);
        SmartDashboard.putNumber("encoder", m_Encoder.getPosition());
    }

    @Override
    public void periodic() {
        log();
    }

    public double getPitch(){
        if (m_pitchError == 0) {
            m_pitchError = m_navX.getPitch();
        }
        return -(m_navX.getPitch()-m_pitchError);
    }

    public void resetGyro(){
        m_navX.reset();
        m_pitchError = m_navX.getRoll();
    }
}
