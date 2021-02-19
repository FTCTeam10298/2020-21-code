package goalDetection

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.*
import kotlin.math.abs


class UGRectDetector {
    private var camera: OpenCvCamera? = null
    private var isUsingWebcam = false
    private var webcamName: String? = null
    private var hardwareMap: HardwareMap
    private lateinit var ftclibPipeline: UGRectRingPipeline

    //The constructor is overloaded to allow the use of webcam instead of the phone camera
    constructor(hMap: HardwareMap) {
        hardwareMap = hMap
    }

    constructor(hMap: HardwareMap, webcamName: String?) {
        hardwareMap = hMap
        isUsingWebcam = true
        this.webcamName = webcamName
    }

    fun init() {
        //This will instantiate an OpenCvCamera object for the camera we'll be using
        camera = if (isUsingWebcam) {
            val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
            OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName::class.java, webcamName), cameraMonitorViewId)
        } else {
            val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
            OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId)
        }

        //Set the pipeline the camera should use and start streaming
        camera!!.setPipeline(UGRectRingPipeline().also{ ftclibPipeline = it })
        camera!!.openCameraDeviceAsync{ camera!!.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT) }
    }

    fun setTopRectangle(topRectHeightPercentage: Double, topRectWidthPercentage: Double) {
        ftclibPipeline.setTopRectHeightPercentage(topRectHeightPercentage)
        ftclibPipeline.setTopRectWidthPercentage(topRectWidthPercentage)
    }

    fun setBottomRectangle(bottomRectHeightPercentage: Double, bottomRectWidthPercentage: Double) {
        ftclibPipeline.setBottomRectHeightPercentage(bottomRectHeightPercentage)
        ftclibPipeline.setBottomRectWidthPercentage(bottomRectWidthPercentage)
    }

    fun setRectangleSize(rectangleWidth: Int, rectangleHeight: Int) {
        ftclibPipeline.setRectangleHeight(rectangleHeight)
        ftclibPipeline.setRectangleWidth(rectangleWidth)
    }

    val stack: Stack
        get() = if (abs(ftclibPipeline.topAverage - ftclibPipeline.bottomAverage) < ftclibPipeline.threshold && ftclibPipeline.topAverage <= 100 && ftclibPipeline.bottomAverage <= 100) {
            Stack.FOUR
        } else if (abs(ftclibPipeline.topAverage - ftclibPipeline.bottomAverage) < ftclibPipeline.threshold && ftclibPipeline.topAverage >= 100 && ftclibPipeline.bottomAverage >= 100) {
            Stack.ZERO
        } else {
            Stack.ONE
        }

    fun setThreshold(threshold: Int) {
        ftclibPipeline.threshold = threshold
    }

    val topAverage: Double
        get() = ftclibPipeline.topAverage
    val bottomAverage: Double
        get() = ftclibPipeline.bottomAverage

    enum class Stack {
        ZERO, ONE, FOUR
    }
}


class UGRectRingPipeline : OpenCvPipeline() {
    //We declare the mats ontop so we can reuse them later to avoid memory leaks
    private val matYCrCb = Mat()
    private val matCbBottom = Mat()
    private val matCbTop = Mat()
    private var topBlock = Mat()
    private var bottomBlock = Mat()

    //Where the average CB value of the rectangles are stored
    var topAverage = 0.0
        private set
    var bottomAverage = 0.0
        private set

    //The max difference allowed inside the rectangles
    var threshold = 15

    //The position related to the screen
    private var topRectWidthPercentage = 0.25
    private var topRectHeightPercentage = 0.25
    private var bottomRectWidthPercentage = 0.25
    private var bottomRectHeightPercentage = 0.35

    //The width and height of the rectangles in terms of pixels
    private var rectangleWidth = 10
    private var rectangleHeight = 10
    override fun processFrame(input: Mat): Mat {
        /**
         * input which is in RGB is the frame the camera gives
         * We convert the input frame to the color space matYCrCb
         * Then we store this converted color space in the mat matYCrCb
         * For all the color spaces go to
         * https://docs.opencv.org/3.4/d8/d01/group__imgproc__color__conversions.html
         */
        Imgproc.cvtColor(input, matYCrCb, Imgproc.COLOR_RGB2YCrCb)

        //The points needed for the rectangles are calculated here
        val topRect = Rect(
                (matYCrCb.width() * topRectWidthPercentage).toInt(),
                (matYCrCb.height() * topRectHeightPercentage).toInt(),
                rectangleWidth,
                rectangleHeight
        )
        val bottomRect = Rect(
                (matYCrCb.width() * bottomRectWidthPercentage).toInt(),
                (matYCrCb.height() * bottomRectHeightPercentage).toInt(),
                rectangleWidth,
                rectangleHeight
        )

        //The rectangle is drawn into the mat
        drawRectOnToMat(input, topRect, Scalar(255.0, 0.0, 0.0))
        drawRectOnToMat(input, bottomRect, Scalar(0.0, 255.0, 0.0))

        //We crop the image so it is only everything inside the rectangles and find the cb value inside of them
        topBlock = matYCrCb.submat(topRect)
        bottomBlock = matYCrCb.submat(bottomRect)
        Core.extractChannel(bottomBlock, matCbBottom, 2)
        Core.extractChannel(topBlock, matCbTop, 2)

        //We take the average
        val bottomMean = Core.mean(matCbBottom)
        val topMean = Core.mean(matCbTop)
        bottomAverage = bottomMean.`val`[0]
        topAverage = topMean.`val`[0]

        //return the mat to be shown onto the screen
        return input
    }

    /**
     * Draw the rectangle onto the desired mat
     *
     * @param mat   The mat that the rectangle should be drawn on
     * @param rect  The rectangle
     * @param color The color the rectangle will be
     */
    private fun drawRectOnToMat(mat: Mat, rect: Rect, color: Scalar) {
        Imgproc.rectangle(mat, rect, color, 1)
    }

    fun setTopRectWidthPercentage(topRectWidthPercentage: Double) {
        this.topRectWidthPercentage = topRectWidthPercentage
    }

    fun setTopRectHeightPercentage(topRectHeightPercentage: Double) {
        this.topRectHeightPercentage = topRectHeightPercentage
    }

    fun setBottomRectWidthPercentage(bottomRectWidthPercentage: Double) {
        this.bottomRectWidthPercentage = bottomRectWidthPercentage
    }

    fun setBottomRectHeightPercentage(bottomRectHeightPercentage: Double) {
        this.bottomRectHeightPercentage = bottomRectHeightPercentage
    }

    fun setRectangleWidth(rectangleWidth: Int) {
        this.rectangleWidth = rectangleWidth
    }

    fun setRectangleHeight(rectangleHeight: Int) {
        this.rectangleHeight = rectangleHeight
    }
}