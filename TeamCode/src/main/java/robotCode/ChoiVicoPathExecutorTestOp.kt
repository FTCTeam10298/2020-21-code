package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import us.brainstormz.localization.OdometryLocalizer
import us.brainstormz.motion.MecanumDifferentialDrivePlatform
import us.brainstormz.path.FollowOneWall
import us.brainstormz.pathexecution.DifferentialDrivePathExecutor
import us.brainstormz.pathexecution.PathExecutor


@Autonomous(name="PathExecutorTest", group="test")
class ChoiVicoPathExecutorTestOp: LinearOpMode() {

    val hardware = ChoiVicoHardware()

    override fun runOpMode() {
        /** START INIT */

        hardware.init(hardwareMap)

        val pathExecutor:PathExecutor = DifferentialDrivePathExecutor(
            localizer = OdometryLocalizer(
                lOdom = hardware.lOdom,
                rOdom = hardware.rOdom,
                cOdom = hardware.cOdom
            ),
            drivePlatform = MecanumDifferentialDrivePlatform(hardware = hardware)
        )

        waitForStart()

        /** Start Code */
        val testPath = FollowOneWall()
        val pathResults = pathExecutor.doPath(testPath)

        while(!pathResults.isDone){
            Thread.sleep(100)
        }
    }

}
