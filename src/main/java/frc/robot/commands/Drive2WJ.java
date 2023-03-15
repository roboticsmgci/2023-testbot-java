package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class Drive2WJ extends CommandBase {
    private Joystick m_xbox;
    //private double distance;
    private double kP = 0.007;

    private SlewRateLimiter filter = new SlewRateLimiter(1);
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
        /* Xbox controller axes:
         * 0: left x
         * 1: left y
         * 2: right x
         * 3: right y
         * Xbox buttons:
         * 1: X
         * 2: A
         * 3: B
         * 4: Y
         * 5: LB
         * 6: RB
         * 7: LT
         * 8: RT
         * 9: back
         * 10: start
         * 11: left stick press
         * 12: right stick press
         */

        if(m_xbox.getRawButton(5)&&m_xbox.getRawButton(4)){
            m_drivetrain.resetGyro();
        }

        double speed = 0.8*Math.min(1, Math.hypot(m_xbox.getRawAxis(0), m_xbox.getRawAxis(1)));//filter.calculate(0.9*Math.min(1, m_xbox.getMagnitude()));//Math.hypot(x, y)

        // Angle between the y-axis and the direction the stick is pointed
        double angle = Math.toDegrees(Math.atan2(m_xbox.getRawAxis(0), -m_xbox.getRawAxis(1)));//Math.atan(x, -y)

        m_drivetrain.angle = angle;

        if(m_xbox.getRawButton(5)){
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
        double angle2 = Math.toDegrees(Math.atan2(m_xbox.getRawAxis(2), -m_xbox.getRawAxis(3)));
        double turn = getCorrection(angle2, 1);//Math.atan2(x, -y))
        double turnSpeed = 0.7*Math.min(1, Math.hypot(m_xbox.getRawAxis(2), m_xbox.getRawAxis(3)));
        l-=turnSpeed*Math.signum(turn);
        r+=turnSpeed*Math.signum(turn);

        if(Math.abs(error)>0.45){
            speed*=Math.signum(error);
            l-=speed;
            r+=speed;
        }else{
            l+=speed*(1-error);
            r+=speed*(1+error);
        }

        m_drivetrain.drive(l, r);
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