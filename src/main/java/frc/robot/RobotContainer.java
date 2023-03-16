// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.DriveConstants;
import frc.robot.commands.TankDrive;
import frc.robot.commands.Drive2WJ;
import frc.robot.commands.Drive3;
import frc.robot.commands.Turn;
import frc.robot.commands.autonomous.*;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
    /* 
    // The robot's subsystems and commands are defined here...
    private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

    // Replace with CommandPS4Controller or CommandJoystick if needed
    private final CommandXboxController m_driverController =
        new CommandXboxController(OperatorConstants.kDriverControllerPort);*/

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("SmartDashboard");
    
    // Propeller m_propeller;

    private final Drivetrain m_drivetrain = new Drivetrain();

    Joystick m_stick1 = new Joystick(0);
    XboxController m_xbox = new XboxController(0);

    private final SendableChooser<Command> m_chooser = new SendableChooser<>();

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer(){
        inst.startServer();

        // Configure the trigger bindings
        configureBindings();

        CameraServer.startAutomaticCapture();

        m_chooser.addOption("Auto move only", new AutoMoveOnly(m_drivetrain));
        m_chooser.addOption("Auto charge only", new AutoChargeOnly(m_drivetrain));
        m_chooser.setDefaultOption("Auto charge move", new AutoChargeMove(m_drivetrain));
        
        SmartDashboard.putData(m_chooser);

        m_drivetrain.setDefaultCommand(
            //new Drive3(m_stick1, m_drivetrain)
            //new Drive2WJ(m_stick1, m_drivetrain)
            new TankDrive(
                () -> {
                    return getLeftSpeed();
                },
                () -> {                
                    return getRightSpeed();
                },
                m_drivetrain
            )
        );



        // try{
        //     Socket socket = new Socket("wpilibpi.local", 5801);
        //     InputStream input = socket.getInputStream();
        //     int character;
        //     StringBuilder data = new StringBuilder();
        //     InputStreamReader reader = new InputStreamReader(input);
        //      while((character = reader.read()) != -1){
        //         data.append((char)character);
        //      }

        //      SmartDashboard.putString("data: ", ""+data);
        // }catch (IOException e){
        //     SmartDashboard.putString("Exception: ", ""+e.toString());
        // }
    }

    /**
     * Gets the current speed of the left side of the robot.
     * @return the left speed
     */
    public double getLeftSpeed() {
        //double thrust = m_stick1.getY(); // forward-back
        double thrust = -m_xbox.getRawAxis(1);
        if(m_xbox.getPOV()==0){
            thrust = 0.2;
        }else if(m_xbox.getPOV()==180){
            thrust = -0.2;
        }
        double twist;
        //if (m_stick1.getRawButton(DriveConstants.kStraightButton)) {
            if (m_xbox.getLeftBumper()) {
            twist = 0;
        } else {
            //twist = m_stick1.getZ(); // twist
            twist = m_xbox.getRawAxis(4);
        }
        if(m_xbox.getPOV()==90){
            twist = 0.3;
        }else if(m_xbox.getPOV()==270){
            twist = -0.3;
        }

        
        //double throttle = (-m_stick1.getThrottle() + 2) / 3; // throttle
        double throttle = 0.6;

        if(m_xbox.getRawButton(6)){
            throttle = 0.2;
        }

        return -(thrust * (1 - DriveConstants.kTwistMultiplier * Math.abs(twist))
              + twist * DriveConstants.kTwistMultiplier) * throttle;

    }

    /**
     * Gets the current speed of the right side of the robot.
     * @return the right speed
     */
    public double getRightSpeed() {
        //double thrust = m_stick1.getY(); // forward-back
        double thrust = -m_xbox.getRawAxis(1);
        if(m_xbox.getPOV()==0){
            thrust = 0.2;
        }else if(m_xbox.getPOV()==180){
            thrust = -0.2;
        }
        double twist;
        //if (m_stick1.getRawButton(DriveConstants.kStraightButton)) {
            if (m_xbox.getLeftBumper()) {
            twist = 0;
        } else {
            //twist = m_stick1.getZ(); // twist
            twist = m_xbox.getRawAxis(4);
        }
        if(m_xbox.getPOV()==90){
            twist = 0.2;
        }else if(m_xbox.getPOV()==270){
            twist = -0.2;
        }

        
        //double throttle = (-m_stick1.getThrottle() + 2) / 3; // throttle
        double throttle = 0.6;

        if(m_xbox.getRawButton(6)){
            throttle = 0.2;
        }

        return -(thrust * (1 - DriveConstants.kTwistMultiplier * Math.abs(twist))
              - twist * DriveConstants.kTwistMultiplier) * throttle;

    }

    /**
     * Use this method to define your trigger->command mappings. Triggers can be created via the
     * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
     * predicate, or via the named factories in {@link
     * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
     * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
     * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
     * joysticks}.
     */
    private void configureBindings() {
        // frc2::JoystickButton(&m_stick2,2).WhenHeld(
        //     SpinPropeller(m_propeller)
        // );
        //new JoystickButton(m_stick1, DriveConstants.kTurnButton).onTrue(new Turn(90, m_drivetrain));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An example command will be run in autonomous
        return m_chooser.getSelected();
    }
}
