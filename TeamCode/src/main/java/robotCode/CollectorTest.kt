package robotCode

import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

class CollectorHardware {

    lateinit var collector: DcMotor
    lateinit var hwMap: HardwareMap

    fun init(ahwMap: HardwareMap) {

        hwMap = ahwMap

        //        COLLECTOR
        collector = hwMap.get("collector") as DcMotor

        collector.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        collector.direction = DcMotorSimple.Direction.FORWARD
    }
}

@TeleOp
class CollectorTest: OpMode() {

    val robot = CollectorHardware()
    val collectorHelp1 = ButtonHelper()
    val collectorHelp2 = ButtonHelper()

    override fun init() {
        robot.init(hardwareMap)
    }

    override fun loop() {
        //        COLLECTOR
        if (collectorHelp1.stateChanged(gamepad1.right_bumper) && (gamepad1.right_bumper))
            if (robot.collector.power == 1.0)
                robot.collector.power = 0.0
            else
                robot.collector.power = 1.0
        else if (collectorHelp2.stateChanged(gamepad1.left_bumper) && (gamepad1.left_bumper))
            if (robot.collector.power == -1.0)
                robot.collector.power = 0.0
            else
                robot.collector.power = -1.0
    }

}