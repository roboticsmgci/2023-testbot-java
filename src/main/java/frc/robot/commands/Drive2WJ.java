package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.subsystems.Drivetrain;

public class Drive2WJ extends CommandBase {
    private Joystick m_xbox;
    //private double distance;
    private double kP = 0.009;

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
            m_drivetrain.resetGyro();
        }

        double speed = 0.7*Math.max(Math.abs(m_xbox.getRawAxis(0)), Math.abs(m_xbox.getRawAxis(1)));//Math.min(1, Math.hypot(m_xbox.getRawAxis(0), m_xbox.getRawAxis(1)));//filter.calculate(0.9*Math.min(1, m_xbox.getMagnitude()));//Math.hypot(x, y)

        boolean brake = m_xbox.getRawButton(3);

        double angle = Math.toDegrees(Math.atan2(m_xbox.getRawAxis(0), -m_xbox.getRawAxis(1)));//Math.atan(x, -y)

        m_drivetrain.angle = angle;

        double turnError = getCorrection(angle, 1);

        if(m_xbox.getRawButton(1)){
            if(angle<0) {
                angle += 180;
            }else{
                angle-=180;
            }
            speed*=-1;
        }
       
        // double heading = (m_drivetrain.m_navX.getAngle() % 360.0);

        // double difference = (heading - angle)*Math.signum(speed);
        // if(difference>180){
        //     difference-=360;
        // }else if(difference<-180){
        //     difference+=360;
        // }

        // double error = kP*(difference);
        double error = getCorrection(angle, Math.signum(speed));

        if(Math.abs(speed)<0.05){
            speed = 0;
        }

        double l=0, r=0;

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
        else if(Math.abs(error)>0.55){
            speed*=Math.signum(error);
            l-=speed;
            r+=speed;
            m_drivetrain.drive(l, r, false);
        }else if(brake){
            l-=turnError;
            r+=turnError;
            m_drivetrain.drive(l, r, false);
        }else{
            l+=speed*(1-error);
            r+=speed*(1+error);
            m_drivetrain.drive(l, r, true);
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

    private double getCorrection(double target, double direction){
        double heading = (m_drivetrain.m_navX.getAngle() % 360.0);

        double difference = (heading - target)*direction;
        if(difference>180){
            difference-=360;
        }else if(difference<-180){
            difference+=360;
        }

        return kP*(difference);
    }
}