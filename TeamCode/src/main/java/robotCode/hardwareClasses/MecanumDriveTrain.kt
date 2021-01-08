package robotCode.hardwareClasses

import com.qualcomm.robotcore.hardware.DcMotor

open class MecanumDriveTrain(private val hardware: MecanumHardware) {

    /**
     * driveSetPower sets all of the drive train motors to the specified power levels.
     * @param lFPower Power level to set front left motor to
     * @param rFPower Power level to set front right motor to
     * @param lBPower Power level to set back left motor to
     * @param rBPower Power level to set back right motor to
     */
    fun driveSetPower(lFPower: Double, rFPower: Double, lBPower: Double, rBPower: Double) {
        hardware.lFDrive.power = lFPower
        hardware.rFDrive.power = rFPower
        hardware.lBDrive.power = lBPower
        hardware.rBDrive.power = rBPower
    }

    /**
     * DrivePowerAll sets all of the drive train motors to the specified power level.
     * @param power Power level to set all motors to
     */
    fun drivePowerAll(power: Double) {
        driveSetPower(power, power, power, power)
    }

    fun drivePowerZero() {
        drivePowerAll(0.0)
    }

    /**
     * driveSetMode sets all of the drive train motors to the specified mode.
     * @param runmode RunMode to set motors to
     */
    fun driveSetMode(runmode: DcMotor.RunMode?) {
        hardware.lFDrive.mode = runmode
        hardware.rFDrive.mode = runmode
        hardware.lBDrive.mode = runmode
        hardware.rBDrive.mode = runmode

    }


    fun driveSetRunToPosition() {
        driveSetMode(DcMotor.RunMode.RUN_TO_POSITION)
    }

    /**
     * driveSetTargetPosition sets all of the drive train motors to the specified positions.
     * @param lFPosition Position to set front left motor to run to
     * @param rFPosition Position to set front right motor to run to
     * @param lBPosition Position to set back left motor to run to
     * @param rBPosition Position to set back right motor to run to
     */
    fun driveSetTargetPosition(lFPosition: Int, rFPosition: Int, lBPosition: Int, rBPosition: Int) {

    }

    fun driveAddTargetPosition(lFPosition: Int, rFPosition: Int, lBPosition: Int, rBPosition: Int) {

    }

    fun driveAllAreBusy(): Boolean = hardware.lFDrive.isBusy && hardware.rFDrive.isBusy && hardware.lBDrive.isBusy && hardware.rBDrive.isBusy

}