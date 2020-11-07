package robotCode.aimBotRobot

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import java.lang.Thread.sleep
import kotlin.math.PI
import kotlin.math.abs

open class EncoderDriveMovement(): MecanumDriveTrain() {

//    val menu = TelemetryMenu(telemetry, gamepad1)
    lateinit var rangeSensor: ModernRoboticsI2cRangeSensor

    val COUNTS_PER_MOTOR_REV = 28.0 // Rev HD Hex v2.1 Motor encoder
    val GEARBOX_RATIO = 19.2 // 40 for 40:1, 20 for 20:1
    val DRIVE_GEAR_REDUCTION = 1 / 1 // This is > 1.0 if geared for torque
    val WHEEL_DIAMETER_INCHES = 3.77953 // For figuring circumference
    val DRIVETRAIN_ERROR = 1.0 // Error determined from testing
    val COUNTS_PER_INCH = COUNTS_PER_MOTOR_REV * GEARBOX_RATIO * DRIVE_GEAR_REDUCTION / (WHEEL_DIAMETER_INCHES * PI) / DRIVETRAIN_ERROR
    val COUNTS_PER_DEGREE: Double = COUNTS_PER_INCH * 0.225 + 0.0 // Found by testing


    /**
     * DriveRobotTime drives the robot the set number of inches at the given power level.
     * @param ms How long to drive
     * @param power Power level to set motors to, negative will drive the robot backwards
     */
    fun driveRobotTime(ms: Int, power: Double) {
        driveSetMode(DcMotor.RunMode.RUN_USING_ENCODER)
        drivePowerAll(power)
        sleep(ms.toLong())
        drivePowerAll(0.0)
        driveSetTargetPosition(0, 0, 0, 0)
    }

    /**
     * DriveRobotDistanceToObject drives the robot to the set number of inches from an object
     * (usually the wall) at the given power level.
     * @param inches How many inches away to the object to go to
     * @param power Power level to set motors to
     */
    fun driveRobotDistanceToObject(power: Double, inches: Double, smart_accel: Boolean) {
        val target = rangeSensor.getDistance(DistanceUnit.INCH) as Float - inches // FIXME: how accurate is sensor?
//        menu.display(10, "Range Sensor: ${rangeSensor.getDistance(DistanceUnit.INCH)}")
        driveRobotPosition(abs(power), target, smart_accel) // Use abs() to make sure power is positive
    }

