package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    val robot= AimBotMovement()

    override fun runOpMode() {

        robot.init(hardwareMap)

        waitForStart()
//        robot.side(1.0, 300)
//        robot.straight( 1.0, 2750)

//        robot.eStrafe(1.0, 10298)
        robot.eStraight(1.0, 100)
    }
}