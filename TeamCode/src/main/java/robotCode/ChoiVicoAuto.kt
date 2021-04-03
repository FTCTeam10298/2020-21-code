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

    val hardware = OdometryTestHardware()
    val robot = OdometryDriveMovement(console, hardware)
    val target = Coordinate()

    val ringDetector = RingDetector(150, 135, console)
    var position: RingDetector.RingPosition = RingDetector.RingPosition.NONE

    val goalDetector = CreamsicleGoalDetector(console)
    val turret = UltimateGoalAimer(console, robot, goalDetector)

    override fun runOpMode() {
        hardware.init(hardwareMap)

        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.cameraName = "Webcam 1"
        opencv.init(hardwareMap)
        opencv.start()

        opencv.onFirstFrame{ ringDetector.init(it) }
        opencv.onNewFrame{ ringDetector.processFrame(it) }

        waitForStart()

        opencv.cameraName = "Webcam 2"
        opencv.init(hardwareMap)
        opencv.start()
        
        opencv.onNewFrame{ turret.updateAimAndAdjustRobot() }

        position = ringDetector.position
        if(position == RingDetector.RingPosition.NONE){
            target.setCoordinate(0.0,60.0,0.0)
            robot.fineTunedGoToPos(target, this)
            sleep(1000)
            target.addCoordinate(36.0,0.0,0.0)
            robot.fineTunedGoToPos(target, this)
            sleep(1000)
            target.addCoordinate(0.0,12.0,0.0)
            robot.fineTunedGoToPos(target, this)
            sleep(1000)
        }
        if(position == RingDetector.RingPosition.ONE){
            target.setCoordinate(0.0,96.0,0.0)
            robot.fineTunedGoToPos(target, this)
            sleep(1000)
            target.addCoordinate(36.0,-36.0,0.0)
            robot.fineTunedGoToPos(target, this)
            sleep(1000)
            target.addCoordinate(0.0,12.0,0.0)
            robot.fineTunedGoToPos(target, this)
            sleep(1000)

        }
        if (position == RingDetector.RingPosition.FOUR){
            target.setCoordinate(0.0,114.0,0.0)
            robot.fineTunedGoToPos(target, this)
            sleep(1000)
            target.addCoordinate(36.0,-54.0,0.0)
            robot.fineTunedGoToPos(target, this)
            sleep(1000)
            target.addCoordinate(0.0,12.0,0.0)
            robot.fineTunedGoToPos(target, this)
            sleep(1000)
        }
    }
}
