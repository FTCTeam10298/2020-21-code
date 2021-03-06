package goalDetection2

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import goalDetection.OpencvAbstraction
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.openftc.easyopencv.OpenCvCameraFactory
import robotCode.RingDetector

@Autonomous
class CreamsicleOpMode() : OpMode() {

    val opencv = OpencvAbstraction(this)


    override fun init() {
        opencv.init(hardwareMap)
        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.start()
        opencv.onNewFrame(::scoopFrame)
    }

    override fun loop() {


    }
}