    /**
     * DriveRobotPosition drives the robot the set number of inches at the given power level.
     * @param inches How far to drive, can be negative
     * @param power Power level to set motors to
     */
    fun driveRobotPosition(power: Double, inches: Double, smart_accel: Boolean) {

        var state = 0 // 0 = NONE, 1 = ACCEL, 2 = DRIVE, 3 = DECEL
        val position: Double = inches * COUNTS_PER_INCH

        driveSetRunToPosition()
        if (smart_accel && power > 0.25) {
            drivePowerAll(0.25) // Use abs() to make sure power is positive
            state = 1 // ACCEL
        } else {
            drivePowerAll(abs(power)) // Use abs() to make sure power is positive
        }
        val flOrigTarget: Int = lFDrive.targetPosition
        val frOrigTarget: Int = rFDrive.targetPosition
        val blOrigTarget: Int = lBDrive.targetPosition
        val brOrigTarget: Int = rBDrive.targetPosition
        driveAddTargetPosition(position.toInt(), position.toInt(), position.toInt(), position.toInt())
        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            while (driveAllAreBusy()) {
                val flDrive: Int = lFDrive.currentPosition
                val frDrive: Int = rFDrive.currentPosition
                val blDrive: Int = lBDrive.currentPosition
                val brDrive: Int = rBDrive.currentPosition
//                menu.display(3, "Front left encoder: $flDrive")
//                menu.display(4, "Front right encoder: $frDrive")
//                menu.display(5, "Back left encoder: $blDrive")
//                menu.display(6, "Back right encoder $brDrive")

                // State magic
                if (state == 1 &&
                        (abs(flDrive - flOrigTarget) > 2 * COUNTS_PER_INCH || abs(frDrive - frOrigTarget) > 2 * COUNTS_PER_INCH || abs(blDrive - blOrigTarget) > 2 * COUNTS_PER_INCH || abs(brDrive - brOrigTarget) > 2 * COUNTS_PER_INCH)) {
                    // We have gone 2 inches, go to full power
                    drivePowerAll(abs(power)) // Use abs() to make sure power is positive
                    state = 2
                } else if (state == 2 &&
                        (abs(flDrive - flOrigTarget) > COUNTS_PER_INCH * (abs(inches) - 2) || abs(frDrive - frOrigTarget) > COUNTS_PER_INCH * (abs(inches) - 2) || abs(blDrive - blOrigTarget) > COUNTS_PER_INCH * (abs(inches) - 2) || abs(brDrive - brOrigTarget) > COUNTS_PER_INCH * (abs(inches) - 2))) {
                    // Cut power by half to DECEL
                    drivePowerAll(abs(power) / 2) // Use abs() to make sure power is positive
                    state = 3 // We are DECELing now
                }
//                menu.display(7, "State: $state (0=NONE,1=ACCEL,2=DRIVING,3=DECEL")
            }
            sleep(10)
        }
        drivePowerAll(0.0)
        // Clear used section of dashboard
//        menu.display(3, "")
//        menu.display(4, "")
//        menu.display(5, "")
//        menu.display(6, "")
//        menu.display(7, "")
    }

    fun driveRobotTurn(power: Double, degree: Double, smart_accel: Boolean) {

        val position: Double = degree * COUNTS_PER_DEGREE
        var state = 0 // 0 = NONE, 1 = ACCEL, 2 = DRIVE, 3 = DECEL

        driveSetRunToPosition()
        if (smart_accel) {
            state = 1
            driveSetPower(power * 0.5, -power * 0.5, power * 0.5, -power * 0.5)
        } else {
            driveSetPower(power, -power, power, -power)
        }
        val flOrigTarget: Int = lFDrive.targetPosition
        val frOrigTarget: Int = rFDrive.targetPosition
        val blOrigTarget: Int = lBDrive.targetPosition
        val brOrigTarget: Int = rBDrive.targetPosition
        driveAddTargetPosition(position.toInt(), -position.toInt(), position.toInt(), -position.toInt())
        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            while (driveAllAreBusy()) {
                val flDrive: Int = lFDrive.currentPosition
                val frDrive: Int = rFDrive.currentPosition
                val blDrive: Int = lBDrive.currentPosition
                val brDrive: Int = rBDrive.currentPosition
//                menu.display(3, "Front left encoder: $flDrive")
//                menu.display(4, "Front right encoder: $frDrive")
//                menu.display(5, "Back left encoder: $blDrive")
//                menu.display(6, "Back right encoder: $brDrive")

                // State magic
                if (state == 1 &&
                        (abs(flDrive - flOrigTarget) > COUNTS_PER_DEGREE * 10 || abs(frDrive - frOrigTarget) > COUNTS_PER_DEGREE * 10 || abs(blDrive - blOrigTarget) > COUNTS_PER_DEGREE * 10 || abs(brDrive - brOrigTarget) > COUNTS_PER_DEGREE * 10)) {
                    // We have rotated 10 degrees, go to full power
                    drivePowerAll(abs(power)) // Use abs() to make sure power is positive
                    state = 2
                } else if (state == 2 &&
                        (abs(flDrive - flOrigTarget) > COUNTS_PER_DEGREE * (abs(degree) - 10) || abs(frDrive - frOrigTarget) > COUNTS_PER_DEGREE * (abs(degree) - 10) || abs(blDrive - blOrigTarget) > COUNTS_PER_DEGREE * (abs(degree) - 10) || abs(brDrive - brOrigTarget) > COUNTS_PER_DEGREE * (abs(degree) - 10))) {
                    // We are within 10 degrees of our destination, cut power by half to DECEL
                    drivePowerAll(abs(power) / 2) // Use abs() to make sure power is positive
                    state = 3 // We are DECELing now
                }
//                menu.display(7, "State: $state (0=NONE,1=ACCEL,2=DRIVING,3=DECEL")
            }
            sleep(10)
        }
        drivePowerAll(0.0)
        // Clear used section of dashboard
