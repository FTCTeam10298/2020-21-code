package robotCode

import android.os.SystemClock.sleep
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import jamesTelemetryMenu.TelemetryConsole
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.*

@TeleOp
class GoalTracker : LinearOpMode()  {

    lateinit var camera: OpenCvInternalCamera
    lateinit var pipeline: GoalDetector
    val console = TelemetryConsole(telemetry)

    override fun runOpMode() {
        val cameraMonitorViewId: Int = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.FRONT, cameraMonitorViewId)
        camera.openCameraDevice()
        pipeline = GoalDetector(console)
        camera.setPipeline(pipeline)
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
        waitForStart()

    }
}

class GoalDetector(private val console: TelemetryConsole): OpenCvPipeline() {

    private val frame: Mat = Mat()

    override fun processFrame(input: Mat): Mat {
        input.copyTo(frame)

        if (frame.empty()) {
            return input
        }

//        The actual code

        val blurredFrame = Mat()
        Imgproc.GaussianBlur(frame, blurredFrame, Size(5.0, 5.0), 0.0)

        val mask = colorMask(blurredFrame)

        val contours: List<MatOfPoint> = listOf()
        val hierarchy = Mat()
//        problematic:
//        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE)

        Imgproc.drawContours(frame, contours, -1, Scalar(0.0, 255.0, 0.0), 3)

        return frame
    }

    private fun colorMask(frame: Mat): Mat {
        val hsv = Mat()
        Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_RGB2HSV)

        val lowBlue = doubleArrayOf(94.0, 80.0, 2.0)
        val highBlue = doubleArrayOf(126.0, 255.0, 255.0)

        val mask = Mat()
        Core.inRange(hsv, Scalar(lowBlue), Scalar(highBlue), mask)

        return mask
    }
}
