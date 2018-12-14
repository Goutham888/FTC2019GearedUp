package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import net.frogbots.ftcopmodetunercommon.opmode.TunableLinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import static org.firstinspires.ftc.teamcode.robotBase.midTraverseRight;


@Autonomous(name = "CubeAuto SAFE")
//@Disabled
public class cubeAuto extends LinearOpMode {
    robotBase robot = new robotBase();
    private ElapsedTime runtime = new ElapsedTime();

    public void runOpMode() {
        robot.init(hardwareMap);
        robot.traverse.setPosition(midTraverseRight);
        robot.ADM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.ADM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.log().add("Gyro Calibrating. Do Not Move!");

       runtime.reset();
        while (robot.navxMicro.isCalibrating())  {
            telemetry.addData("calibrating", "%s", Math.round(runtime.seconds())%2==0 ? "|.." : "..|");
            telemetry.update();
            sleep(50);
        }
        telemetry.log().clear(); telemetry.log().add("Gyro Calibrated. Press Start.");
        telemetry.clear();

        telemetry.update();

        waitForStart();
        runtime.reset();

        //Lower Lift
        if (opModeIsActive()){
            robot.ADM.setTargetPosition((int) (robot.LEAD_SCREW_TURNS * robot.COUNTS_PER_MOTOR_REV_rev) - 100); //tuner
            robot.ADM.setPower(.5);
            telemetry.addData("Lift Encoder Value", robot.ADM.getCurrentPosition());
        }

        sleep(5000);
        robot.ADM.setPower(.05); //To stop jittering

        //Slide over
        if (opModeIsActive()){
            robot.traverse.setPosition(robot.maxTraverse);
            robot.marker.setPosition(robot.markerMid);
            sleep(1000);
            robot.intakePitch.setPosition(robot.boxFlat);
            sleep(1000);
            robot.intake.setPower(-1.0);
            robot.marker.setPosition(robot.markerIn);
        }
        sleep(1000);

        //Drive Forward into depot
        robot.encoderDriveStraight(36, 5.0, opModeIsActive(), runtime);

        //Turn to the left
        robot.intake.setPower(-1.0);
        sleep(500);
        robot.intake.setPower(0.0);
        sleep(500);
        robot.intakePitch.setPosition(robot.boxStowed);
        sleep(500);
        robot.encoderDriveStraight(16, 2.0,opModeIsActive(), runtime);
        sleep(1000);
        robot.turnByGyro(45, .05, opModeIsActive());
        sleep(1000);

        //Drop off marker (out, in)
        if(opModeIsActive()){
            robot.marker.setPosition(robot.markerOut);
            sleep(1000);
            robot.marker.setPosition(robot.markerIn);
            sleep(1000);
        }

    }
}
