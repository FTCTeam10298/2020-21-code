package robotCode

import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import kotlin.math.abs
import kotlin.math.absoluteValue
import creamsicleGoalDetection.CreamsicleGoalDetector
import creamsicleGoalDetection.UltimateGoalAimer
import openCvAbstraction.OpenCvAbstraction
import pid.PID
import ringDetector.RingDetector
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole
import kotlin.math.pow

@TeleOp(name="ChoiVico TeleOp", group="ChoiVico")
class ChoiVicoTeleOp: OpMode() {

    val console = TelemetryConsole(telemetry)
    val hardware = ChoiVicoHardware()
    val robot = OdometryDriveMovement(console, hardware)

    val opencv = OpenCvAbstraction(this)

//    val goalDetector = CreamsicleGoalDetector(console)
//    val turret = UltimateGoalAimer(console, goalDetector, hardware)

    val highGoalPreset = 4550
//    val powerShotsPreset = 4000
    var shooterRpm: Double = highGoalPreset.toDouble()
    var shooterReving = false
    var ringShooting: RingShooting = RingShooting.One
    var triggerDown: Boolean = false

    var loopTime: Double = 0.0
    var lastTime: Double = 0.0

    val gamepad2RightBumperHelper = ButtonHelper()
    val gamepad2LeftBumperHelper = ButtonHelper()
    val gamepad1RightBumperHelper = ButtonHelper()
    val gamepad1LeftBumperHelper = ButtonHelper()
    val dUpHelp = ButtonHelper()
    val dDownHelp = ButtonHelper()
    val clawHelp = ButtonHelper()
    val turretHelp = ButtonHelper()


