package us.brainstormz.choivico.robotCode

import us.brainstormz.choivico.buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import kotlin.math.abs
import kotlin.math.absoluteValue
import us.brainstormz.choivico.creamsicleGoalDetection.CreamsicleGoalDetector
import us.brainstormz.choivico.creamsicleGoalDetection.UltimateGoalAimer
import us.brainstormz.choivico.openCvAbstraction.OpenCvAbstraction
import us.brainstormz.choivico.robotCode.hardwareClasses.OdometryDriveTrain
import us.brainstormz.choivico.telemetryWizard.TelemetryConsole
import kotlin.math.pow

@TeleOp(name="ChoiVico TeleOp", group="ChoiVico")
class ChoiVicoTeleOp: OpMode() {

    val console = TelemetryConsole(telemetry)
    val hardware = ChoiVicoHardware()

    val robot = OdometryDriveTrain(hardware, console)

    val opencv = OpenCvAbstraction(this)

    val goalDetector = CreamsicleGoalDetector(console)
    val turret = UltimateGoalAimer(console, goalDetector, hardware)

    val highGoalPreset = 4550
//    val powerShotsPreset = 4000
    var shooterRpm: Double = highGoalPreset.toDouble()
    var shooterReving = false
    var triggerDown: Boolean = false
    var timeKniodIn: Long = 0L
    var timeKniodOut: Long = 0L
    var ringLevel: LiftStage = LiftStage.Bottom

    var turretState: Boolean = false

    var loopTime: Double = 0.0
    var lastTime: Double = 0.0

    val liftMonitor = LiftMonitor()
    val gamepad2RightBumperHelper = ButtonHelper()
    val gamepad2LeftBumperHelper = ButtonHelper()
    val gamepad1RightBumperHelper = ButtonHelper()
    val gamepad1LeftBumperHelper = ButtonHelper()
    val dUpHelp = ButtonHelper()
    val dDownHelp = ButtonHelper()
    val clawHelp = ButtonHelper()
    val flapHelp = ButtonHelper()
    val kniodHelp = ButtonHelper()
    val liftUpHelp = ButtonHelper()
    val liftDownHelp = ButtonHelper()
    val turrethelp = ButtonHelper()


    override fun init() {
        hardware.init(hardwareMap)

        opencv.cameraName = hardware.turretCameraName
//        opencv.onNewFrame(goalDetector::scoopFrame)
//        opencv.init(hardwareMap)
//        opencv.start()

    }

    override fun start() {
        hardware.shooter.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        hardware.shooter.power = 0.3    // Idle Shooter
    }

