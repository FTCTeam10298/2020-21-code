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

//        Start Ring Detector
        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.cameraName = hardware.ringDetectionCameraName
        opencv.init(hardwareMap)
        opencv.start()
        opencv.onFirstFrame(ringDetector::init)
        opencv.onNewFrame(ringDetector::processFrame)


//        val ringCamera = CameraWrap(cameraName = "Webcam 1", opencv, hardwareMap)
//        val aimCamera = CameraWrap(cameraName = "Webcam 2", opencv, hardwareMap)
//        val aimCamera = ringCamera
//        ringCamera.startWatching( onFirstFrame = ringDetector::init, onNewFrame = ringDetector::processFrame)

        wizard.newMenu("gameType", "Which kind of game is it?", listOf("Remote", "In-Person"), "alliance", true)
        wizard.newMenu("alliance", "What alliance are we on?", listOf("Red", "Blue"), "startPos")
        wizard.newMenu("startPos", "Which line are we starting on?", listOf("Closer to you", "Closer to the middle"), "ourWobble")
        wizard.newMenu("ourWobble", "Will we do our wobble", listOf("Yes", "No"), "starterStack")
        wizard.newMenu("starterStack", "Will we collect the starter stack", listOf("Yes", "No"), "topGoal")
        wizard.newMenu("topGoal", "Will we shoot in the top goal?", listOf("Yes", "No"))
        wizard.newMenu("park", "Will we park?", listOf("Yes", "No"))

//        wizard.summonWizard(gamepad1)

        console.display(1, "Initialize Complete")

        waitForStart()

        /** START AUTO */

        console.display(1, "Start Auto")

//        Store data and stop ring detector
        position = ringDetector.position
//        opencv.stop()

//        Start Turret Vision
//        opencv.optimizeView = false
//        opencv.openCameraDeviceAsync = false
//        opencv.cameraName = hardware.turretCameraName
//        opencv.init(hardwareMap)
//        opencv.start()
//        opencv.onNewFrame( goalDetector::scoopFrame )

//        aimCamera.startWatching( onFirstFrame = null, onNewFrame = goalDetector::scoopFrame)

//        Auto Aim

//        object: Thread() {
//            override fun run() {
//                while (opModeIsActive()) {
//                    turret.updateAimAndAdjustRobot()
//                    sleep(10)
//                }
//
//            }
//        }.start()

        robot.globalRobot.setCoordinate(14.5, 0.0, 0.0)

        hardware.collector.power = 0.6

        target.addCoordinate(10.0, 10.0)
        robot.straightGoToPosition(target, 0.9, 4.0)

        goToVelocity()

        hardware.collector.power = 0.0

        target.setCoordinate(13.0, 52.0, 10.0)
        robot.straightGoToPosition(target, 0.9, 0.3)

//        1st ring
        hardware.lift1.position = 0.33
        sleep(1000)

        for (i in (1..2)) {
            hardware.kniod.position = 1.0
            sleep(400)
            hardware.kniod.position = 0.5
            sleep(400)
        }
        sleep(500)

//        2nd ring
        hardware.lift1.position = 0.47
        sleep(2000)

        for (i in (1..2)) {
            hardware.kniod.position = 1.0
            sleep(400)
            hardware.kniod.position = 0.5
            sleep(500)
        }
        sleep(500)

