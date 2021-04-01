package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import creamsicleGoalDetection.CreamsicleGoalDetector
import creamsicleGoalDetection.UltimateGoalAimer
import locationTracking.Coordinate
import openCvAbstraction.OpenCvAbstraction
import ringDetector.RingDetector
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole

@Autonomous(name="Auto ChoiVico", group="ChoiVico")
class ChoiVicoAuto: LinearOpMode() {

    val opencv = OpenCvAbstraction(this)

    val console = TelemetryConsole(telemetry)

    val hardware = ChoiVicoHardware()
    val robot = OdometryDriveMovement(console, hardware)
    val target = Coordinate()

    val ringDetector = RingDetector(150, 135, console)
    var position: RingDetector.RingPosition = RingDetector.RingPosition.NONE

    val goalDetector = CreamsicleGoalDetector(console)
    val turret = UltimateGoalAimer(console, robot, goalDetector)

    override fun runOpMode() {
        hardware.init(hardwareMap)

        opencv.init(hardwareMap)
        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.start()

        opencv.onFirstFrame{ ringDetector.init(it) }
        opencv.onNewFrame{ ringDetector.processFrame(it) }

        waitForStart()

        position = ringDetector.position

    }
}
