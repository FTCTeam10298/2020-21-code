package robotCode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import jamesTelemetryMenu.TelemetryConsole
import robotCode.aimBotRobot.MecanumDriveTrain

@TeleOp(name="Aim Bot Tele-Op", group="Aim Bot")
class AimBotTeleOp(): OpMode() {

    val robot = MecanumDriveTrain()
    val console = TelemetryConsole(telemetry)

    override fun init() {
        console.display(1, "Initializing...")
        robot.init(hardwareMap)
        console.display(1, "Initialized")
    }

    override fun loop() {

        console.display(1, "Robot Running")

//        DRONE DRIVE
        val y = curveVal(gamepad1.left_stick_y.toDouble(), 0.5, -0.5, 0.5)
        val x = curveVal(gamepad1.left_stick_x.toDouble(), 0.5, -0.5, 0.5)
        val r = curveVal(gamepad1.right_stick_x.toDouble(), 0.5, -0.5, 0.5)

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
        if (gamepad1.right_trigger > 0)
            robot.belt.power = 0.8
        else
            robot.belt.power = 0.0


        console.display(2, "Collector: ")
        console.display(3, "Belt: ${robot.belt.power}")
        console.display(4, "Shooter: ${robot.shooter.power}")

        console.display(6, "LF: ${robot.lFDrive.power}")
        console.display(7, "RF: ${robot.rFDrive.power}")
        console.display(8, "LB: ${robot.lBDrive.power}")
        console.display(9, "RB: ${robot.rBDrive.power}")
    }

    fun curveVal(subject: Double, range1: Double, range2: Double, amp: Double): Double {

        return if (subject < range1 && subject > range2) {
            subject * amp
        }else{
            subject
        }
    }

}
