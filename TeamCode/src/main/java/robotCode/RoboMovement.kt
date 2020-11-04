import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import robotCode.AimBotHardware
import java.lang.Thread.sleep

open class AimBotHardwarePlus(): AimBotHardware() {

    /**
     * DrivePowerAll sets all of the drive train motors to the specified power level.
     * @param power Power level to set all motors to
     */
    open fun DrivePowerAll(power: Double) {
        driveSetPower(power, power, power, power)
    }

    /**
     * driveSetPower sets all of the drive train motors to the specified power levels.
     * @param flPower Power level to set front left motor to
     * @param frPower Power level to set front right motor to
     * @param blPower Power level to set back left motor to
     * @param brPower Power level to set back right motor to
     */
    open fun driveSetPower(flPower: Double, frPower: Double, blPower: Double, brPower: Double) {
        lFDrive.power = flPower
        rFDrive.power = frPower
        lBDrive.power = blPower
        rBDrive.power = brPower
    }

    /**
     * driveSetMode sets all of the drive train motors to the specified mode.
     * @param runmode RunMode to set motors to
     */
    open fun driveSetMode(runmode: RunMode?) {
        lFDrive.mode = runmode
        rFDrive.mode = runmode
        lBDrive.mode = runmode
        rBDrive.mode = runmode
    }

    open fun driveSetRunToPosition() {
        if (lFDrive.getMode() != RunMode.RUN_TO_POSITION || rFDrive.getMode() != RunMode.RUN_TO_POSITION || lBDrive.getMode() != RunMode.RUN_TO_POSITION || rBDrive.getMode() != RunMode.RUN_TO_POSITION) {
            driveSetMode(RunMode.STOP_AND_RESET_ENCODER)
            driveSetMode(RunMode.RUN_TO_POSITION)
            // When the encoder is reset, also reset the target position, so it doesn't add an old
            // target position when using driveAddTargetPosition().
            driveSetTargetPosition(0, 0, 0, 0)
        }
    }

    /**
     * driveSetMode sets all of the drive train motors to the specified positions.
     * @param flPosition Position to set front left motor to run to
     * @param frPosition Position to set front right motor to run to
     * @param blPosition Position to set back left motor to run to
     * @param brPosition Position to set back right motor to run to
     */
    open fun driveSetTargetPosition(flPosition: Int, frPosition: Int, blPosition: Int, brPosition: Int) {
        lFDrive.setTargetPosition(flPosition)
        rFDrive.setTargetPosition(frPosition)
        backLeftDrive.setTargetPosition(blPosition)
        backRightDrive.setTargetPosition(brPosition)
    }

    open fun driveAddTargetPosition(flPosition: Int, frPosition: Int, blPosition: Int, brPosition: Int) {
        lFDrive.setTargetPosition(lFDrive.getTargetPosition() + flPosition)
        rFDrive.setTargetPosition(rFDrive.getTargetPosition() + frPosition)
        backLeftDrive.setTargetPosition(backLeftDrive.getTargetPosition() + blPosition)
        backRightDrive.setTargetPosition(backRightDrive.getTargetPosition() + brPosition)
    }

    open fun driveAllAreBusy(): Boolean {
        return (lFDrive.isBusy() && rFDrive.isBusy() && backLeftDrive.isBusy()
                && backRightDrive.isBusy())
    }
}

open class RoboMovement() {

    val robot = AimBotHardwarePlus()

    /**
     * DriveRobotTime drives the robot the set number of inches at the given power level.
     * @param ms How long to drive
     * @param power Power level to set motors to, negative will drive the robot backwards
     */
    fun DriveRobotTime(ms: Int, power: Double) {
        robot.driveSetMode(DcMotor.RunMode.RUN_USING_ENCODER)
        robot.DrivePowerAll(power)
        sleep(ms.toLong())
        robot.DrivePowerAll(0.0)
        robot.driveSetTargetPosition(0, 0, 0, 0)
    }

