package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.subsystems.Drivetrain;

public class Drive2WJ extends CommandBase {
    private Joystick m_xbox;
    //private double distance;
    private double kP = 0.008, kD = 0.001;
    
    private PDController pd = new PDController(kP, kD);

    private Drivetrain m_drivetrain;

    public Drive2WJ(Joystick xbox, Drivetrain drivetrain) {
        m_xbox = xbox;
        m_drivetrain = drivetrain;

        setName("Drive2WJ");
        addRequirements(m_drivetrain);
    }

    @Override
    public void initialize(){
    }

    @Override
    public void execute() {
        if(m_xbox.getRawButton(5)&&m_xbox.getRawButton(6)){
            m_drivetrain.m_navX.reset();
        }

        double speed = 0.7*Math.max(Math.abs(m_xbox.getRawAxis(0)), Math.abs(m_xbox.getRawAxis(1)));//Math.min(1, Math.hypot(m_xbox.getRawAxis(0), m_xbox.getRawAxis(1)));//filter.calculate(0.9*Math.min(1, m_xbox.getMagnitude()));//Math.hypot(x, y)

        boolean brake = m_xbox.getRawButton(3);

        // Angle between the y-axis and the direction the stick is pointed
        double angle = Math.toDegrees(Math.atan2(m_xbox.getRawAxis(0), -m_xbox.getRawAxis(1)));//Math.atan(x, -y)

        m_drivetrain.angle = angle;

        double correction = 0;

        double heading = m_drivetrain.m_navX.getAngle()%360;

        if(!m_xbox.getRawButton(1) && Math.abs(heading-angle)>90){
            speed*=-1;
            
            if(heading<0) {
                heading += 180;
            }else{
                heading-=180;
            }
        }
            
        correction = pd.calculate(angle, heading);

        if(m_xbox.getPOV()==0){
            m_drivetrain.drive(0.2, 0.2);
        } else if(m_xbox.getPOV()==180){
            m_drivetrain.drive(-0.2, -0.2);
        }
        else if(m_xbox.getPOV()==90){
            m_drivetrain.drive(0.3, -0.3);
        }
        else if(m_xbox.getPOV()==270){
            m_drivetrain.drive(-0.3, 0.3);
        }
        else if(Math.abs(correction)>0.55){
            speed*=Math.signum(correction);
            m_drivetrain.drive2(0, speed, !brake);
        }else if(brake){
            m_drivetrain.drive2(0, correction+0.1*Math.signum(correction), false);
        }else{
            m_drivetrain.drive2(speed, speed*correction, true);
        }
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