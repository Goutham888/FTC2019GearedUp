package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static android.os.SystemClock.sleep;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

@TeleOp(name = "TeleOp")
//Disabled
public class teleOp extends OpMode {
    RevBlinkinLedDriver.BlinkinPattern pattern;
    robotBase robot                     = new robotBase();
    private ElapsedTime runtime         = new ElapsedTime();

    int direction = -1;
    int targetPos= -2000;

    double leftPower = 0.0;
    double rightPower = 0.0;
    private boolean turnOffMotor=false;
    private boolean armLimitReached=true;
    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.ADM.setMode(STOP_AND_RESET_ENCODER);
        robot.ADM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftDrive.setMode(RUN_USING_ENCODER);
        robot.rightDrive.setMode(RUN_USING_ENCODER);

        robot.inVertical.setMode(STOP_AND_RESET_ENCODER);
        robot.inVertical.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.inVertical.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        runtime.reset();

    }

    public void start() {
        if(robot.inVertical.getCurrentPosition()>targetPos) {
            while ((robot.inVertical.getCurrentPosition() > targetPos)) {
                telemetry.addData("EncoderPos", robot.inVertical.getCurrentPosition());
                telemetry.update();
                robot.inVertical.setPower(0.03);
            }
        }
        robot.inVertical.setPower(0);
        robot.traverse.setPosition(robot.minTraverse);
    }

    @Override
    public void loop() {
        if(direction == 1) {
            leftPower = robot.getWheelPower(gamepad1.left_stick_y);
            rightPower = robot.getWheelPower(gamepad1.right_stick_y);
        }
        else if(direction == -1){
            rightPower = robot.getWheelPower(gamepad1.left_stick_y);
            leftPower = robot.getWheelPower(gamepad1.right_stick_y);
        }
        robot.leftDrive.setPower((leftPower * direction));
        robot.rightDrive.setPower((rightPower * direction));


        if(gamepad1.a){
            robot.ADM.setTargetPosition((int)(-robot.LEAD_SCREW_TURNS * robot.COUNTS_PER_MOTOR_REV_rev)+200);
            robot.ADM.setPower(-.5);
        }
        if(gamepad1.y){
            robot.ADM.setTargetPosition(-50);
            robot.ADM.setPower(.5);
        }

        if(gamepad1.x){
            robot.traverse.setPosition(robot.maxTraverse);
        }
        if(gamepad1.b){
            robot.traverse.setPosition(robot.minTraverse);
        }
        if(gamepad1.right_bumper){
            robot.traverse.setPosition(robot.midTraverseRight);
        }
        if(gamepad1.left_bumper){
            robot.traverse.setPosition(robot.midTraverseLeft);
        }

        if(gamepad1.dpad_up) {
            direction *= -1;
            sleep(500);
        }
        if(gamepad1.dpad_down) {
            robot.marker.setPosition(robot.markerMid);
            sleep(250);
        }

        if(gamepad1.dpad_left){
            double pos = robot.traverse.getPosition() + .02;
            if(pos < robot.maxTraverse) {
                robot.traverse.setPosition(pos);
                sleep(250);
            }
        }

        if(gamepad1.dpad_right){
            double pos = robot.traverse.getPosition() - .02;
            if(pos > robot.minTraverse) {
                robot.traverse.setPosition(pos);
                sleep(250);
            }
        }

        //------------------------------------------------------------------------------------------



        /*if(gamepad2.y){
            /// make arm move until reached hall effect sensor, then stop
                while (robot.vertHall.getState()) {
                    robot.inVertical.setPower(-0.03);
                }
                sleep(190);
            // Stop all motion;
            robot.inVertical.setPower(0);
            robot.inVertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        }*/
        /*if(!gamepad2.y) {
            if (robot.inVertical.getCurrentPosition() > 0) {
                //robot.inVertical.setPower(0.02);
                telemetry.addData("stop", "true");
            }
        }*/

        /*while(robot.vertHall.getState() && gamepad2.y && !armLimitReached){
            telemetry.addData("inVertical Direction", "In");
            telemetry.update();
            robot.inVertical.setPower(-0.03);
            turnOffMotor=true;
        }

        if(turnOffMotor){
            sleep(320);
            robot.inVertical.setPower(0);
            robot.inVertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            turnOffMotor=false;
        }
        if(robot.inVertical.getCurrentPosition()<0){
            robot.inVertical.setPower(-gamepad2.right_stick_y * 0.1);
            armLimitReached=false;
        }
        else {
            robot.inVertical.setPower(0.03);
            /*while (robot.inVertical.getCurrentPosition() > 0) {
                telemetry.addData("inVertical Direction","Out");
                telemetry.update();
                robot.inVertical.setPower(0.03);
            }*/
            /*sleep(190);
            robot.inVertical.setPower(0);
            armLimitReached=true;
        }*/
            /*
        if(robot.inVertical.getCurrentPosition()<0){
            armLimitReached=true;
        }
        if(armLimitReached){
            robot.inVertical.setPower(0.03);
            sleep(190);
            robot.inVertical.setPower(0);
            armLimitReached=false;
        }
        */
        //if(!armLimitReached){
            robot.inVertical.setPower(-gamepad2.right_stick_y * 0.1);

        //}

        telemetry.addData("Arm", robot.inVertical.getCurrentPosition());
        //Set motor power to stick input, directionally scaled
        robot.inHorizontal.setPower(gamepad2.left_stick_y);

        //Control Intake
        if(gamepad2.right_trigger > 0)
            robot.intake.setPower(.8);
        else if(gamepad2.left_trigger > 0)
            robot.intake.setPower(-.8);
        else
            robot.intake.setPower(0.0);

        if(gamepad2.a)
            robot.intakeGate.setPosition(1.0);
        else
            robot.intakeGate.setPosition(-1.0);

        telemetry.addData("Time", Math.round(runtime.seconds() - 8));
        if(runtime.seconds() >= 108 && runtime.seconds() <= 128){
            pattern = RevBlinkinLedDriver.BlinkinPattern.STROBE_RED;
        }
        else{
            pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
        }
        robot.blinkinLedDriver.setPattern(pattern);
        telemetry.update();
    }
}