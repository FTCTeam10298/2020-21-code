//This package was commited by Gabe, your friendly Aperture Science Bad Code Associate!
// AD: Join the new Aperture "Code with portals" initiative today!!

//APERTURE SCIENCE INNOVATORS
//PROJECT DEUTRIUM
//WARNING: WE ARE NOT RESPONSIBLE FOR DAMAGE DONE TO DISPLAY, COMPUTER SYSTEM, OR THE SANITY OF
//UNAUTHORIZED USERS

package deutriumAuto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name="Aim Bot Auto: Plan Deutrium", group="Aim Bot")
class DeutriumAuto(): LinearOpMode() {

    val robot= DeutriumHardware()

    override fun runOpMode() {
//        after init pressed
        robot.init(hardwareMap)

        waitForStart()
//        after start pressed

        for (i in (0 .. 6)) {
            var ramp: Double = 1
            robot.lBDrive?.power = ramp
            robot.lBDrive?.power = ramp
            robot.lBDrive?.power = ramp
            robot.lBDrive?.power = ramp
            if (2 % ramp == 0.0) {
                ramp /= 2
            }
            sleep(60)
            println("Are you still there?")
            //yay you win
        }
    }
}