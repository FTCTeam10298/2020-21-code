package goalDetection

import android.os.SystemClock.sleep
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcontroller.external.samples.ConceptWebcam
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.internal.system.Deadline
import org.opencv.core.Mat
import org.openftc.easyopencv.*
import java.util.concurrent.TimeUnit

//class CameraMaker {
//
//    lateinit var camera: OpenCvInternalCamera
//    private val cameraMonitorViewId: Int = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
//
//    fun create(direction: OpenCvInternalCamera.CameraDirection) {
//        camera = OpenCvCameraFactory.getInstance().createInternalCamera(direction, cameraMonitorViewId)
//        camera.openCameraDevice()
//    }
//
//    fun setPipeline(pipeline: OpenCvPipeline) {
//        camera.setPipeline(pipeline)
//    }
//}

class PipelineAbstraction: OpenCvPipeline() {
    var isFirstFrame = true
    private val frame: Mat = Mat()
    
    var userFun: (Mat) -> Mat = {it}
    var onFirstFrame: ((Mat) -> Unit)? = null

    override fun processFrame(input: Mat): Mat {
        input.copyTo(frame)

        if (frame.empty()) {
            return input
        }

        return if (onFirstFrame != null && isFirstFrame) {
            isFirstFrame = false
            onFirstFrame?.invoke(frame)
            input
        } else {
            userFun(frame)
        }

    }

}

class OpencvAbstraction(private val opmode: OpMode) {

    private val pipeline = PipelineAbstraction()
    val frame = Mat()

    private lateinit var cameraManager: CameraManager
    private lateinit var camera: org.firstinspires.ftc.robotcore.external.hardware.camera
    private lateinit var webcamName: WebcamName
    var cameraDirection: OpenCvInternalCamera.CameraDirection = OpenCvInternalCamera.CameraDirection.FRONT
    var optimizeView = false
    var openCameraDeviceAsync = false


    fun init() {

        cameraManager = ClassFactory.getInstance().cameraManager

        val cameraMonitorViewId: Int = opmode.hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", opmode.hardwareMap.appContext.packageName)

        webcamName = hardwareMap.get(WebcamName::class.java, "Webcam 1")
        val deadline = Deadline(ConceptWebcam.secondsPermissionTimeout.toLong(), TimeUnit.SECONDS)
        camera = cameraManager.requestPermissionAndOpenCamera(deadline, cameraName, null);
        camera.setPipeline(pipeline)
    }

    fun start() {
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
        sleep(100)

        if (optimizeView)
            camera.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW)

        if (openCameraDeviceAsync)
            camera.openCameraDeviceAsync(OpenCvCamera.AsyncCameraOpenListener { camera.startStreaming(320, 240, OpenCvCameraRotation.SIDEWAYS_LEFT) })
    }

    fun stop() {
        camera.stopStreaming()
    }

    fun onFirstFrame(function: (Mat) -> Unit) {
        pipeline.onFirstFrame = function
    }

    fun onNewFrame(function: (Mat) -> Mat) {
        pipeline.userFun = function
    }
}
