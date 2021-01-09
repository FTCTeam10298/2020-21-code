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
            hardware.shooter.velocity = 1980.0
            if (hardware.shooter.velocity * 60 / 28 >= 4114.0 /*RPM*/ ) {
                hardware.gate.position = 1.0
                hardware.belt.power = 0.8
            }
        } else {
            hardware.belt.power = 0.0
//            robot.shooter.power = 1.0   // Idle Shooter
            hardware.gate.position = 0.0
        }
        console.display(4, "Shooter rpm: ${hardware.shooter.velocity * 60 / 28}")
        console.display(5, "Shooter Power: ${hardware.shooter.power}")

//        SHOOTER
        val shooterPowerIncrement: Double = 0.008
        val shooterPower: Double = hardware.shooter.power

        when {
            (dUpHelp.stateChanged(gamepad1.dpad_up) && gamepad1.dpad_up) && shooterPower < 1.0 -> hardware.shooter.power += shooterPowerIncrement
            (dDownHelp.stateChanged(gamepad1.dpad_down) && gamepad1.dpad_down) && shooterPower > 0.0 + shooterPowerIncrement -> hardware.shooter.power -= shooterPowerIncrement
            gamepad1.dpad_left -> hardware.shooter.power = 0.0
            gamepad1.dpad_right -> hardware.shooter.power = 0.83
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
        if (clawHelp.stateChanged(gamepad2.x) && gamepad2.x)
            when (hardware.claw.position) {
                0.0  -> hardware.claw.position = 1.0
                else -> hardware.claw.position = 0.0
            }

//        CONSOLE
//        console.display(5, "Collector: ${robot.collector.power}")
        console.display(6, "Claw: ${hardware.claw.position}")
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
