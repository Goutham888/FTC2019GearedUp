package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import net.frogbots.ftcopmodetunercommon.opmode.TunableLinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import static org.firstinspires.ftc.teamcode.robotBase.midTraverseRight;


@Autonomous(name = "CubeAuto AGGRESSIVE")
//@Disabled
public class cubeAutoAggressive extends LinearOpMode {
    robotBase robot = new robotBase();
    private ElapsedTime runtime = new ElapsedTime();

    int maxIndex = 0;
    int[] freq = new int[3];
    int max = 0;

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = "AbEDH9P/////AAABmcFPgUDLz0tMh55QD8t9w6Bqxt3h/G+JEMdItgpjoR+S1FFRIeF/w2z5K7r/nUzRZKleksLHPglkfMKX0NltxxpVUpXqj+w6sGvedaNq449JZbEQxaYe4SU+3NNi0LBN879h9LZW9RxJFOMt7HfgssnBdg+3IsiwVKKYnovU+99oz3gJkcOtYhUS9ku3s0Wz2n6pOu3znT3bICiR0/480N63FS7d6Mk6sqN7mNyxVcRf8D5mqIMKVNGAjni9nSYensl8GAJWS1vYfZ5aQhXKs9BPM6mST5qf58Tg4xWoHltcyPp0x33tgQHBbcel0M9pYe/7ub1pmzvxeBqVgcztmzC7uHnosDO3/2MAMah8qijd";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        robot.init(hardwareMap);
        robot.traverse.setPosition(midTraverseRight);
        robot.ADM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.ADM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //tensor flow IR start
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

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

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();

        waitForStart();
        runtime.reset();

        //Lower Lift
        if (opModeIsActive()) {
            robot.ADM.setTargetPosition((int) (robot.LEAD_SCREW_TURNS * robot.COUNTS_PER_MOTOR_REV_rev) - 100); //tuner
            robot.ADM.setPower(.75);
        }

        sleep(3000);
        robot.ADM.setPower(.05); //To stop jittering

        //Slide over
        if (opModeIsActive()) {
            robot.traverse.setPosition(robot.maxTraverse);
            sleep(2000);
            robot.encoderDriveStraight(4, 1.0, opModeIsActive(), runtime);
        }
        if (opModeIsActive()) {
            if (tfod != null) {
                tfod.activate();
            }
            runtime.reset();
            while (runtime.seconds() < 3) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        if (updatedRecognitions.size() == 1) {
                            int goldMineralX = -1;
                            for (Recognition recognition : updatedRecognitions) {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                    goldMineralX = (int) recognition.getLeft();
                                }
                            }
                            if (goldMineralX != -1) {
                                freq[1]++;
                            }
                        }
                    }
                }
                telemetry.addData("Time", runtime.seconds());
                telemetry.update();
            }
            telemetry.addData("Position Reached", "Between Scans");
            telemetry.update();
            robot.turnByGyro(-35, .05, opModeIsActive());

            sleep(1000);
            runtime.reset();
            while (runtime.seconds() < 2) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        if (updatedRecognitions.size() == 1) {
                            int goldMineralX = -1;
                            for (Recognition recognition : updatedRecognitions) {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                    goldMineralX = (int) recognition.getLeft();
                                }
                            }

                            if (goldMineralX != -1) {
                                freq[2]++;
                            }
                        }
                    }
                }
                telemetry.addData("Time", runtime.seconds());
                telemetry.update();
            }
            robot.turnByGyro(0, .05, opModeIsActive());
            for (int i = 0; i < freq.length; i++) {
                if (freq[i] > max) {
                    maxIndex = i;
                    max = freq[i];
                }
            }
            if(freq[2] < 2 && freq[1] < 2){
                maxIndex = 0;
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
        telemetry.addData("Location", maxIndex);
        telemetry.addData("Index 0", freq[0]);
        telemetry.addData("Index 1", freq[1]);
        telemetry.addData("Index 2", freq[2]);
        telemetry.update();

        if(maxIndex == 0) {
            robot.turnByGyro(40, .05, opModeIsActive());
            if (opModeIsActive()) {
                robot.intake.setPower(1.0);
            }
            robot.encoderDriveStraight(32, 4.0, opModeIsActive(), runtime);
            robot.turnByGyro(-45, .05, opModeIsActive());
            robot.encoderDriveStraight(32, 4.0, opModeIsActive(), runtime);
            robot.turnByGyro(45, .05, opModeIsActive());

        }
        else if(maxIndex == 1){
            if (opModeIsActive()) {
                robot.intake.setPower(1.0);
            }
            robot.encoderDriveStraight(30, 5.0, opModeIsActive(), runtime);
            robot.encoderDriveStraight(16, 2.0,opModeIsActive(), runtime);
            sleep(1000);
            robot.turnByGyro(45, .05, opModeIsActive());
            sleep(1000);
        }
        else if(maxIndex == 2){
            robot.turnByGyro(-40, .05, opModeIsActive());
            if (opModeIsActive()) {
                robot.intake.setPower(1.0);
            }
            robot.encoderDriveStraight(32, 4.0, opModeIsActive(), runtime);
            robot.turnByGyro(45, .05, opModeIsActive());
            robot.encoderDriveStraight(32, 4.0, opModeIsActive(), runtime);
        }

        //robot.turnByGyro(45, .05, opModeIsActive());
        //robot.encoderDriveStraight(-90, 5.0, opModeIsActive(), runtime);


        //Drop off marker (out, in)
        if (opModeIsActive()) {
            robot.marker.setPosition(robot.markerOut);
            sleep(1000);
            robot.marker.setPosition(robot.markerIn);
            sleep(1000);
        }
    }
        /**
         * Initialize the Vuforia localization engine.
         */
        private void initVuforia () {
            /*
             * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
             */
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

            parameters.vuforiaLicenseKey = VUFORIA_KEY;

            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
            //or
            //parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam");

            //  Instantiate the Vuforia engine
            vuforia = ClassFactory.getInstance().createVuforia(parameters);

            // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
        }

        /**
         * Initialize the Tensor Flow Object Detection engine.
         */
        private void initTfod () {
            int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                    "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
            tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
        }
}