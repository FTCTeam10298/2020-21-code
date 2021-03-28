package creamsicleGoalDetection

import org.opencv.core.Mat
import robotCode.hardwareClasses.MecanumDriveTrain
import telemetryWizard.TelemetryConsole

class CreamsicleAutoAim(val console: TelemetryConsole, val drivetrain: MecanumDriveTrain) {
    enum class Directions {
        Right,
        Left,
        TargetAcquired
    }

    val goalTracking = CreamsicleGoalDetector(console)

    var turnDir: Directions? = null

    fun update(frame: Mat): Mat {
        console.display(1, "start")

        goalTracking.scoopFrame(frame)

        if (goalTracking.x < 245)
            turnDir = Directions.Right

        if (goalTracking.x > 245 && goalTracking.x < 255)
            turnDir = Directions.TargetAcquired

        else if (goalTracking.x >= 255)
            turnDir = Directions.Left

        console.display(7, "Turn direction: $turnDir")

        aimNoScope()

        return frame
    }

    fun aimNoScope() {

        console.display(12, turnDir.toString())

        if (turnDir == Directions.Left) {
            drivetrain.driveSetPower(-0.3, 0.3, -0.3, 0.3)
        }

        if (turnDir == Directions.Right) {
            drivetrain.driveSetPower(0.3, -0.3, 0.3, 0.3)
        }

    }
}