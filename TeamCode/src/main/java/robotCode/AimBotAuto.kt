package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import jamesTelemetryMenu.TelemetryMenu
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvInternalCamera
import robotCode.aimBotRobot.EncoderDriveMovement

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    var menu: TelemetryMenu = TelemetryMenu(telemetry)
    lateinit var phoneCam: OpenCvInternalCamera
    lateinit var pipeline: SkystoneDeterminationPipeline
    val robot= EncoderDriveMovement(menu)

    override fun runOpMode() {

        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId)
        pipeline = SkystoneDeterminationPipeline()
        phoneCam.setPipeline(pipeline)

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.
        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW)
        phoneCam.openCameraDeviceAsync(OpenCvCamera.AsyncCameraOpenListener { phoneCam.startStreaming(320, 240, OpenCvCameraRotation.SIDEWAYS_LEFT) })

        robot.init(hardwareMap)

        waitForStart()

        while (opModeIsActive()) {
            telemetry.addData("Analysis", pipeline!!.analysis)
            telemetry.addData("Position", pipeline!!.position)
            telemetry.update()

            // Don't burn CPU cycles busy-looping in this sample
            sleep(50)
        }

//        Wins 5 Pts.

        robot.shooter.power = 1.0
        robot.driveRobotPosition(1.0,73.0, true)
//        robot.driveRobotTurn(1.0, 360 * 1.0)

    }
}