    override fun init() {
        hardware.init(hardwareMap)

        opencv.cameraName = hardware.turretCameraName
//        opencv.onNewFrame(goalDetector::scoopFrame)
        opencv.init(hardwareMap)
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


//        LIFT
        hardware.lift1.position = gamepad2.left_stick_y.toDouble()
        hardware.lift2.position = gamepad2.left_stick_y.toDouble()

//        if (ringsIn == 0) {
//            hardware.lift1.position = 0.5
//            hardware.lift2.position = 0.5
//        }

//        Shoot routine
        fun goToVelocity() {
            hardware.shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER;
            hardware.shooter.setVelocityPIDFCoefficients(450.0, 20.0, 0.0,0.0)
            hardware.shooter.velocity = (shooterRpm / 60.0 * 28)
        }

        fun isVelocityCorrect(): Boolean = toRPM(hardware.shooter.velocity) >= shooterRpm - percentage(10.0 , shooterRpm)

        fun stateChanged(): Boolean = !triggerDown && gamepad1.right_trigger > 0.5

        fun shoot() {

            when (ringShooting) {
                RingShooting.One -> {
                    hardware.roller.power = 0.0
                    hardware.lift1.position = 0.8
                    hardware.lift2.position = 0.8
                    if (waitInLoop(1000))
                        hardware.roller.power = 1.0
                    if (waitInLoop(1000))
                        hardware.roller.power = 0.0
                    ringShooting = RingShooting.Two

                }
                RingShooting.Two -> {
                    hardware.roller.power = 0.0
                    hardware.lift1.position = 0.7
                    hardware.lift2.position = 0.7
                    if (waitInLoop(1000))
                        hardware.roller.power = 1.0
                    if (waitInLoop(1000))
                        hardware.roller.power = 0.0
                    ringShooting = RingShooting.Three
                }
                RingShooting.Three -> {
                    hardware.roller.power = 0.0
                    hardware.lift1.position = 0.6
                    hardware.lift2.position = 0.6
                    if (waitInLoop(1000))
                        hardware.roller.power = 1.0
                    if (waitInLoop(1000))
                        hardware.roller.power = 0.0
                    ringShooting = RingShooting.One
                }
            }
        }

        if (gamepad2.b || gamepad1.b)
            shoot()

        if (gamepad1.left_trigger > 0.2 || gamepad2.left_trigger > 0.2 || gamepad1.right_trigger > 0.2 || gamepad2.right_trigger > 0.2) {
            goToVelocity()

            ringShooting = RingShooting.One

            if (isVelocityCorrect() && (gamepad1.right_trigger > 0.2 || gamepad2.right_trigger > 0.2)) {
                shoot()
                hardware.roller.power = 1.0
            }
            triggerDown = gamepad1.right_trigger > 0.2

            shooterReving = true
        } else if (shooterReving && !isVelocityCorrect()) {
            hardware.shooter.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            hardware.shooter.power = 0.3    // Idle Shooter
            hardware.roller.power = 0.0
            shooterReving = false
        }

//        SHOOTER
        val shooterRpmIncrement: Int = 200

        when {
            (dUpHelp.stateChanged(gamepad1.dpad_up) && gamepad1.dpad_up) && shooterRpm < 5500 -> shooterRpm += shooterRpmIncrement
            (dDownHelp.stateChanged(gamepad1.dpad_down) && gamepad1.dpad_down) && shooterRpm > 0 + shooterRpmIncrement -> shooterRpm -= shooterRpmIncrement
            gamepad1.dpad_left || gamepad2.dpad_left -> hardware.shooter.power = 0.0
            gamepad1.dpad_right || gamepad2.dpad_right-> shooterRpm = highGoalPreset.toDouble()
        }

//        if (gamepad2.b || gamepad1.b)
//            shooterRpm = powerShotsPreset.toDouble()

//        TURRET
        if (gamepad2.left_stick_x.toDouble() !== 0.0 || gamepad2.left_stick_button)
            hardware.turret.power = gamepad2.left_stick_x.toDouble()
//        else
//            turret.updateAimAndAdjustRobot()

//        COLLECTOR
        if ((gamepad1RightBumperHelper.stateChanged(gamepad1.right_bumper) && (gamepad1.right_bumper)) || (gamepad2RightBumperHelper.stateChanged(gamepad2.right_bumper) && (gamepad2.right_bumper)))
            if (hardware.collector.power == 1.0)
                hardware.collector.power = 0.0
            else
                hardware.collector.power = 1.0
        else if ((gamepad1LeftBumperHelper.stateChanged(gamepad1.left_bumper) && (gamepad1.left_bumper)) || (gamepad2LeftBumperHelper.stateChanged(gamepad2.left_bumper) && (gamepad2.left_bumper)))
            if (hardware.collector.power == -1.0)
                hardware.collector.power = 0.0
            else
                hardware.collector.power = -1.0

//        WOBBLE ARM
//        if (gamepad2.right_stick_y > 0) {
//            hardware.wobble.targetPosition = 2
//            hardware.wobble.mode = DcMotor.RunMode.RUN_TO_POSITION
//            hardware.wobble.power = 0.8
//        } else if (gamepad2.right_stick_y < 0) {
//            hardware.wobble.targetPosition = -2
//            hardware.wobble.mode = DcMotor.RunMode.RUN_TO_POSITION
//            hardware.wobble.power = 0.8
//        }
        hardware.wobble.power = gamepad2.right_stick_y.toDouble()

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

//        ROLLER
        if (gamepad1.y || gamepad2.y)
            hardware.roller.power = 1.0
//        if (rollerHelper.stateChanged(gamepad1.y) && !gamepad1.y)
//            hardware.roller.power = 0.0

        loopTime = time - lastTime
        lastTime = time

        console.display(2, "Loop time: $loopTime")
        console.display(3, "Ring #$ringShooting")
//        console.display(4, "Shooter rpm: ${hardware.shooter.velocity * 60 / 28}")
        console.display(4, "Target rpm: $shooterRpm")
//        console.display(5, "Shooter tps: ${hardware.shooter.velocity}")
        console.display(5, "Shooter Power: ${hardware.shooter.power}")
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

    enum class RingShooting {
        One,
        Two,
        Three
    }

    fun waitInLoop(ms: Int): Boolean = timeSince(time) >= ms

    fun timeSince(ms: Double): Double = time - ms
}