    override fun loop() {

//        DRONE DRIVE
        val yInput = gamepad1.left_stick_y.toDouble()
        val xInput = gamepad1.left_stick_x.toDouble()
        val rInput = gamepad1.right_stick_x.toDouble()

        val y = -yInput
        val x = xInput
        val r = -rInput * abs(rInput)
//        val y = yInput.pow(5)
//        val x = xInput.pow(5)
//        val r = rInput.pow(5) * 0.5 + 0.5 * rInput

        robot.driveSetPower(
                (y + x - r),
                (y - x + r),
                (y - x - r),
                (y + x + r)
        )

//        SHOOTER ROUTINE
        fun goToVelocity() {
            hardware.shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER;
            hardware.shooter.setVelocityPIDFCoefficients(450.0, 20.0, 0.0, 0.0)
            hardware.shooter.velocity = (shooterRpm / 60.0 * 28)
        }

        fun isVelocityCorrect(): Boolean = toRPM(hardware.shooter.velocity) >= shooterRpm - percentage(10.0, shooterRpm)

        if (gamepad1.left_trigger > 0.0 || gamepad2.left_trigger > 0.0 || gamepad1.right_trigger > 0.0 || gamepad2.right_trigger > 0.0)
            goToVelocity()
        else {
            hardware.shooter.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            hardware.shooter.power = 0.3    // Idle Shooter
        }

//        val liftButtonPressed = (gamepad2.b || gamepad1.b || (gamepad1.right_trigger > 0) || (gamepad2.right_trigger > 0))
//
//        when (liftMonitor.nextStageForServo(System.currentTimeMillis(), liftButtonPressed, timeKniodIn, timeKniodOut, hardware.liftLimit.isPressed)) {
//            LiftStage.Bottom -> {
//                hardware.lift1.position = 0.0
//            }
//            LiftStage.A -> {
//                hardware.lift1.position = 0.41
//            }
//            LiftStage.B -> {
//                hardware.lift1.position = 0.47
//            }
//            LiftStage.C -> {
//                hardware.lift1.position = 0.58
//            }
//            null -> {
//
//            }
//        }
//
//        val currentOperation = liftMonitor.currentOperation()
//        val text = if(currentOperation==null){
//            "null"
//        }else{
//            "in-progress? $currentOperation"
//        }
//        console.display(10, "isKniodOut? ${liftMonitor.isKniodOut()}, currentOperation = $text")
//
////        if (gamepad2.b || gamepad1.b)
////            hardware.lift1.position = 0.33
//
//        val aKniodButtonIsPressed = gamepad2.y || gamepad1.y
//        if(kniodHelp.stateChanged(aKniodButtonIsPressed)){
//            if(aKniodButtonIsPressed){
//                timeKniodOut = System.currentTimeMillis()
//                hardware.kniod.position = 0.9
//            }else{
//                timeKniodIn = System.currentTimeMillis()
//                hardware.kniod.position = 0.5
//            }
//        }

        if (liftUpHelp.stateChanged(gamepad2.dpad_up) && gamepad2.dpad_up)
            ringLevel = when (ringLevel) {
                LiftStage.Bottom -> LiftStage.A
                LiftStage.A -> LiftStage.B
                LiftStage.B -> LiftStage.C
                LiftStage.C -> LiftStage.Bottom
            }

        if (liftDownHelp.stateChanged(gamepad2.dpad_down) && gamepad2.dpad_down)
            ringLevel = when (ringLevel) {
                LiftStage.Bottom -> LiftStage.C
                LiftStage.A -> LiftStage.Bottom
                LiftStage.B -> LiftStage.A
                LiftStage.C -> LiftStage.B
            }

        when (ringLevel) {
            LiftStage.Bottom -> {
                hardware.lift1.position = 0.0
            }
            LiftStage.A -> {
                hardware.lift1.position = 0.30
            }
            LiftStage.B -> {
                hardware.lift1.position = 0.47
            }
            LiftStage.C -> {
                hardware.lift1.position = 0.592
            }
        }

        val aKniodButtonIsPressed = gamepad2.y || gamepad1.y
        if(kniodHelp.stateChanged(aKniodButtonIsPressed)){
            if(aKniodButtonIsPressed){
                timeKniodOut = System.currentTimeMillis()
                hardware.kniod.position = 0.9
            }else{
                timeKniodIn = System.currentTimeMillis()
                hardware.kniod.position = 0.5
            }
        }

//        SHOOTER
        val shooterRpmIncrement: Int = 200

        when {
            (dUpHelp.stateChanged(gamepad1.dpad_up) && gamepad1.dpad_up) && shooterRpm < 5500 -> shooterRpm += shooterRpmIncrement
            (dDownHelp.stateChanged(gamepad1.dpad_down) && gamepad1.dpad_down) && shooterRpm > 0 + shooterRpmIncrement -> shooterRpm -= shooterRpmIncrement
            gamepad1.dpad_left || gamepad2.dpad_left -> hardware.shooter.power = 0.0
            gamepad1.dpad_right || gamepad2.dpad_right -> shooterRpm = highGoalPreset.toDouble()
        }

        if (flapHelp.stateChanged((gamepad1.a || gamepad2.a)) && (gamepad1.a || gamepad2.a)) {
            when (hardware.flap.position) {
                0.5 -> hardware.flap.position = 0.6
                0.6 -> hardware.flap.position = 0.7
                0.7 -> hardware.flap.position = 1.0
                else -> hardware.flap.position = 0.5
            }
        }

        console.display(12, "current high ${ringLevel}")


//        TURRET

        if (abs(gamepad2.left_stick_x.toDouble()) > 0.1)
            hardware.turret.power = gamepad2.left_stick_x.toDouble()
        else if (turrethelp.stateChanged(gamepad2.left_stick_button) && gamepad2.left_stick_button) {
            turretState = !turretState
        } else if (turretState)
            turret.updateAimAndAdjustRobot()
        else
            hardware.turret.power = gamepad2.left_stick_x.toDouble()

//        COLLECTOR
        if ((gamepad1RightBumperHelper.stateChanged(gamepad1.right_bumper) && (gamepad1.right_bumper)) || (gamepad2RightBumperHelper.stateChanged(gamepad2.right_bumper) && (gamepad2.right_bumper)))
            if (hardware.collector.power == 1.0) {
                hardware.bottomTrans.power = 0.0
                hardware.transfer.power = 0.0
                hardware.collector.power = 0.0
            } else {
                hardware.bottomTrans.power = 1.0
                hardware.transfer.power = 1.0
                hardware.collector.power = 1.0
            }
        else if ((gamepad1LeftBumperHelper.stateChanged(gamepad1.left_bumper) && (gamepad1.left_bumper)) || (gamepad2LeftBumperHelper.stateChanged(gamepad2.left_bumper) && (gamepad2.left_bumper)))
            if (hardware.collector.power == -1.0) {
                hardware.bottomTrans.power = 0.0
                hardware.transfer.power = 0.0
                hardware.collector.power = 0.0
            } else {
                hardware.bottomTrans.power = -1.0
                hardware.transfer.power = -1.0
                hardware.collector.power = -1.0
            }

//        WOBBLE ARM

//        if (gamepad2.right_stick_y > 0) {
//            hardware.wobble.targetPosition = -1
//            hardware.wobble.mode = DcMotor.RunMode.RUN_TO_POSITION
//            hardware.wobble.power = 0.4
//        } else if (gamepad2.right_stick_y < 0) {
//            hardware.wobble.targetPosition = 1
//            hardware.wobble.mode = DcMotor.RunMode.RUN_TO_POSITION
//            hardware.wobble.power = -0.4
//        } else
//            hardware.wobble.power = 0.0

        hardware.wobble.power = gamepad2.right_stick_y.toDouble() / 2

//        CLAW
        if (clawHelp.stateChanged(gamepad2.x) && gamepad2.x) {
            when (hardware.claw1.position) {
                1.0 -> {
                    hardware.claw1.position = 0.5; hardware.claw2.position = 0.5
                }
                else -> {
                    hardware.claw1.position = 1.0; hardware.claw2.position = 1.0
                }
            }
        }


        loopTime = time - lastTime
        lastTime = time

        console.display(2, "Loop time: $loopTime")
        console.display(4, "Shooter rpm: ${hardware.shooter.velocity * 60 / 28}")
        console.display(5, "Target rpm: $shooterRpm")
        console.display(6, "Shooter Power: ${hardware.shooter.power}")
        console.display(7, "Lift Limit: ${hardware.liftLimit.isPressed}")
        console.display(8, "wobble: ${hardware.wobble.currentPosition}")

//        console.display(8, "LF: ${hardware.lFDrive.power}")
//        console.display(9, "RF: ${hardware.rFDrive.power}")
//        console.display(10, "LB: ${hardware.lBDrive.power}")
//        console.display(11, "RB: ${hardware.rBDrive.power}")
    }

    fun toRPM(tps: Double): Double = tps * 60 / 28

    fun percentage(percent: Double, value: Double): Double = (value / 100) * percent

    fun pow(input:Double, exponent: Double): Double {
        var polarity: Double = 0.0
        polarity = when {
            input > 0 -> 1.0
            input < 0 -> -1.0
            else -> 0.0
        }
        return input.absoluteValue.pow(exponent) * polarity
    }
}