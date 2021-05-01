package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import creamsicleGoalDetection.CreamsicleGoalDetector
import creamsicleGoalDetection.UltimateGoalAimer
import locationTracking.Coordinate
import openCvAbstraction.OpenCvAbstraction
import org.opencv.core.Mat
import org.opencv.core.Point
import ringDetector.RingDetector
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole
import telemetryWizard.TelemetryWizard

@Autonomous(name="Auto ChoiVico", group="ChoiVico")
class ChoiVicoAuto: LinearOpMode() {

    val opencv = OpenCvAbstraction(this)

    val console = TelemetryConsole(telemetry)
    val wizard = TelemetryWizard(console, this)

    val hardware = ChoiVicoHardware()
    val robot = OdometryDriveMovement(console, hardware, this)
    val target = Coordinate()

    val ringDetector = RingDetector(150, 135, console)
    var position: RingDetector.RingPosition = RingDetector.RingPosition.NONE

    val goalDetector = CreamsicleGoalDetector(console)
    val turret = UltimateGoalAimer(console, goalDetector, hardware)

    val highGoalPreset = 4550
    var shooterRpm: Double = highGoalPreset.toDouble()

    override fun runOpMode() {
        /** START INIT */

        hardware.init(hardwareMap)

        hardware.lift1.position = 0.33

        wizard.newMenu("gameType", "Which kind of game is it?", listOf("In-Person", "Remote"), "alliance", true)
        wizard.newMenu("alliance", "What alliance are we on?", listOf("Blue", "Red"), "startPos")
        wizard.newMenu("startPos", "Which line are we starting on?", listOf("Closer to you", "Closer to the middle"), "ourWobble")
        wizard.newMenu("ourWobble", "Will we do our wobble", listOf("Yes", "No"), "topGoal")
        wizard.newMenu("topGoal", "Will we shoot in the top goal?", listOf("Yes", "No"), "park")
        wizard.newMenu("park", "Will we park?", listOf("Yes", "No"))

        wizard.summonWizard(gamepad1)

        console.display(1, "Initialize Complete")

//        Start Ring Detector
        ringDetector.REGION1_TOPLEFT_ANCHOR_POINT = if (wizard.wasItemChosen("startPos", "Closer to you"))
             Point(290.0, 197.0)
        else
            Point(50.0, 197.0)

        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.cameraName = hardware.turretCameraName
        opencv.init(hardwareMap)
        opencv.start()
        opencv.onFirstFrame(ringDetector::init)
        opencv.onNewFrame(ringDetector::processFrame)

        waitForStart()

        /** START AUTO */

        console.display(1, "Start Auto")

//        Store data and stop ring detector
        position = ringDetector.position
        opencv.stop()

        if (wizard.wasItemChosen("gameType", "In-Person")) {
            if (wizard.wasItemChosen("alliance", "Blue")) {
                if (wizard.wasItemChosen("startPos", "Closer to you")) {
//                    Set start position (0,0 is left bottom corner of field)
                    robot.globalRobot.setCoordinate(14.5, 0.0, 0.0)

                    if (wizard.wasItemChosen("topGoal", "Yes")) {

                        hardware.collector.power = 0.6

                        target.addCoordinate(10.0, 10.0)
                        robot.straightGoToPosition(target, 0.9, 6.0)

                        goToVelocity()

                        hardware.collector.power = 0.0

                        target.setCoordinate(13.0, 52.0, 10.0)
                        robot.straightGoToPosition(target, 0.9, 0.3)

                        hardware.transfer.power = 1.0
                        hardware.bottomTrans.power = 1.0

//        1st ring
                        hardware.lift1.position = 0.33
                        sleep(1000)

                        hardware.kniod.position = 1.0
                        sleep(400)
                        hardware.kniod.position = 0.5
                        sleep(400)

//        2nd ring
                        hardware.lift1.position = 0.48
                        sleep(2000)

                        for (i in (1..3)) {
                            hardware.kniod.position = 1.0
                            sleep(500)
                            hardware.kniod.position = 0.5
                            sleep(500)
                        }
                        sleep(400)

//        3rd ring
                        hardware.lift1.position = 0.6
                        sleep(2000)

                        for (i in (1..2)) {
                            hardware.kniod.position = 1.0
                            sleep(400)
                            hardware.kniod.position = 0.5
                            sleep(400)
                        }
                        sleep(400)

//        done shooting
                        idleShooter()

                        hardware.lift1.position = 0.0

                        hardware.transfer.power = 0.0
                        hardware.bottomTrans.power = 0.0

                    }

                } else if (wizard.wasItemChosen("startPos", "Closer to the middle")) {

//                    Set start position (0,0 is left bottom corner of field)
                    robot.globalRobot.setCoordinate(36.0, 0.0, 0.0) //smth else


                    if (wizard.wasItemChosen("topGoal", "Yes")) {

                        hardware.collector.power = 0.6

                        goToVelocity()

                        target.setCoordinate(36.0, 52.0, -8.0)
                        robot.straightGoToPosition(target, 0.9, 0.3)

                        hardware.collector.power = 0.0

                        hardware.transfer.power = 1.0
                        hardware.bottomTrans.power = 1.0

//        1st ring
                        hardware.lift1.position = 0.33
                        sleep(1000)

                        hardware.kniod.position = 1.0
                        sleep(400)
                        hardware.kniod.position = 0.5
                        sleep(400)

//        2nd ring
                        hardware.lift1.position = 0.48
                        sleep(2000)

                        for (i in (1..3)) {
                            hardware.kniod.position = 1.0
                            sleep(500)
                            hardware.kniod.position = 0.5
                            sleep(500)
                        }
                        sleep(400)

//        3rd ring
                        hardware.lift1.position = 0.6
                        sleep(2000)

                        for (i in (1..2)) {
                            hardware.kniod.position = 1.0
                            sleep(400)
                            hardware.kniod.position = 0.5
                            sleep(400)
                        }
                        sleep(400)

//        done shooting
                        idleShooter()

                        hardware.lift1.position = 0.0

                        hardware.transfer.power = 0.0
                        hardware.bottomTrans.power = 0.0

                    }

                }

                if (wizard.wasItemChosen("ourWobble", "Yes")) {

//                        deliver wobble

                    hardware.wobble.power = 0.2

                    when (position) {
                        RingDetector.RingPosition.FOUR -> {
                            target.setCoordinate(11.0, 92.0, 0.0)
                            robot.straightGoToPosition(target, 1.0, 0.2)

                            target.addCoordinate(r = -193.0)
                            robot.turnGoToPosition(target, 1.0, 0.5)
                        }
                        RingDetector.RingPosition.ONE -> {
                            target.setCoordinate(29.0, 79.0, 0.0)
                            robot.straightGoToPosition(target, 1.0, 0.2)

                            target.addCoordinate(r = -193.0)
                            robot.turnGoToPosition(target, 1.0, 0.5)
                        }
                        RingDetector.RingPosition.NONE -> {
                            target.setCoordinate(11.0, 66.0, -193.0)
                            robot.straightGoToPosition(target, 1.0, 0.3)

                            target.addCoordinate(r = -193.0)
                            robot.turnGoToPosition(target, 1.0, 0.5)
                        }
                    }
                    hardware.wobble.power = -0.5
                    sleep(1300)
                    hardware.wobble.power = 0.0

                    hardware.claw1.position = 0.5
                    hardware.claw2.position = 0.5

                    sleep(1000)

                }
                if (wizard.wasItemChosen("park", "Yes")) {
//        park
                    target.setCoordinate(11.0, 68.0, -193.0)
                    robot.straightGoToPosition(target, 1.0, 0.3)

                }
            } else {
                console.display(3, "No red auto")
            }

        } else {
            console.display(3, "No remote auto")
        }

        console.display(1, "End Auto")
        /** END AUTO */
    }

    fun goToVelocity() {
        hardware.shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER;
        hardware.shooter.setVelocityPIDFCoefficients(450.0, 20.0, 0.0, 0.0)
        hardware.shooter.velocity = (shooterRpm / 60.0 * 28)
    }

    fun idleShooter() {
        hardware.shooter.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        hardware.shooter.power = 0.3    // Idle Shooter
    }

    class CameraWrap(val cameraName:String,
                     val opencv:OpenCvAbstraction,
                     val hardwareMap: HardwareMap){
        private var hasInitialized = false

        fun startWatching(onFirstFrame:((Mat)->Unit)?, onNewFrame: (Mat)->Mat){
            if(!hasInitialized){
                hasInitialized = true

                opencv.cameraName = cameraName
                if(onFirstFrame!=null){
                    opencv.onFirstFrame(onFirstFrame)
                }
                opencv.onNewFrame(onNewFrame)
                opencv.init(hardwareMap)
                opencv.start()
            }
        }

    }
}
