//This package was commited by Gabe, your friendly Aperture Science Bad Code Associate!
// AD: Join the new Aperture "Code with portals" initiative today!!

//APERTURE SCIENCE INNOVATORS
//PROJECT DEUTRIUM
//WARNING: WE ARE NOT RESPONSIBLE FOR DAMAGE DONE TO DISPLAY, COMPUTER SYSTEM, OR THE SANITY OF
//UNAUTHORIZED USERS


package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    val robot= AimBotMovement()

    override fun runOpMode() {

        robot.init(hardwareMap)

        waitForStart()
         for (i in (0 .. 45)) {
            var ramp: Double = 1.1
            robot.lBDrive?.power = -ramp
            robot.lFDrive?.power = -ramp
            robot.rFDrive?.power = -ramp
            robot.rBDrive?.power = -ramp
            if (2 % ramp == 0.0) {
                ramp /= 2
            }

            sleep(60)
            println("Are you still there?")
            //yay you win
        }
    }
}