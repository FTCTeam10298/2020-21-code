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

    fun rampShooterPower(targetPower: Double) {
        while (robot.shooter.power < targetPower) {
            robot.shooter.power = targetPower - robot.shooter.power
        }
    }

    override fun init() {
        robot.init(hardwareMap)
    }

    override fun loop() {

//        DRONE DRIVE
        val y =gamepad1.left_stick_y.toDouble()
        val x =gamepad1.left_stick_x.toDouble()
        val r =gamepad1.right_stick_x.toDouble()

        menu.display(3, "Y: $y")
        menu.display(4, "X: $x")
        menu.display(5, "X: $r")

        fourMotors(
                -(y + x + r),
                -(y - x - r),
                -(y + x - r),
                -(y - x + r)
        )

        menu.display(7, "LF: ${robot.lFDrive.power}")
        menu.display(8, "RF: ${robot.rFDrive.power}")
        menu.display(9, "LB: ${robot.lBDrive.power}")
        menu.display(10, "RB: ${robot.rBDrive.power}")

//        SHOOTER
        val shooterPowerIncrement: Double = 0.008
        val shooterPower: Double = robot.shooter.power

        when {
            gamepad1.dpad_up && shooterPower < 1.0 -> robot.shooter.power += shooterPowerIncrement
            gamepad1.dpad_down && shooterPower > 0.0 + shooterPowerIncrement -> robot.shooter.power -+ shooterPowerIncrement
            gamepad1.dpad_left -> robot.shooter.power = 0.0
            gamepad1.dpad_right -> rampShooterPower(1.0)
        }

        menu.display(1, "Shooter Power: ${robot.shooter.power}")

    }
}
