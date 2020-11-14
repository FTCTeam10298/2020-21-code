package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import jamesTelemetryMenu.TelemetryMenu
import robotCode.aimBotRobot.EncoderDriveMovement

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    var menu: TelemetryMenu = TelemetryMenu(telemetry)
    val robot= EncoderDriveMovement(menu)

    override fun runOpMode() {

        robot.init(hardwareMap)

        waitForStart()

//        Wins 5 Pts.

//        robot.driveSetRunToPosition()
//        robot.lFDrive.targetPosition = 5000
//        robot.lFDrive.power = 1.0
//        while (robot.lFDrive.isBusy) {
//            sleep(10)
//            menu.display(2, robot.lFDrive.power.toString())
//        }
        robot.driveRobotPosition(0.5,100.0, true)
//        robot.driveRobotTurn(1.0, 360 * 1.0)

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