    /**
     * DriveRobotDistanceToObject drives the robot to the set number of inches from an object
     * (usually the wall) at the given power level.
     * @param inches How many inches away to the object to go to
     * @param power Power level to set motors to
     */
    fun DriveRobotDistanceToObject(power: Double, inches: Double, smart_accel: Boolean) {
        val target = rangeSensor.getDistance(DistanceUnit.INCH) as Float - inches // FIXME: how accurate is sensor?
        dashboard.displayPrintf(10, "Range Sensor: ", rangeSensor.getDistance(DistanceUnit.INCH))
        DriveRobotPosition(Math.abs(power), target, smart_accel) // Use abs() to make sure power is positive
    }

    /**
     * DriveRobotPosition drives the robot the set number of inches at the given power level.
     * @param inches How far to drive, can be negative
     * @param power Power level to set motors to
     */
    fun DriveRobotPosition(power: Double, inches: Double, smart_accel: Boolean) {
        var state = 0 // 0 = NONE, 1 = ACCEL, 2 = DRIVE, 3 = DECEL
        val position: Double = inches * org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_INCH
        robot.driveSetRunToPosition()
        if (smart_accel && power > 0.25) {
            robot.DrivePowerAll(0.25) // Use abs() to make sure power is positive
            state = 1 // ACCEL
        } else {
            robot.DrivePowerAll(Math.abs(power)) // Use abs() to make sure power is positive
        }
        val flOrigTarget: Int = robot.lFDrive.getTargetPosition()
        val frOrigTarget: Int = robot.rFDrive.getTargetPosition()
        val blOrigTarget: Int = robot.backLeftDrive.getTargetPosition()
        val brOrigTarget: Int = robot.backRightDrive.getTargetPosition()
        robot.driveAddTargetPosition(position.toInt(), position.toInt(), position.toInt(), position.toInt())
        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            while (robot.driveAllAreBusy()) {
                val flDrive: Int = robot.lFDrive.getCurrentPosition()
                val frDrive: Int = robot.rFDrive.getCurrentPosition()
                val blDrive: Int = robot.backLeftDrive.getCurrentPosition()
                val brDrive: Int = robot.backRightDrive.getCurrentPosition()
                dashboard.displayPrintf(3, "Front left encoder: %d", flDrive)
                dashboard.displayPrintf(4, "Front right encoder: %d", frDrive)
                dashboard.displayPrintf(5, "Back left encoder: %d", blDrive)
                dashboard.displayPrintf(6, "Back right encoder %d", brDrive)

                // State magic
                if (state == 1 &&
                        (Math.abs(flDrive - flOrigTarget) > 2 * org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_INCH || Math.abs(frDrive - frOrigTarget) > 2 * org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_INCH || Math.abs(blDrive - blOrigTarget) > 2 * org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_INCH || Math.abs(brDrive - brOrigTarget) > 2 * org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_INCH)) {
                    // We have gone 2 inches, go to full power
                    robot.DrivePowerAll(Math.abs(power)) // Use abs() to make sure power is positive
                    state = 2
                } else if (state == 2 &&
                        (Math.abs(flDrive - flOrigTarget) > org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_INCH * (Math.abs(inches) - 2) || Math.abs(frDrive - frOrigTarget) > org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_INCH * (Math.abs(inches) - 2) || Math.abs(blDrive - blOrigTarget) > org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_INCH * (Math.abs(inches) - 2) || Math.abs(brDrive - brOrigTarget) > org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_INCH * (Math.abs(inches) - 2))) {
                    // Cut power by half to DECEL
                    robot.DrivePowerAll(Math.abs(power) / 2) // Use abs() to make sure power is positive
                    state = 3 // We are DECELing now
                }
                dashboard.displayPrintf(7, "State: %d (0=NONE,1=ACCEL,2=DRIVING,3=DECEL", state)
            }
            sleep(10)
        }
        robot.DrivePowerAll(0)
        // Clear used section of dashboard
        dashboard.displayText(3, "")
        dashboard.displayText(4, "")
        dashboard.displayText(5, "")
        dashboard.displayText(6, "")
        dashboard.displayText(7, "")
    }

    fun DriveRobotTurn(power: Double, degree: Double, smart_accel: Boolean) {
        val position: Double = degree * org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_DEGREE
        var state = 0 // 0 = NONE, 1 = ACCEL, 2 = DRIVE, 3 = DECEL
        robot.driveSetRunToPosition()
        if (smart_accel) {
            state = 1
            robot.driveSetPower(power * 0.5, -power * 0.5, power * 0.5, -power * 0.5)
        } else {
            robot.driveSetPower(power, -power, power, -power)
        }
        val flOrigTarget: Int = robot.lFDrive.getTargetPosition()
        val frOrigTarget: Int = robot.rFDrive.getTargetPosition()
        val blOrigTarget: Int = robot.backLeftDrive.getTargetPosition()
        val brOrigTarget: Int = robot.backRightDrive.getTargetPosition()
        robot.driveAddTargetPosition(position.toInt(), -position as Int, position.toInt(), -position as Int)
        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            while (robot.driveAllAreBusy()) {
                val flDrive: Int = robot.lFDrive.getCurrentPosition()
                val frDrive: Int = robot.rFDrive.getCurrentPosition()
                val blDrive: Int = robot.backLeftDrive.getCurrentPosition()
                val brDrive: Int = robot.backRightDrive.getCurrentPosition()
                dashboard.displayPrintf(3, "Front left encoder: %d", flDrive)
                dashboard.displayPrintf(4, "Front right encoder: %d", frDrive)
                dashboard.displayPrintf(5, "Back left encoder: %d", blDrive)
                dashboard.displayPrintf(6, "Back right encoder %d", brDrive)

                // State magic
                if (state == 1 &&
                        (Math.abs(flDrive - flOrigTarget) > org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_DEGREE * 10 || Math.abs(frDrive - frOrigTarget) > org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_DEGREE * 10 || Math.abs(blDrive - blOrigTarget) > org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_DEGREE * 10 || Math.abs(brDrive - brOrigTarget) > org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_DEGREE * 10)) {
                    // We have rotated 10 degrees, go to full power
                    robot.DrivePowerAll(Math.abs(power)) // Use abs() to make sure power is positive
                    state = 2
                } else if (state == 2 &&
                        (Math.abs(flDrive - flOrigTarget) > org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_DEGREE * (Math.abs(degree) - 10) || Math.abs(frDrive - frOrigTarget) > org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_DEGREE * (Math.abs(degree) - 10) || Math.abs(blDrive - blOrigTarget) > org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_DEGREE * (Math.abs(degree) - 10) || Math.abs(brDrive - brOrigTarget) > org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_DEGREE * (Math.abs(degree) - 10))) {
                    // We are within 10 degrees of our destination, cut power by half to DECEL
                    robot.DrivePowerAll(Math.abs(power) / 2) // Use abs() to make sure power is positive
                    state = 3 // We are DECELing now
                }
                dashboard.displayPrintf(7, "State: %d (0=NONE,1=ACCEL,2=DRIVING,3=DECEL", state)
            }
            sleep(10)
        }
        robot.DrivePowerAll(0)
        // Clear used section of dashboard
        dashboard.displayText(3, "")
        dashboard.displayText(4, "")
        dashboard.displayText(5, "")
        dashboard.displayText(6, "")
        dashboard.displayText(7, "")
    }

    /** For compatibility  */
    fun DriveRobotTurn(power: Double, degree: Double) {
        DriveRobotTurn(power, degree, false)
    }

    /**
     * DriveSidewaysTime makes the robot drive sideways for the specified time and power.
     * @param time How long to drive in seconds
     * @param power The power to use while driving,
     * positive values go right and negative values go left
     */
    fun DriveSidewaysTime(time: Double, power: Double) {
        robot.driveSetMode(DcMotor.RunMode.RUN_USING_ENCODER)
        robot.driveSetPower(-power, power, power, -power)

        // Continue driving for the specified amount of time, then stop
        val ms = time * 1000
        sleep(ms.toLong())
        robot.DrivePowerAll(0)
        robot.driveSetRunToPosition()
        robot.driveSetTargetPosition(0, 0, 0, 0)
    }

    /**
     * DriveRobotHug is used to make the robot drive hugging a wall.
     * The robot will move mostly straight and slightly to the side,
     * so it will stay against the wall.
     * @param power Power to use while driving
     * @param inches How many inches to drive
     * @param hugLeft Whether to hug left or right
     */
    fun DriveRobotHug(power: Double, inches: Int, hugLeft: Boolean) {
        val position: Double = inches * org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_INCH
        robot.driveSetMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        robot.driveSetMode(DcMotor.RunMode.RUN_TO_POSITION)
        if (!hugLeft && inches > 0 || hugLeft && inches < 0) {
            robot.driveSetPower(power * .5, power, power, power * .5)
        } else if (!hugLeft && inches < 0 || hugLeft && inches > 0) {
            robot.driveSetPower(power, power * .5, power * .5, power)
        }
        robot.driveSetTargetPosition(position.toInt(), position.toInt(), position.toInt(), position.toInt())
        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            while (robot.driveAllAreBusy()) {
                dashboard.displayPrintf(3, "Left front encoder: %d", robot.lFDrive.getCurrentPosition())
                dashboard.displayPrintf(4, "Right front encoder: %d", robot.rFDrive.getCurrentPosition())
                dashboard.displayPrintf(5, "Left back encoder: %d", robot.lBDrive.getCurrentPosition())
                dashboard.displayPrintf(6, "Right back encoder %d", robot.backRightDrive.getCurrentPosition())
            }
            sleep(10)
        }
        robot.DrivePowerAll(0)
    }

    fun DriveRobotArc(power: Double, inches: Double, difference: Double) {
        var difference = difference
        val position: Double = inches * org.firstinspires.ftc.teamcode.Brian_Autonomous.COUNTS_PER_INCH
        difference = Range.clip(difference, -1.0, 1.0)
        //power 1, inches -48, difference -.5
        robot.driveSetMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        if (difference > 0) {
            robot.rFDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER)
            robot.backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER)
            robot.lFDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION)
            robot.backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION)
        } else {
            robot.rFDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION)
            robot.backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION)
            robot.lFDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER)
            robot.backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER)
        }
        if (difference > 0 && inches > 0) robot.driveSetPower(Math.abs(power), Math.abs(power * difference), Math.abs(power), Math.abs(power * difference)) else if (difference > 0 && inches < 0) robot.driveSetPower(Math.abs(power), -Math.abs(power * difference), Math.abs(power), -Math.abs(power * difference)) else if (difference < 0 && inches > 0) robot.driveSetPower(Math.abs(power * difference), Math.abs(power), Math.abs(power * difference), Math.abs(power)) else if (difference < 0 && inches < 0) robot.driveSetPower(-Math.abs(power * difference), Math.abs(power), -Math.abs(power * difference), Math.abs(power))
        if (difference > 0) {
            robot.lFDrive.setTargetPosition(position.toInt())
            robot.backLeftDrive.setTargetPosition(position.toInt())
        } else {
            robot.rFDrive.setTargetPosition(position.toInt())
            robot.backRightDrive.setTargetPosition(position.toInt())
        }
        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            if (difference > 0) {
                while (robot.lFDrive.isBusy() && robot.backLeftDrive.isBusy()) {
                    val flDrive: Int = robot.lFDrive.getCurrentPosition()
                    val blDrive: Int = robot.backLeftDrive.getCurrentPosition()
                    dashboard.displayPrintf(3, "Front left encoder: %d", flDrive)
                    dashboard.displayPrintf(4, "Back left encoder: %d", blDrive)
                }
            } else {
                while (robot.rFDrive.isBusy() && robot.backRightDrive.isBusy()) {
                    val frDrive: Int = robot.rFDrive.getCurrentPosition()
                    val brDrive: Int = robot.backRightDrive.getCurrentPosition()
                    dashboard.displayPrintf(3, "Front left encoder: %d", frDrive)
                    dashboard.displayPrintf(4, "Back left encoder: %d", brDrive)
                }
            }
            sleep(10)
        }
        robot.DrivePowerAll(0)
    }
}