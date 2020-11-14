package robotCode

//import buttonHelper.Gamepad1
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

    override fun init() {
        console.display(1, "Initializing...")
        robot.init(hardwareMap)
        console.display(1, "Initialized")
    }


    override fun loop() {

        console.display(1, "Robot Running")

//        DRONE DRIVE
//        Invert
        if (stateChanged(gamepad1.left_stick_button) && gamepad1.left_stick_button) { // only fire event on button down
            driveDirection = -driveDirection //invert
        }

//        val y = driveDirection * gamepad1.left_stick_y.toDouble()
//        val x = driveDirection * gamepad1.left_stick_x.toDouble()
//        val r = gamepad1.right_stick_x.toDouble()


        val y = pow(gamepad1.left_stick_y.toDouble(),1.4) * driveDirection
        val x = pow(gamepad1.left_stick_x.toDouble(),2.2) * driveDirection
        val r = pow(gamepad1.right_stick_x.toDouble(),3.0)

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

//        BELT && GATE
        if (gamepad1.right_trigger > 0) {
            robot.belt.power = 0.8
            robot.gate.position = 1.0
        } else {
            robot.belt.power = 0.0
            robot.gate.position = 0.0
        }

//        CONSOLE
        console.display(4, "Collector: ")
        console.display(5, "Shooter: ${robot.shooter.power}")
        console.display(6, "Gate: ${robot.gate.position}")
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

    var buttonPreviousValue = false
    fun stateChanged(button: Boolean):Boolean {
        val re: Boolean = buttonPreviousValue != button
        buttonPreviousValue = button
        return re
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

    fun curveVal(subject: Double, range1: Double, range2: Double, amp: Double): Double {

        return if (subject < range1 && subject > range2) {
            subject * amp
        } else {
            subject
        }
    }
}
