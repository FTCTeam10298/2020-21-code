package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import robotCode.aimBotRobot.AimBotRobot
import jamesTelemetryMenu.TelemetryMenu
import buttonHelper.ButtonHelper

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    val robot= AimBotRobot()
//    val menu = TelemetryMenu(telemetry, gamepad1)

    override fun runOpMode() {

        robot.init(hardwareMap)

        waitForStart()

//        Wins 5 Pts.
        robot.drive.driveRobotPosition(1.0,100.0, false)

//        robot.drive.driveRobotTurn(1.0, 360 * 1.0)
1
    }

    fun abscondTime(Interval:Int, milis:Int) {

        for (i in (0 .. Interval)) {

            var ramp: Double = 1.1

            robot.lBDrive.power = ramp
            robot.lFDrive.power = ramp
            robot.rFDrive.power = ramp
            robot.rBDrive.power = ramp

            if (2 % ramp == 0.0) {
                ramp /= 2
            }

            Thread.sleep(milis.toLong())
        }

    }


}


