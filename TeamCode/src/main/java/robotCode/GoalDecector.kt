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

        val mask = colorMask()

        val contours: List<MatOfPoint> = listOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE)

        Imgproc.drawContours(frame, contours, -1, Scalar(0.0, 255.0, 0.0), 3)

        return frame
    }

    private fun colorMask(): Mat {
        val hsv = Mat()
        Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_RGB2HSV)

        val lowBlue = doubleArrayOf(94.0, 80.0, 2.0)
        val highBlue = doubleArrayOf(126.0, 255.0, 255.0)

        val mask = Mat()
        Core.inRange(hsv, Scalar(lowBlue), Scalar(highBlue), mask)

        return mask
    }
}



class ColorSorterThing(): OpenCvPipeline() {

    private val workingMatrix: Mat = Mat()

    override fun processFrame(input: Mat): Mat {

        input.copyTo(workingMatrix)

        if (workingMatrix.empty()) {
            return input
        }

        fun toIntArray(arg: DoubleArray): IntArray {
            val values: DoubleArray = arg
            var value: IntArray = intArrayOf()
            values.forEach {value += intArrayOf(it.toInt())}
            return value
        }
        fun simpleIntMat(/*vararg values: Int*/ values: DoubleArray):Mat {
            values.forEach { it.toInt() }
            val mat = Mat(1, values.size, CvType.CV_8UC3)
            mat.put(0, 1, toIntArray(values))
            return mat
        }
        fun toScalar(value: DoubleArray): Scalar = Scalar(value)

        val lowBlue = doubleArrayOf(94.0, 80.0, 2.0)
        val highBlue = doubleArrayOf(126.0, 255.0, 255.0)

        val hSVFrame = Mat()
        Imgproc.cvtColor(workingMatrix, hSVFrame, Imgproc.COLOR_RGB2HSV)

        val mask = Mat()
        Core.inRange(hSVFrame, toScalar(lowBlue), toScalar(highBlue), mask)

        val blue = Mat()
        Core.bitwise_and(simpleIntMat(lowBlue), simpleIntMat(highBlue), blue, mask)

        return blue
    }
}
