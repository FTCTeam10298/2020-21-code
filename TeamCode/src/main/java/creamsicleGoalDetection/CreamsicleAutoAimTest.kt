package creamsicleGoalDetection

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import locationTracking.Coordinate
import openCvAbstraction.OpenCvAbstraction
import ringDetector.RingDetector
import robotCode.hardwareClasses.OdometryDriveMovement
import robotCode.OdometryTestHardware
import telemetryWizard.TelemetryConsole

@TeleOp(name="James Auto Aim Test", group="Tests")
class CreamsicleAutoAimTest: LinearOpMode() {

    val console = TelemetryConsole(telemetry)
    val hardware = OdometryTestHardware()
    val robot = OdometryDriveMovement(console, hardware)
    val opencv = OpenCvAbstraction(this)

    val turret = CreamsicleAutoAim(console, robot)
    var position: RingDetector.RingPosition = RingDetector.RingPosition.NONE

    val ringDetector = RingDetector(150, 135, console)

    val target = Coordinate()

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

        opencv.onNewFrame { turret.update(it) }

        target.setCoordinate(x = 0.0, y = 24.0, r = 0.0)
        robot.straightGoToPosition(target,1.0,0.5,this)

        target.setCoordinate(x = 10.0, y = 0.0, r = 0.0)
        robot.straightGoToPosition(target,1.0,0.5,this)
    }
}