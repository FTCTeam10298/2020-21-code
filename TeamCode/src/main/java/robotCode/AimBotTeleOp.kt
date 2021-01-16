 package robotCode

//import buttonHelper.Gamepad1
import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import pid.MotorWithPID
import telemetryWizard.TelemetryConsole
import robotCode.hardwareClasses.MecanumDriveTrain
import kotlin.math.absoluteValue
import kotlin.math.pow

@TeleOp(name="Aim Bot Tele-Op", group="Aim Bot")
class AimBotTeleOp: OpMode() {

    val hardware = AimBotHardware()
    val robot = MecanumDriveTrain(hardware)
    val console = TelemetryConsole(telemetry)

    val shooterPID = MotorWithPID()
    val highGoalPreset = 4100
    var shooterRpm: Double = highGoalPreset.toDouble()
    var triggerHeld = false

    var driveDirection: Int = 1

    val invertHelp = ButtonHelper()
    val clawHelp = ButtonHelper()
    val collectorHelp1 = ButtonHelper()
    val collectorHelp2 = ButtonHelper()
    val gateHelp = ButtonHelper()
    val dUpHelp = ButtonHelper()
    val dDownHelp = ButtonHelper()
    val triggerHelper = ButtonHelper()

    override fun init() {
        console.display(1, "Initializing...")
        hardware.init(hardwareMap)
        console.display(1, "Initialized")
    }

    override fun start() {
        console.display(1, "Robot Running")

        hardware.shooter.power = 0.3
    }
    override fun loop() {


//        DRONE DRIVE
//        Invert
        if (invertHelp.stateChanged(gamepad1.left_stick_button) && gamepad1.left_stick_button) {
            driveDirection = -driveDirection //invert
        }

        val yInput = gamepad1.left_stick_y.toDouble()
        val xInput = gamepad1.left_stick_x.toDouble()
        val rInput = gamepad1.right_stick_x.toDouble()

        val y = yInput.pow(5) * driveDirection
        val x = xInput.pow(5) * driveDirection
        val r = rInput.pow(5) * 0.5 + 0.5 * rInput

        robot.driveSetPower(
                -(y - x - r),
                -(y + x + r),
                -(y + x - r),
                -(y - x + r)
        )


//        Shoot routine
        fun goToVelocity() {
            hardware.shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER;
            hardware.shooter.setVelocityPIDFCoefficients(450.0, 20.0, 0.0,0.0)
            hardware.shooter.velocity = (shooterRpm / 60.0 * 28)
        }

        fun isVelocityCorrect(): Boolean = toRPM(hardware.shooter.velocity) >= shooterRpm - percentage(2.0 , shooterRpm) && toRPM(hardware.shooter.velocity) <= shooterRpm + percentage(2.0 , shooterRpm)

        if (gamepad1.right_trigger > 0.2) {
            goToVelocity()
            if (isVelocityCorrect()) {
                hardware.gate.position = 1.0
                hardware.belt.power = 0.8
            }
            triggerHeld = true
        } else if (triggerHeld && !isVelocityCorrect()) {
            hardware.belt.power = 0.0
            hardware.shooter.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            hardware.shooter.power = 0.3   // Idle Shooter
            hardware.gate.position = 0.0
            triggerHeld = false
        }

        console.display(4, "Shooter rpm: ${hardware.shooter.velocity * 60 / 28}")
        console.display(5, "Shooter tps: ${hardware.shooter.velocity}")
        console.display(6, "Shooter Power: ${hardware.shooter.power}")

//        SHOOTER
        val shooterRpmIncrement: Int = 200

        when {
            (dUpHelp.stateChanged(gamepad1.dpad_up) && gamepad1.dpad_up) && shooterRpm < 5500 -> shooterRpm += shooterRpmIncrement
            (dDownHelp.stateChanged(gamepad1.dpad_down) && gamepad1.dpad_down) && shooterRpm > 0 + shooterRpmIncrement -> shooterRpm -= shooterRpmIncrement
            gamepad1.dpad_left || gamepad2.dpad_left -> hardware.shooter.power = 0.0
            gamepad1.dpad_right || gamepad2.dpad_right-> shooterRpm = highGoalPreset.toDouble()
        }

        if (gamepad1.left_trigger > 0.2 || gamepad2.left_trigger > 0.2) {
            hardware.shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER;
            hardware.shooter.setVelocityPIDFCoefficients(450.0, 20.0, 0.0, 0.0)
            hardware.shooter.velocity = (shooterRpm / 60.0 * 28)
        }

//        COLLECTOR
        if (collectorHelp1.stateChanged(gamepad1.right_bumper) && (gamepad1.right_bumper))
            if (hardware.collector.power == 1.0)
                hardware.collector.power = 0.0
            else
                hardware.collector.power = 1.0
        else if (collectorHelp2.stateChanged(gamepad1.left_bumper) && (gamepad1.left_bumper))
            if (hardware.collector.power == -1.0)
                hardware.collector.power = 0.0
            else
                hardware.collector.power = -1.0

//        BELT
        when {
            gamepad1.b || gamepad2.b -> hardware.belt.power = 0.8
            gamepad1.b || gamepad2.b -> hardware.belt.power = -0.8
            else -> hardware.belt.power = 0.0
        }

//        WOBBLE ARM
        val wobbleStick = gamepad2.right_stick_y
        hardware.wobbleArm.power = wobbleStick.toDouble()

//        CLAW
        if (clawHelp.stateChanged(gamepad2.x) && gamepad2.x) {
            when (hardware.lClaw.position) {
                0.0 -> {
                    hardware.lClaw.position = 5.0; hardware.rClaw.position = 5.0
                }
                else -> {
                    hardware.lClaw.position = 0.0; hardware.rClaw.position = 0.0
                }
            }
        }

//        GATE
        if (gateHelp.stateChanged(gamepad1.y) && gamepad1.y || gateHelp.stateChanged(gamepad2.y) && gamepad2.y) {
            when (hardware.gate.position) {
                0.0 -> {
                    hardware.gate.position = 1.0
                }
                else -> {
                    hardware.gate.position = 0.0
                }
            }
        }

//        CONSOLE
//        console.display(5, "Collector: ${robot.collector.power}")
        console.display(7, "Claw: ${hardware.lClaw.position}")
        console.display(8, "Belt: ${hardware.belt.power}")
        when {
            driveDirection > 0 -> console.display(9, "Collector first")
            else -> console.display(10, "Shooter first")
        }
        console.display(11, "LF: ${hardware.lFDrive.power}")
        console.display(12, "RF: ${hardware.rFDrive.power}")
        console.display(13, "LB: ${hardware.lBDrive.power}")
        console.display(14, "RB: ${hardware.rBDrive.power}")
    }
    fun toRPM(tps: Double): Double = tps * 60 / 28

    fun percentage(percent: Double, value: Double): Double = (value / 100) * percent

    fun pow(n: Double, exponent: Double): Double {
        var polarity: Double = 0.0
        polarity = when {
            n > 0 -> 1.0
            n < 0 -> -1.0
            else -> 0.0
        }
        return n.absoluteValue.pow(exponent) * polarity
    }
}
