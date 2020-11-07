//This package was commited by Gabe, your friendly Aperture Science Bad Code Associate!
// AD: Join the new Aperture "Code with portals" initiative today!!

//APERTURE SCIENCE INNOVATORS
//PROJECT DEUTRIUM (Branch Redacted)
//WARNING: WE ARE NOT RESPONSIBLE FOR DAMAGE DONE TO DISPLAY, COMPUTER SYSTEM, OR THE SANITY OF
//UNAUTHORIZED USERS


package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import jamesTelemetryMenu.TelemetryMenu
import robotCode.aimBotRobot.AimBotRobot


@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    val robot= AimBotRobot()
    val menu = TelemetryMenu(telemetry, gamepad1)

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
            menu.display(1, "Are you still there?")
        }

    }

    override fun runOpMode() {

        robot.init(hardwareMap)

        waitForStart()

//        Wins 5 Pts.
//        robot.drive.driveRobotPosition(1.0,100.0, false)

        robot.drive.driveRobotTurn(1.0, 360 * 1.0)

    }
}


