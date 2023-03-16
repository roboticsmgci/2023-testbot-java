package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    CANSparkMax m_motor = new CANSparkMax(6, MotorType.kBrushless); // port may not be correct
    RelativeEncoder m_encoder = m_motor.getEncoder();

    public Intake() {
        m_motor.setInverted(false); // may need to change
        m_motor.setIdleMode(IdleMode.kBrake);
        m_encoder.setPosition(0);

        setName("Intake");
    }

    /**
     * Set the arm output power.
     * 
     * @param percent desired speed
     * @param amps current limit
     */
    public void setMotor(double percent, int amps) {
        m_motor.set(percent);
        m_motor.setSmartCurrentLimit(amps);
        // SmartDashboard.putNumber("intake power (%)", percent);
        // SmartDashboard.putNumber("intake motor current (amps)", m_motor.getOutputCurrent());
        // SmartDashboard.putNumber("intake motor temperature (C)", m_motor.getMotorTemperature());
    }

}
