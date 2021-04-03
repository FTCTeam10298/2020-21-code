package creamsicleGoalDetection

import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import openCvAbstraction.OpenCvAbstraction
import org.opencv.imgproc.Imgproc
import robotCode.ChoiVicoHardware
import robotCode.hardwareClasses.EncoderDriveMovement
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole


@Autonomous(name="Creamsicle Test and Calibration", group="Tests")
class CreamsicleAutoAimTestAndCal : OpMode() {
    val hardware = ChoiVicoHardware()
    val console = TelemetryConsole(telemetry)
    val robot = OdometryDriveMovement(console, hardware)
    val opencv = OpenCvAbstraction(this)
    val goalDetector = CreamsicleGoalDetector(console)
    val aimer = UltimateGoalAimer(console, goalDetector, hardware)
    val movement = EncoderDriveMovement(hardware, console)
    val font = Imgproc.FONT_HERSHEY_COMPLEX
    val XbuttonHelper = ButtonHelper()
    val YbuttonHelper = ButtonHelper()
    val DpadHelper = ButtonHelper()
    val RbumperHelper = ButtonHelper()
    val LbumperHelper = ButtonHelper()
    val AbuttonHelper = ButtonHelper()

    override fun init() {
        hardware.init(hardwareMap)
        opencv.init(hardwareMap)
        opencv.start()
        opencv.onNewFrame(goalDetector::scoopFrame)
    }

    private var varBeingEdited: CreamsicleGoalDetector.NamedVar = goalDetector.L_H
    fun render() {
        console.display(2, "Active Var; ${varBeingEdited.name}")
        console.display(4, "${varBeingEdited.value}")
    }

    override fun init_loop() {
        if (XbuttonHelper.stateChanged(gamepad1.x) && gamepad1.x) {
            console.display(3, "TrainerMODE; ${goalDetector.displayMode}")
            when (goalDetector.displayMode) {
                "frame" -> goalDetector.displayMode = "mask"
                "mask" -> goalDetector.displayMode = "kernel"
                "kernel" -> goalDetector.displayMode = "frame"
            }
            render()
        }


        when {
            DpadHelper.stateChanged(gamepad1.dpad_left) && gamepad1.dpad_left -> {
                when (varBeingEdited) {
                    goalDetector.L_H -> varBeingEdited = goalDetector.L_S
                    goalDetector.L_S -> varBeingEdited = goalDetector.L_V
                    goalDetector.L_V -> varBeingEdited = goalDetector.U_H
                    goalDetector.U_H -> varBeingEdited = goalDetector.U_S
                    goalDetector.U_S -> varBeingEdited = goalDetector.U_V
                    goalDetector.U_V -> varBeingEdited = goalDetector.L_H
                }
                render()
            }
        }

        if (YbuttonHelper.stateChanged(gamepad1.y) && gamepad1.y) {
            console.display(1, "Vals Zeroed")
            goalDetector.L_H.value = 0.0
            goalDetector.L_S.value = 0.0
            goalDetector.L_V.value = 0.0
            goalDetector.U_H.value = 0.0
            goalDetector.U_S.value = 0.0
            goalDetector.U_V.value = 0.0
            render()
        }

        if (RbumperHelper.stateChanged(gamepad1.right_bumper) && gamepad1.right_bumper) {
            varBeingEdited.value += 5
            render()
        }
        if (LbumperHelper.stateChanged(gamepad1.left_bumper) && gamepad1.left_bumper) {
            varBeingEdited.value -= 5
            render()
        }

        if (AbuttonHelper.stateChanged(gamepad1.a) && gamepad1.a) {
            goalDetector.L_H.value = 0.0
            goalDetector.L_S.value = 0.0
            goalDetector.L_V.value = 0.0
            goalDetector.U_H.value = 255.0
            goalDetector.U_S.value = 255.0
            goalDetector.U_V.value = 255.0
            console.display(1, "Vals Squonked")
            render()
        }
    }

    override fun loop() {
        aimer.updateAimAndAdjustRobot()
    }
}