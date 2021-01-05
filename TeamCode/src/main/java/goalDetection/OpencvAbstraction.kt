package goalDetection

import com.qualcomm.robotcore.util.Hardware
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvInternalCamera
import org.openftc.easyopencv.OpenCvPipeline

class CameraMaker {

    lateinit var camera: OpenCvInternalCamera
    private val cameraMonitorViewId: Int = BlocksOpModeCompanion.hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", BlocksOpModeCompanion.hardwareMap.appContext.packageName)

    fun create(direction: OpenCvInternalCamera.CameraDirection) {
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(direction, cameraMonitorViewId)
        camera.openCameraDevice()
    }

    fun setPipeline(pipeline: OpenCvPipeline) {
        camera.setPipeline(pipeline)
    }
}

class PipelineAbstraction: OpenCvPipeline() {
    val frame: Mat = Mat()
    var newFrame: Mat = Mat()

    override fun processFrame(input: Mat?): Mat {
        input?.copyTo(frame)

        if (frame.empty()) {
            return input!!
        }

        newFrame = frame

        return frame
    }

}

class OpencvAbstraction {

    val pipeline = PipelineAbstraction()

    lateinit var camera: OpenCvInternalCamera
    var cameraDirection: OpenCvInternalCamera.CameraDirection = OpenCvInternalCamera.CameraDirection.BACK

    fun init(cameraMonitorViewId: Int) {
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(cameraDirection, cameraMonitorViewId)
        camera.openCameraDevice()

        camera.setPipeline(pipeline)
    }

    fun start() {
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
    }

    val frame get() = pipeline.newFrame

    fun frame() = pipeline.newFrame
}