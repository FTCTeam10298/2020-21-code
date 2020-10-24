//more gabe code, so 42
package MTEST


import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

open class MTestHardware {

    //    these are the spinny flywheel death things
    var rFlywheel: DcMotor? = null
    var lFlywheel: DcMotor? = null

    var hwMap: HardwareMap? = null

    fun init(ahwMap: HardwareMap) {

        hwMap = ahwMap

        rFlywheel = hwMap?.get("rFlywheel") as DcMotor
        lFlywheel = hwMap?.get("lFlywheel") as DcMotor

        rFlywheel?.direction = DcMotorSimple.Direction.REVERSE
        lFlywheel?.direction = DcMotorSimple.Direction.FORWARD

        rFlywheel?.mode = DcMotor.RunMode.RUN_USING_ENCODER
        lFlywheel?.mode = DcMotor.RunMode.RUN_USING_ENCODER

    }
}