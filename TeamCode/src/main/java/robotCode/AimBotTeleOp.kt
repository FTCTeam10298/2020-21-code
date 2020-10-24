package robotCode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name="Aim Bot Tele-Op", group="Aim Bot")
class AimBotTeleOp(): OpMode() {

    val robot = AimBotHardware()


    fun fourMotors(rFpower: Double, lFpower: Double, lBpower: Double, rBpower: Double) {
        robot.rFDrive?.power = rFpower
        robot.lFDrive?.power = lFpower
        robot.lBDrive?.power = lBpower
        robot.rBDrive?.power = rBpower
    }

    override fun init() {
        robot.init(hardwareMap)
    }

    override fun loop() {
        val y =gamepad1.left_stick_y.toDouble()
        val x =gamepad1.left_stick_x.toDouble()
        val r =gamepad1.right_stick_x.toDouble()

        fourMotors(
               -(y + x + r),
                -(y - x - r),
                -(y + x - r),
                -(y - x + r)
        )
        
//        Patrick and James' goal for today:
//        add shooter control to tele-op (right under here)

        robot.rFDrive!!.power = gamepad1.right_trigger.toDouble()
        robot.rFDrive!!.power = gamepad1.left_trigger.toDouble()
    }
}
