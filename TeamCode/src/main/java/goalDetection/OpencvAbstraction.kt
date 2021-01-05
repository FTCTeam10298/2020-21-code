package goalDetection

import android.os.SystemClock.sleep
import com.qualcomm.robotcore.eventloop.opmode.OpMode
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
    var rtn = Mat()

    override fun processFrame(input: Mat): Mat {
        input.copyTo(frame)

        if (frame.empty()) {
            return input
        }

        newFrame = frame

        if (rtn.empty())
            rtn = frame

        return rtn
    }

    fun setReturn(input: Mat) {
        if (!frame.empty())
            rtn = input
    }
}

class OpencvAbstraction(private val opmode: OpMode) {

    val pipeline = PipelineAbstraction()

    lateinit var camera: OpenCvInternalCamera
    var cameraDirection: OpenCvInternalCamera.CameraDirection = OpenCvInternalCamera.CameraDirection.BACK

    fun init() {

        val cameraMonitorViewId: Int = opmode.hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", opmode.hardwareMap.appContext.packageName)
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(cameraDirection, cameraMonitorViewId)
        camera.openCameraDevice()
        camera.setPipeline(pipeline)
    }

    fun start() {
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
        sleep(100)
    }


    val frame get() = pipeline.newFrame

    fun setReturn(input: Mat) {
        pipeline.setReturn(input)
    }
}
