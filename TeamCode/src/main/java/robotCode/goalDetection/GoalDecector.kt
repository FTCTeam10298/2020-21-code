package robotCode.goalDetection

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.*
import org.openftc.easyopencv.OpenCvCamera.AsyncCameraOpenListener
import robotCode.RingDeterminationPipeline

@TeleOp
class GoalDecector : LinearOpMode()  {

    lateinit var camera: OpenCvInternalCamera
    lateinit var pipeline: GoalDetector

    override fun runOpMode() {
        val cameraMonitorViewId: Int = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId)
        camera.openCameraDevice()
        camera.setPipeline(pipeline)
        camera.startStreaming(320, 240, OpenCvCameraRotation.SIDEWAYS_LEFT)

    }
}

class GoalDetector(val input: Mat): OpenCvPipeline() {

    private val workingMatrix: Mat = Mat()


    override fun processFrame(input: Mat): Mat {
        input.copyTo(workingMatrix)

        if (workingMatrix.empty()) {
            return input
        }

        Imgproc.cvtColor(workingMatrix, workingMatrix, Imgproc.COLOR_RGB2YCrCb)

        val mat: Mat = workingMatrix.submat(120, 150, 12, 50)

        val total: Double = Core.sumElems(mat).`val`[2]
        return workingMatrix
    }
}