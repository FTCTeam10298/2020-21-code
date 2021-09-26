package us.brainstormz.motion

import com.qualcomm.robotcore.hardware.DcMotor

interface DifferentialDrivePlatform {
    fun leftDrive():List<DcMotor>
    fun rightDrive():List<DcMotor>
}