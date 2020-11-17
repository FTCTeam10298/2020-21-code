package robotCode

//import buttonHelper.Gamepad1
import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import jamesTelemetryMenu.TelemetryConsole
import robotCode.aimBotRobot.MecanumDriveTrain
import kotlin.math.absoluteValue
import kotlin.math.pow

@TeleOp(name="Aim Bot Tele-Op", group="Aim Bot")
class AimBotTeleOp(): OpMode() {

    val robot = MecanumDriveTrain()
    val console = TelemetryConsole(telemetry)

    var driveDirection: Int = 1
    val invertHelp = ButtonHelper()
    val clawHelp = ButtonHelper()
    val collectorHelp1 = ButtonHelper()
    val collectorHelp2 = ButtonHelper()

    override fun init() {
        console.display(1, "Initializing...")
        robot.init(hardwareMap)
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

//        SHOOTER
        val shooterPowerIncrement: Double = 0.008
        val shooterPower: Double = robot.shooter.power

        when {
            gamepad1.dpad_up && shooterPower < 1.0 -> robot.shooter.power += shooterPowerIncrement
            gamepad1.dpad_down && shooterPower > 0.0 + shooterPowerIncrement -> robot.shooter.power -= shooterPowerIncrement
            gamepad1.dpad_left -> robot.shooter.power = 0.0
            gamepad1.dpad_right -> robot.shooter.power = 1.0
        }

//        BELT
        when {
            gamepad1.right_trigger > 0 || gamepad2.right_trigger > 0 -> robot.belt.power = 0.8
            gamepad1.left_trigger > 0 || gamepad2.left_trigger > 0 -> robot.belt.power = -0.8
            else -> robot.belt.power = 0.0
        }

//        COLLECTOR
        if (collectorHelp1.stateChanged(gamepad1.right_bumper) && (gamepad1.right_bumper))
//            robot.collector.power = -robot.collector.power
            if (robot.collector.power == 1.0)
                robot.collector.power = 0.0
            else
                robot.collector.power = 1.0
        else if (collectorHelp2.stateChanged(gamepad1.left_bumper) && (gamepad1.left_bumper))
            if (robot.collector.power == -1.0)
                robot.collector.power = 0.0
            else
                robot.collector.power = -1.0

//        WOBBLE ARM
        robot.wobbleArm.power = -gamepad2.right_stick_y.toDouble().pow(5) * 0.5 + 0.5 * rInput

//        CLAW
        if (clawHelp.stateChanged(gamepad2.x) && gamepad2.x)
            when (robot.claw.position) {
                0.0  -> robot.claw.position = 1.0
                else -> robot.claw.position = 0.0
            }

//        CONSOLE
        console.display(4, "Shooter: ${robot.shooter.power}")
        console.display(5, "Collector: ${robot.collector.power}")
        console.display(6, "Claw: ${robot.claw.position}")
        console.display(7, "Belt: ${robot.belt.power}")
        when {
            driveDirection > 0 -> console.display(8, "Collector first")
            else -> console.display(8, "Shooter first")
        }
        console.display(9, "LF: ${robot.lFDrive.power}")
        console.display(10, "RF: ${robot.rFDrive.power}")
        console.display(11, "LB: ${robot.lBDrive.power}")
        console.display(12, "RB: ${robot.rBDrive.power}")
    }

//    var buttonPreviousValue = false
//    fun stateChanged(button: Boolean):Boolean {
//        val re: Boolean = buttonPreviousValue != button
//        buttonPreviousValue = button
//        return re
//    }

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
