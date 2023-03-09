package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class Drive2 extends CommandBase {
    private Joystick m_joystick;
    //private double distance;
    private double kP = 0.007;
    private Drivetrain m_drivetrain;

    public Drive2(Joystick joystick, Drivetrain drivetrain) {
        m_joystick = joystick;
        m_drivetrain = drivetrain;

        setName("Drive2");
        addRequirements(m_drivetrain);
    }

    @Override
    public void initialize(){
        m_drivetrain.m_navX.reset();
    }

    @Override
    public void execute() {
        double speed = 0.8*(1-m_joystick.getThrottle())/2*m_joystick.getMagnitude();//Math.hypot(x, y)

        double angle = m_joystick.getDirectionDegrees();//Math.atan(x, -y)

        m_drivetrain.angle = angle;

        if(m_joystick.getRawButton(1)){
            if(angle<0) {
                angle += 180;
            }else{
                angle-=180;
            }
            speed*=-1;
        }
       
        double heading = (m_drivetrain.m_navX.getAngle() % 360.0);

        double difference = (heading - angle)*Math.signum(speed);
        if(difference>180){
            difference-=360;
        }else if(difference<-180){
            difference+=360;
        }

        double error = kP*(difference);

        if(Math.abs(speed)<0.08){
            speed = 0;
        }
        if(Math.abs(error)>0.5){
            speed*=Math.signum(error);
            m_drivetrain.drive(-speed, speed);
        }else{
            m_drivetrain.drive(speed*(1-error), speed*(1+error));
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