//        3rd ring
        hardware.lift1.position = 0.6
        sleep(3000)

        for (i in (1..2)) {
            hardware.kniod.position = 1.0
            sleep(500)
            hardware.kniod.position = 0.5
            sleep(500)
        }
        sleep(500)

        idleShooter()

        target.setCoordinate(8.0, 97.0, 0.0)
        robot.straightGoToPosition(target, 0.9, 0.1)

        target.setCoordinate(8.0, 97.0, 180.0)
        robot.turnGoToPosition(target, 0.9, 0.1)

        sleep(2000)

        if (wizard.wasItemChosen("gameType", "In-Person")) {
            if (wizard.wasItemChosen("alliance", "Blue")) {
                if (wizard.wasItemChosen("startPos", "Closer to you")) {
//                    Set start position (0,0 is left bottom corner of field)
//                    robot.globalRobot.setCoordinate(14.5, 0.0, 0.0)

                    if (wizard.wasItemChosen("ourWobble", "Yes")) {
                        if (wizard.wasItemChosen("starterStack", "Yes")) {
                            if (wizard.wasItemChosen("powerShot", "Yes")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {
//                                      IMPORTANT
                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }
                            } else if (wizard.wasItemChosen("powerShot", "No")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }
                            }
                        } else if (wizard.wasItemChosen("starterStack", "No")) {
                            if (wizard.wasItemChosen("powerShot", "Yes")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }

                            } else if (wizard.wasItemChosen("powerShot", "No")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }

                            }
                        }
                    } else if (wizard.wasItemChosen("ourWobble", "No")) {
                        if (wizard.wasItemChosen("starterStack", "Yes")) {
                            if (wizard.wasItemChosen("powerShot", "Yes")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }
                            } else if (wizard.wasItemChosen("powerShot", "No")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }
                            }
                        } else if (wizard.wasItemChosen("starterStack", "No")) {
                            if (wizard.wasItemChosen("powerShot", "Yes")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }
                            } else if (wizard.wasItemChosen("powerShot", "No")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {
                                        console.display(3, "That's nothing, just chilling")
                                    }
                                }
                            }
                        }
                    }
                } else if (wizard.wasItemChosen("startPos", "Closer to the middle")) {
                    if (wizard.wasItemChosen("ourWobble", "Yes")) {
                        if (wizard.wasItemChosen("starterStack", "Yes")) {
                            if (wizard.wasItemChosen("powerShot", "Yes")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }
                            } else if (wizard.wasItemChosen("powerShot", "No")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }
                            }
                        } else if (wizard.wasItemChosen("starterStack", "No")) {
                            if (wizard.wasItemChosen("powerShot", "Yes")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }
                            } else if (wizard.wasItemChosen("powerShot", "No")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }
                            }
                        }
                    } else if (wizard.wasItemChosen("ourWobble", "No")) {
                        if (wizard.wasItemChosen("starterStack", "Yes")) {
                            if (wizard.wasItemChosen("powerShot", "Yes")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }
                            } else if (wizard.wasItemChosen("powerShot", "No")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }
                            }
                        } else if (wizard.wasItemChosen("starterStack", "No")) {
                            if (wizard.wasItemChosen("powerShot", "Yes")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                }
                            } else if (wizard.wasItemChosen("powerShot", "No")) {
                                if (wizard.wasItemChosen("topGoal", "Yes")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {

                                    }
                                } else if (wizard.wasItemChosen("topGoal", "No")) {
                                    if (wizard.wasItemChosen("park", "Yes")) {

                                    } else if (wizard.wasItemChosen("park", "No")) {
                                        console.display(3, "That's nothing, just chilling")
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                console.display(3, "No red auto")
            }

        } else {
            console.display(3, "No remote auto")
        }


//        if (position == RingDetector.RingPosition.NONE) {
//            target.setCoordinate(0.0, 60.0, 0.0)
//            robot.fineTunedGoToPos(target)
//            sleep(1000)
//            target.addCoordinate(36.0, 0.0, 0.0)
//            robot.fineTunedGoToPos(target)
//            sleep(1000)
//            hardware.turret.targetPosition = 1
//            hardware.turret.power = 1.0
//            target.addCoordinate(0.0, 12.0, 0.0)
//            robot.fineTunedGoToPos(target)
//            sleep(1000)
//        }
//        if (position == RingDetector.RingPosition.ONE) {
//            target.setCoordinate(0.0, 96.0, 0.0)
//            robot.fineTunedGoToPos(target)
//            sleep(1000)
//            target.addCoordinate(36.0, -36.0, 0.0)
//            robot.fineTunedGoToPos(target)
//            sleep(1000)
//            target.addCoordinate(0.0, 12.0, 0.0)
//            robot.fineTunedGoToPos(target)
//            sleep(1000)
//
//        }
//        if (position == RingDetector.RingPosition.FOUR) {
//            target.setCoordinate(0.0, 114.0, 0.0)
//            robot.fineTunedGoToPos(target)
//            sleep(1000)
//            target.addCoordinate(36.0, -54.0, 0.0)
//            robot.fineTunedGoToPos(target)
//            sleep(1000)
//            target.addCoordinate(0.0, 12.0, 0.0)
//            robot.fineTunedGoToPos(target)
//            sleep(1000)
//        }

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
