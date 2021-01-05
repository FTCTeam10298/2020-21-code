package goalDetection

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import jamesTelemetryMenu.TelemetryConsole
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Moments

@TeleOp
class GoalTracker : LinearOpMode()  {

    val console = TelemetryConsole(telemetry)

    val opencv = OpencvAbstraction(this)
    val goalDetector = GoalDetector()

    override fun runOpMode() {
        opencv.init()
        opencv.start()

        while (!isStarted) {
            goalDetector.detectTrapezoid(opencv.frame, gamepad1, gamepad2)
            opencv.setReturn(goalDetector.display)
        }

        waitForStart()

    }
}

class GoalDetector() {

    var display: Mat = Mat()

    fun detectTrapezoid(frame: Mat, gamepad1: Gamepad, gamepad2: Gamepad) {

//        To be tuned

        var lh = 0.0
        lh += gamepad1.left_stick_y.toDouble()

        var ls = 0.0
        ls += gamepad1.left_stick_x.toDouble()

        var lv = 0.0
        lv += gamepad1.right_stick_y.toDouble()

        var h = 0.0
        h += gamepad2.left_stick_y.toDouble()

        var s = 0.0
        s += gamepad2.left_stick_x.toDouble()

        var v = 0.0
        v += gamepad2.right_stick_y.toDouble()


        val lowBlue = doubleArrayOf(lh, ls, lv)
        doubleArrayOf(48.0, 86.0, 0.0)
        val highBlue = doubleArrayOf(h, s, v)
        doubleArrayOf(131.0, 155.0, 255.0)

        val mask = colorMask(frame, lowBlue, highBlue)

        val blurredMask = Mat()
        Imgproc.GaussianBlur(mask, blurredMask, Size(5.0, 5.0), 0.0)

//        val erodedMask = Mat()
//        Imgproc.erode(mask, erodedMask, Mat())

        val contours: MutableList<MatOfPoint> = mutableListOf()
        Imgproc.findContours(blurredMask, contours, Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

        val largeContours = contours.filter{ Imgproc.contourArea(it) > 500}
        val squareContours = contours.filter{ isSquare(it) }
        squareContours.forEach {

//            val x = it.row(0)
//            val y = it.row(1)
            Imgproc.drawContours(frame, listOf(it), 0, Scalar(0.0, 255.0, 0.0), 2)
            Imgproc.circle(frame, contourCenter(it), 3, Scalar(255.0, 255.0, 255.0), -1)
        }

//        val sdo = squareContours.maxWith{ Imgproc.contourArea(it).toDouble() }
//        val goal = squareContours.first{ Imgproc.contourArea(it) >  }

        display = blurredMask
    }

    private fun colorMask(frame: Mat, lowValue: DoubleArray, highValue: DoubleArray): Mat {
        val hsv = Mat()
        Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_RGB2HSV)

        val mask = Mat()
        Core.inRange(hsv, Scalar(lowValue), Scalar(highValue), mask)

        return mask
    }

    private fun smoothContour(contour: MatOfPoint): MatOfPoint {
        val cnt = contour.toMatOfPoint2f()

        val peri = Imgproc.arcLength(cnt, true)

        val smoothed = MatOfPoint2f()
        Imgproc.approxPolyDP(cnt, smoothed,0.01 * peri, true)


        return smoothed.toMatOfPoint()
    }

    private fun isSquare(contour: MatOfPoint): Boolean = smoothContour(contour).rows() == 4

    private fun contourCenter(contour: MatOfPoint): Point {
        val m: Moments = Imgproc.moments(contour)
        val cX = m._m10 / m._m00
        val cY = m._m01 / m._m00

        return Point(cX, cY)
    }
}

private fun MatOfPoint.toMatOfPoint2f(): MatOfPoint2f {
    val matOfPoint2f = MatOfPoint2f()
    this.convertTo(matOfPoint2f, CvType.CV_32F)
    return matOfPoint2f
}

private fun MatOfPoint2f.toMatOfPoint(): MatOfPoint {
    val matOfPoint = MatOfPoint()
    this.convertTo(matOfPoint, CvType.CV_32S)
    return matOfPoint
}
