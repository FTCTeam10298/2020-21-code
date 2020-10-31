//This package was commited by Gabe, your friendly Aperture Science Bad Code Associate!
// AD: Join the new Aperture "Code with portals" initiative today!!

//APERTURE SCIENCE INNOVATORS
//PROJECT DEUTRIUM (Branch Redacted)
//WARNING: WE ARE NOT RESPONSIBLE FOR DAMAGE DONE TO DISPLAY, COMPUTER SYSTEM, OR THE SANITY OF
//UNAUTHORIZED USERS


package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    val robot= AimBotMovement()

    fun abscondTime(Interval:Int, milis:Int) {

        for (i in (0 .. Interval)) {

            var ramp: Double = 1.1

            robot.lBDrive?.power = ramp
            robot.lFDrive?.power = ramp
            robot.rFDrive?.power = ramp
            robot.rBDrive?.power = ramp

            if (2 % ramp == 0.0) {
                ramp /= 2
            }

            sleep(milis.toLong())
            telemetry.addLine("Are you still there?")
        }

    }


    override fun runOpMode() {

        robot.init(hardwareMap)

        waitForStart()

//        Wins 5 Pts.
//        abscondTime(50,60)

//        robot.rFDrive?.power = 1.0
        telemetry.addLine("Combat position: ${robot.rFDrive?.currentPosition}")
        telemetry.update()
        sleep(1000)
        while (robot.rFDrive!!.currentPosition >= 2000) {
            telemetry.addLine("Current position: ${robot.rFDrive?.currentPosition}")
            robot.rFDrive?.power = 1.0
            telemetry.update()
        }
        robot.rFDrive?.power = 0.0

    }
}
