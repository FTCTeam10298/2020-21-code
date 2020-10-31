package robotCode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import jamesTelemetryMenu.TelemetryMenu

@TeleOp(name="Aim Bot Tele-Op", group="Aim Bot")
class AimBotTeleOp(): OpMode() {

    val robot = AimBotHardware()
    val menu = TelemetryMenu(telemetry)

    fun fourMotors(rFpower: Double, lFpower: Double, lBpower: Double, rBpower: Double) {
        robot.rFDrive.power = rFpower
        robot.lFDrive.power = lFpower
        robot.lBDrive.power = lBpower
        robot.rBDrive.power = rBpower
    }

    override fun init() {
        robot.init(hardwareMap)
    }

    override fun loop() {

//        DRONE DRIVE
        val y =gamepad1.left_stick_y.toDouble()
        val x =gamepad1.left_stick_x.toDouble()
        val r =gamepad1.right_stick_x.toDouble()

//        fourMotors(
//                -(y + x + r),
//                -(y - x - r),
//                -(y + x - r),
//                -(y - x + r)
//        )

//        SHOOTER
//        when {
//            gamepad1.right_trigger.toDouble() > 0.2 -> robot.rFDrive.power = gamepad1.right_trigger.toDouble()
//            gamepad1.left_trigger.toDouble() > 0.2 -> robot.rFDrive.power = gamepad1.left_trigger.toDouble()
//            else -> robot.rFDrive.power = 0.0
//        }


        val shooterSpeedIncrement: Double = 0.008

        when {
            gamepad1.dpad_up && robot.shooter.power < 1.0 - shooterSpeedIncrement -> robot.shooter.power += shooterSpeedIncrement
//            gamepad1.dpad_down && robot.shooter!!.power > shooterSpeedIncrement     -> robot.shooter!!.power -= shooterSpeedIncrement
//            gamepad1.dpad_down && robot.shooter!!.power < shooterSpeedIncrement -> robot.shooter!!.power = 0.0
//            gamepad1.dpad_left -> robot.shooter!!.power = 0.0
        }
        menu.display(1, "${robot.shooter.power}")


    }
}
