package robotCode

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
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId)
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

//        To be tuned
        val mask = colorMask(blurredFrame, doubleArrayOf(48.0, 86.0, 0.0), doubleArrayOf(121.0, 255.0, 255.0))

        val contours: MutableList<MatOfPoint> = mutableListOf()
        val hierarchy = Mat()
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE)

        for (contour in contours) {
            val area = Imgproc.contourArea(contour)
            if (area > 1000)
                Imgproc.drawContours(frame, listOf(contour), -1, Scalar(0.0, 255.0, 0.0), 1)
        }

        return frame
    }

    private fun colorMask(frame: Mat, lowValue: DoubleArray, highValue: DoubleArray): Mat {
        val hsv = Mat()
        Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_RGB2HSV)

        val mask = Mat()
        Core.inRange(hsv, Scalar(lowValue), Scalar(highValue), mask)

        return mask
    }
}
