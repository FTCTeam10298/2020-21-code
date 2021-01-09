 package robotCode

//import buttonHelper.Gamepad1
import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import jamesTelemetryMenu.TelemetryConsole
import robotCode.hardwareClasses.MecanumDriveTrain
import kotlin.math.absoluteValue
import kotlin.math.pow

@TeleOp(name="Aim Bot Tele-Op", group="Aim Bot")
class AimBotTeleOp(): OpMode() {

    val hardware = AimBotHardware()
    val robot = MecanumDriveTrain(hardware)
    val console = TelemetryConsole(telemetry)

    var driveDirection: Int = 1
    var shooterRpm = 4114
    var triggerHeld = false

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


    override fun loop() {

        console.display(1, "Robot Running")

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
        if (gamepad1.right_trigger > 0.2) {
            hardware.shooter.setVelocityPIDFCoefficients(55.0, 1.0, 0.0,0.0)
            hardware.shooter.velocity = (shooterRpm / 60 * 28).toDouble()
            if (hardware.shooter.velocity * 60 / 28 >= shooterRpm /*RPM*/ ) {
                hardware.gate.position = 1.0
                hardware.belt.power = 0.8
            }
            triggerHeld = true
        } else if (triggerHeld) {
            hardware.belt.power = 0.0
            hardware.shooter.power = 0.3   // Idle Shooter
            hardware.gate.position = 0.0
            triggerHeld = false
        }

        console.display(4, "Shooter rpm: ${hardware.shooter.velocity * 60 / 28}")
        console.display(5, "Shooter Power: ${hardware.shooter.power}")

//        SHOOTER
        val shooterRpmIncrement: Int = 300

        when {
            (dUpHelp.stateChanged(gamepad1.dpad_up) && gamepad1.dpad_up) && shooterRpm < 5500 -> shooterRpm += shooterRpmIncrement
            (dDownHelp.stateChanged(gamepad1.dpad_down) && gamepad1.dpad_down) && shooterRpm > 0 + shooterRpmIncrement -> shooterRpm -= shooterRpmIncrement
            gamepad1.dpad_left -> hardware.shooter.power = 0.0
            gamepad1.dpad_right -> shooterRpm = 4114
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
        if (gateHelp.stateChanged(gamepad1.y) && gamepad1.y) {
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
        console.display(6, "Claw: ${hardware.lClaw.position}")
        console.display(7, "Belt: ${hardware.belt.power}")
        when {
            driveDirection > 0 -> console.display(8, "Collector first")
            else -> console.display(8, "Shooter first")
        }
        console.display(9, "LF: ${hardware.lFDrive.power}")
        console.display(10, "RF: ${hardware.rFDrive.power}")
        console.display(11, "LB: ${hardware.lBDrive.power}")
        console.display(12, "RB: ${hardware.rBDrive.power}")
    }

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
