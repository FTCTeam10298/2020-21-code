package robotCode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import jamesGoalDetection.OpencvAbstraction
import telemetryWizard.TelemetryConsole
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

// CURRENT VERSION

@TeleOp(name="Ring Detector Test", group="Tests")
class RingDetectorTest: LinearOpMode()  {

    val console = TelemetryConsole(telemetry)

    val opencv = OpencvAbstraction(this)
    val ringDetector = RingDetector(150, 135, console)

    override fun runOpMode() {
        opencv.init(hardwareMap)
        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.start()

        waitForStart()
        opencv.onFirstFrame{ ringDetector.init(it) }

        if (ringDetector.position == RingDetector.RingPosition.FOUR)
        while (opModeIsActive()) {
            telemetry.addData("Analysis", ringDetector.analysis)
            telemetry.addData("Position", ringDetector.position)
            telemetry.update()

            opencv.onNewFrame { ringDetector.processFrame(it)}

        }
    }
}

class RingDetector(val FOUR_RING_THRESHOLD : Int, val ONE_RING_THRESHOLD : Int, private val console: TelemetryConsole) {
    /*
     * An enum to define the ring position
     */
    enum class RingPosition {
        FOUR, ONE, NONE
    }

    //val FOUR_RING_THRESHOLD = 150
    //val ONE_RING_THRESHOLD = 135
    var region1_pointA = Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x,
            REGION1_TOPLEFT_ANCHOR_POINT.y)
    var region1_pointB = Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT)

    /*
     * Working variables
     */
    var region1_Cb: Mat? = null
    var YCrCb = Mat()
    var Cb = Mat()
    var analysis = 0

    // Volatile since accessed by OpMode thread w/o synchronization
    @Volatile
    var position = RingPosition.FOUR

    /*
     * This function takes the RGB frame, converts to YCrCb,
     * and extracts the Cb channel to the 'Cb' variable
     */
    fun inputToCb(input: Mat?) {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb)
        Core.extractChannel(YCrCb, Cb, 1)
    }

    fun init(firstFrame: Mat) {
        inputToCb(firstFrame)
        region1_Cb = Cb.submat(Rect(region1_pointA, region1_pointB))
    }


    fun processFrame(input: Mat): Mat {
        console.display(1, position.toString())
        inputToCb(input)
        analysis = Core.mean(region1_Cb).`val`[0].toInt()
        Imgproc.rectangle(
                input,  // Buffer to draw on
                region1_pointA,  // First point which defines the rectangle
                region1_pointB,  // Second point which defines the rectangle
                BLUE,  // The color the rectangle is drawn in
                2) // Thickness of the rectangle lines
        position = RingPosition.FOUR // Record our analysis
        position = when {
            analysis > FOUR_RING_THRESHOLD -> {
                RingPosition.FOUR
            }
            analysis > ONE_RING_THRESHOLD -> {
                RingPosition.ONE
            }
            else -> {
                RingPosition.NONE
            }
        }
        Imgproc.rectangle(
                input,  // Buffer to draw on
                region1_pointA,  // First point which defines the rectangle
                region1_pointB,  // Second point which defines the rectangle
                TRANSPARENT,  // The color the rectangle is drawn in
                1) // Negative thickness means solid fill
        return input
    }

    companion object {
        /*
     * Some color constants
     */
        val BLUE = Scalar(0.0, 0.0, 255.0)
        val TRANSPARENT = Scalar(0.0, 255.0, 0.0)

        /*
     * The core values which define the location and size of the sample regions
     */
//        Camera is landscape top left is 0,0
        val REGION1_TOPLEFT_ANCHOR_POINT = Point(220.0, 197.0)
        const val REGION_WIDTH = 35
        const val REGION_HEIGHT = 25
    }
}
