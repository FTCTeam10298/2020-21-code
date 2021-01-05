package goalDetection

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import jamesTelemetryMenu.TelemetryConsole
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Moments

@TeleOp
class GoalTracker : LinearOpMode()  {

    val console = TelemetryConsole(telemetry)

    val opencv = OpencvAbstraction()
    val goalDetector = GoalDetector(console)

    override fun runOpMode() {
        val cameraMonitorViewId: Int = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        opencv.init(cameraMonitorViewId)
        opencv.start()

        while (true) {
            console.display(1, opencv.pipeline.rtn.empty().toString() + "hi")
            goalDetector.detectTrapezoid(opencv.frame)
        }

        waitForStart()

    }
}

class GoalDetector(private val console: TelemetryConsole) {

    fun detectTrapezoid(frame: Mat)/*: Mat*/ {

        val blurredFrame = Mat()
        Imgproc.GaussianBlur(frame, blurredFrame, Size(5.0, 5.0), 0.0)

//        To be tuned
        val lowBlue = doubleArrayOf(48.0, 86.0, 0.0)
        val highBlue = doubleArrayOf(131.0, 155.0, 255.0)

        val mask = colorMask(blurredFrame, lowBlue, highBlue)

        val contours: MutableList<MatOfPoint> = mutableListOf()
        Imgproc.findContours(mask, contours, Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE)

        val largeContours = contours/*.filter{Imgproc.contourArea(it) > 100}*/
        largeContours.forEach{ Imgproc.drawContours(frame, listOf(it), 0, Scalar(0.0, 255.0, 0.0), 2) }

        val squareContours = largeContours.filter{isSquare(it)}
        squareContours.forEach{ Imgproc.circle(frame, contourCenter(it), 3, Scalar(255.0, 255.0, 255.0), -1) }

//        return mask
    }

    private fun colorMask(frame: Mat, lowValue: DoubleArray, highValue: DoubleArray): Mat {
        val hsv = Mat()
        Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_RGB2HSV)

        val mask = Mat()
        Core.inRange(hsv, Scalar(lowValue), Scalar(highValue), mask)

        return mask
    }

    private fun isSquare(contour: MatOfPoint): Boolean {
        val cnt = MatOfPoint2f()
        contour.convertTo(cnt, CvType.CV_32F)

        val peri = Imgproc.arcLength(cnt, true)
        val approx = MatOfPoint2f()
        Imgproc.approxPolyDP(cnt, approx,0.01 * peri, true)

        val a = MatOfPoint()
        approx.convertTo(a, CvType.CV_32S)

        return a.rows() == 4
    }

    private fun contourCenter(contour: MatOfPoint): Point {
        val m: Moments = Imgproc.moments(contour)
        val cX = m._m10 / m._m00
        val cY = m._m01 / m._m00

        return Point(cX, cY)
    }
}
