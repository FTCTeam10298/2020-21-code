package goalDetection

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import jamesTelemetryMenu.TelemetryConsole
import locationTracking.Coordinate

@TeleOp
class AutoAim(private val console: TelemetryConsole, opMode: OpMode) {

    val detector = GoalDetector(console)
    val opencv = OpencvAbstraction(opMode)
    var aimed = false
    val coordinate = Coordinate()

    fun init() {
        opencv.init()
        opencv.start()
    }

    fun aim() {

        while (!aimed) {
            detector.detectTrapezoid(opencv.frame)
            detector.goal
            coordinate.
        }

    }

}