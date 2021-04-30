package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
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
    val wizard = TelemetryWizard(console)

    val hardware = ChoiVicoHardware()
    val robot = OdometryDriveMovement(console, hardware, this)
    val target = Coordinate()

    val ringDetector = RingDetector(150, 135, console)
    var position: RingDetector.RingPosition = RingDetector.RingPosition.NONE

    val goalDetector = CreamsicleGoalDetector(console)
    val turret = UltimateGoalAimer(console, goalDetector, hardware)

    override fun runOpMode() {
        /** START INIT */

        hardware.init(hardwareMap)

        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.cameraName = "Webcam main"
        opencv.onFirstFrame(ringDetector::init)
        opencv.onNewFrame(ringDetector::processFrame)
        opencv.init(hardwareMap)
        opencv.start()


//        val ringCamera = CameraWrap(cameraName = "Webcam 1", opencv, hardwareMap)
//        val aimCamera = CameraWrap(cameraName = "Webcam 2", opencv, hardwareMap)
//        val aimCamera = ringCamera
//        ringCamera.startWatching( onFirstFrame = ringDetector::init, onNewFrame = ringDetector::processFrame)

        wizard.newMenu("gameType", "Which kind of game is it?", listOf("Remote", "In-Person"), "alliance", true)
        wizard.newMenu("alliance", "What alliance are we on?", listOf("Red", "Blue"), "startPos")
        wizard.newMenu("startPos", "Which line are we starting on?", listOf("Closer to you", "Closer to the middle"), "ourWobble")
        wizard.newMenu("ourWobble", "Will we do our wobble", listOf("Yes", "No"), "starterStack")
        wizard.newMenu("starterStack", "Will we collect the starter stack", listOf("Yes", "No"), "powerShot")
        wizard.newMenu("powerShot", "Will we do the power shots?", listOf("Yes", "No"),"topGoal")
        wizard.newMenu("topGoal", "Will we shoot in the top goal?", listOf("Yes", "No"))
        wizard.newMenu("park", "Will we park?", listOf("Yes", "No"))

        wizard.summonWizard(gamepad1)

        console.display(1, "Initialize Complete")
        waitForStart()

        /** START AUTO */

        console.display(1, "Start Auto")

        position = ringDetector.position

        opencv.optimizeView = false
        opencv.openCameraDeviceAsync = false
        opencv.cameraName = "turretWebcam"
        opencv.onNewFrame( goalDetector::scoopFrame )
        opencv.init(hardwareMap)
        opencv.start()

//        aimCamera.startWatching( onFirstFrame = null, onNewFrame = goalDetector::scoopFrame)


//        object: Thread() {
//            override fun run() {
//                while (opModeIsActive()) {
//                    turret.updateAimAndAdjustRobot()
//                    sleep(10)
//                }
//
//            }
//        }.start()


        if (wizard.wasItemChosen("gameType", "In-Person")) {
            if (wizard.wasItemChosen("alliance", "Blue")) {
                if (wizard.wasItemChosen("startPos", "Closer to you")) {
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



            if (position == RingDetector.RingPosition.NONE) {
                target.setCoordinate(0.0, 60.0, 0.0)
                robot.fineTunedGoToPos(target)
                sleep(1000)
                target.addCoordinate(36.0, 0.0, 0.0)
                robot.fineTunedGoToPos(target)
                sleep(1000)
                hardware.turret.targetPosition = 1
                hardware.turret.power = 1.0
                target.addCoordinate(0.0, 12.0, 0.0)
                robot.fineTunedGoToPos(target)
                sleep(1000)
            }
            if (position == RingDetector.RingPosition.ONE) {
                target.setCoordinate(0.0, 96.0, 0.0)
                robot.fineTunedGoToPos(target)
                sleep(1000)
                target.addCoordinate(36.0, -36.0, 0.0)
                robot.fineTunedGoToPos(target)
                sleep(1000)
                target.addCoordinate(0.0, 12.0, 0.0)
                robot.fineTunedGoToPos(target)
                sleep(1000)

            }
            if (position == RingDetector.RingPosition.FOUR) {
                target.setCoordinate(0.0, 114.0, 0.0)
                robot.fineTunedGoToPos(target)
                sleep(1000)
                target.addCoordinate(36.0, -54.0, 0.0)
                robot.fineTunedGoToPos(target)
                sleep(1000)
                target.addCoordinate(0.0, 12.0, 0.0)
                robot.fineTunedGoToPos(target)
                sleep(1000)
            }
        } else {
            console.display(3, "No remote auto")
        }

        console.display(1, "End Auto")
        /** END AUTO */
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
