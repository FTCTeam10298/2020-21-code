package robotCode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import jamesTelemetryMenu.TelemetryConsole
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Moments
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


//        To be tuned
        val mask = colorMask(frame, doubleArrayOf(48.0, 86.0, 0.0), doubleArrayOf(131.0, 155.0, 255.0))

        val blurredFrame = Mat()
        Imgproc.GaussianBlur(mask, blurredFrame, Size(5.0, 5.0), 0.0)

        val contours: MutableList<MatOfPoint> = mutableListOf()
        val hierarchy = Mat()
        Imgproc.findContours(blurredFrame, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE)

        for (contour in contours) {
            Imgproc.drawContours(frame, findSquares(contour), 0, Scalar(0.0, 255.0, 0.0), 2)
            Imgproc.circle(frame, contourCenter(contour), 3, Scalar(255.0, 255.0, 255.0), -1)
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

    private fun findSquares(contour: MatOfPoint): List<MatOfPoint> {
        val c = MatOfPoint2f()
        contour.convertTo(c, CvType.CV_32F)

        val peri = Imgproc.arcLength(c, true)
        val approx = MatOfPoint2f()
        Imgproc.approxPolyDP(c, approx,0.04 * peri, true)

        val a = MatOfPoint()
        approx.convertTo(a, CvType.CV_32S)
        return listOf(a)
    }

    private fun contourCenter(contour: MatOfPoint): Point {
        val m: Moments = Imgproc.moments(contour)
        val cX = m._m10 / m._m00
        val cY = m._m01 / m._m00

        return Point(cX, cY)
    }
}
