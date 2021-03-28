package jamesGoalDetection

import android.os.SystemClock
import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import locationTracking.Coordinate
import robotCode.ChoiVicoHardware
import robotCode.RingDetector
import robotCode.hardwareClasses.MecanumDriveTrain
import robotCode.hardwareClasses.OdometryDriveMovement
import robotCode.odometryTestHardware
import telemetryWizard.TelemetryConsole
import kotlin.math.abs
import kotlin.math.absoluteValue

@TeleOp
class AutoAimTest: LinearOpMode() {

    val console = TelemetryConsole(telemetry)
    val hardware = odometryTestHardware()
    val robot = OdometryDriveMovement(console, hardware)
    val opencv = OpencvAbstraction(this)

    val turret = AutoAim(console, robot)
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