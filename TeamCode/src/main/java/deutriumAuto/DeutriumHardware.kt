//This package was commited by Gabe, your friendly Aperture Science Bad Code Associate!
// AD: Join the new Aperture "Code with portals" initiative today!!

//APERTURE SCIENCE INNOVATORS
//PROJECT DEUTRIUM
//WARNING: WE ARE NOT RESPONSIBLE FOR DAMAGE DONE TO DISPLAY, COMPUTER SYSTEM, OR THE SANITY OF
//UNAUTHORIZED USERS

package deutriumAuto

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

open class DeutriumHardware {

    //    these are the drive motors
    var rFDrive: DcMotor? = null
    var lFDrive: DcMotor? = null
    var rBDrive: DcMotor? = null
    var lBDrive: DcMotor? = null

    var hwMap: HardwareMap? = null

    fun init(ahwMap: HardwareMap) {

        hwMap = ahwMap

        rFDrive = hwMap?.get("rFDrive") as DcMotor
        lFDrive = hwMap?.get("lFDrive") as DcMotor
        rBDrive = hwMap?.get("rBDrive") as DcMotor
        lBDrive = hwMap?.get("lBDrive") as DcMotor

        rFDrive?.direction = DcMotorSimple.Direction.FORWARD
        lFDrive?.direction = DcMotorSimple.Direction.REVERSE
        rBDrive?.direction = DcMotorSimple.Direction.REVERSE
        lBDrive?.direction = DcMotorSimple.Direction.FORWARD

        rFDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lFDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rBDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lBDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        rFDrive?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lFDrive?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rBDrive?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rBDrive?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    // this would be useful, but i'm lazy and so it lives in auto
    //fun decay(startspeed: Double, runtime: Double, decayrate: Double) {
//     }
}