//        menu.display(3, "")
//        menu.display(4, "")
//        menu.display(5, "")
//        menu.display(6, "")
//        menu.display(7, "")
    }

    /** For compatibility  */
    fun driveRobotTurn(power: Double, degree: Double) {
        driveRobotTurn(power, degree, false)
    }

    /**
     * DriveSidewaysTime makes the robot drive sideways for the specified time and power.
     * @param time How long to drive in seconds
     * @param power The power to use while driving,
     * positive values go right and negative values go left
     */
    fun driveSidewaysTime(time: Double, power: Double) {
        driveSetMode(RunMode.RUN_USING_ENCODER)
        driveSetPower(-power, power, power, -power)

        // Continue driving for the specified amount of time, then stop
        val ms = time * 1000
        sleep(ms.toLong())
        drivePowerAll(0.0)
        driveSetRunToPosition()
        driveSetTargetPosition(0, 0, 0, 0)
    }

    /**
     * DriveRobotHug is used to make the robot drive hugging a wall.
     * The robot will move mostly straight and slightly to the side,
     * so it will stay against the wall.
     * @param power Power to use while driving
     * @param inches How many inches to drive
     * @param hugLeft Whether to hug left or right
     */
    fun driveRobotHug(power: Double, inches: Int, hugLeft: Boolean) {

        val position: Double = inches * COUNTS_PER_INCH

        driveSetMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        driveSetMode(DcMotor.RunMode.RUN_TO_POSITION)
        if (!hugLeft && inches > 0 || hugLeft && inches < 0) {
            driveSetPower(power * .5, power, power, power * .5)
        } else if (!hugLeft && inches < 0 || hugLeft && inches > 0) {
            driveSetPower(power, power * .5, power * .5, power)
        }
        driveSetTargetPosition(position.toInt(), position.toInt(), position.toInt(), position.toInt())
        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            while (driveAllAreBusy()) {
//                menu.display(3, "Left front encoder: ${lFDrive.currentPosition}")
//                menu.display(4, "Right front encoder: ${rFDrive.currentPosition}")
//                menu.display(5, "Left back encoder: ${lBDrive.currentPosition}")
//                menu.display(6, "Right back encoder: ${rBDrive.currentPosition}")
            }
            sleep(10)
        }
        drivePowerAll(0.0)
    }

    fun driveRobotArc(power: Double, inches: Double, difference: Double) {

        var difference = difference
        val position: Double = inches * COUNTS_PER_INCH

        difference = Range.clip(difference, -1.0, 1.0)
        //power 1, inches -48, difference -.5
        driveSetMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        if (difference > 0) {
            rFDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
            rBDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
            lFDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            lBDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
        } else {
            rFDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            rBDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            lFDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
            lBDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
        }
        if (difference > 0 && inches > 0) driveSetPower(abs(power), abs(power * difference), abs(power), abs(power * difference)) else if (difference > 0 && inches < 0) driveSetPower(abs(power), -abs(power * difference), abs(power), -abs(power * difference)) else if (difference < 0 && inches > 0) driveSetPower(abs(power * difference), abs(power), abs(power * difference), abs(power)) else if (difference < 0 && inches < 0) driveSetPower(-abs(power * difference), abs(power), -abs(power * difference), abs(power))
        if (difference > 0) {
            lFDrive.targetPosition = position.toInt()
            lBDrive.targetPosition = position.toInt()
        } else {
            rFDrive.targetPosition = position.toInt()
            rBDrive.targetPosition = position.toInt()
        }
        for (i in 0..4) {    // Repeat check 5 times, sleeping 10ms between,
            // as isBusy can be a bit unreliable
            if (difference > 0) {
                while (lFDrive.isBusy && lBDrive.isBusy) {
                    val flDrive: Int = lFDrive.currentPosition
                    val blDrive: Int = lBDrive.currentPosition
//                    menu.display(3, "Front left encoder: $flDrive")
//                    menu.display(4, "Back left encoder: $blDrive")
                }
            } else {
                while (rFDrive.isBusy && rBDrive.isBusy) {
                    val frDrive: Int = rFDrive.currentPosition
                    val brDrive: Int = rBDrive.currentPosition
//                    menu.display(3, "Front left encoder: $frDrive")
//                    menu.display(4, "Back left encoder: $brDrive")
                }
            }
            sleep(10)
        }
        drivePowerAll(0.0)
    }

//    New funs

    fun tiptoeMotor(motorUsed:DcMotor, ticks:Int) {

        motorUsed.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorUsed.mode = DcMotor.RunMode.RUN_TO_POSITION
//        telemetry.addLine("] ${motorUsed} JOINS THE FIGHT!")
//        telemetry.update()

        motorUsed.targetPosition = ticks
        while (motorUsed.isBusy) {
            motorUsed.power = 1.0
        }
//        telemetry.addLine("] ${motorUsed} uses Spin sucessfully!")
//        telemetry.update()
        motorUsed.power = 0.0



    }

    fun abscondCautiously(motorUsed1:DcMotor, motorUsed2:DcMotor, motorUsed3:DcMotor, motorUsed4:DcMotor,  ticks:Int) {

        motorUsed1.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorUsed1.mode = DcMotor.RunMode.RUN_TO_POSITION
        motorUsed2.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorUsed2.mode = DcMotor.RunMode.RUN_TO_POSITION
        motorUsed3.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorUsed3.mode = DcMotor.RunMode.RUN_TO_POSITION
        motorUsed4.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorUsed4.mode = DcMotor.RunMode.RUN_TO_POSITION
//        telemetry.addLine("] ${motorUsed1} JOINS THE FIGHT!")
//        telemetry.update()

        motorUsed1.targetPosition = ticks
        motorUsed2.targetPosition = ticks
        motorUsed3.targetPosition = ticks
        motorUsed4.targetPosition = ticks

        while (motorUsed1.isBusy && motorUsed2.isBusy && motorUsed3.isBusy && motorUsed4.isBusy) {
            motorUsed1.power = 1.0
            motorUsed2.power = 1.0
            motorUsed3.power = 1.0
            motorUsed4.power = 1.0
        }
//        telemetry.addLine("] ${motorUsed1} uses creepBeast sucessfully!")
//        telemetry.update()
        motorUsed1.power = 0.0
        motorUsed2.power = 0.0
        motorUsed3.power = 0.0
        motorUsed4.power = 0.0

    }

}