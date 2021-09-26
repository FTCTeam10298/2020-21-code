package us.brainstormz.choivico.ringDetector

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import us.brainstormz.choivico.openCvAbstraction.OpenCvAbstraction
import us.brainstormz.choivico.telemetryWizard.TelemetryConsole

@TeleOp(name="Ring Detector Test", group="Tests")
class RingDetectorTest: LinearOpMode()  {

    val console = TelemetryConsole(telemetry)

    val opencv = OpenCvAbstraction(this)
    val ringDetector = RingDetector(150, 135, console)

    override fun runOpMode() {
        opencv.init(hardwareMap)
        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.start()

        waitForStart()
        opencv.onFirstFrame{ ringDetector.init(it) }
        opencv.onNewFrame { ringDetector.processFrame(it)}

        if (ringDetector.position == RingDetector.RingPosition.FOUR)
            while (opModeIsActive()) {
                telemetry.addData("Analysis", ringDetector.analysis)
                telemetry.addData("Position", ringDetector.position)
                telemetry.update()
            }
    }
}