package us.brainstormz.motion

import robotCode.hardwareClasses.MecanumHardware

class MecanumDifferentialDrivePlatform(private val hardware:MecanumHardware):DifferentialDrivePlatform {
    override fun leftDrive() = listOf(hardware.lBDrive, hardware.lFDrive)

    override fun rightDrive() = listOf(hardware.rBDrive, hardware.rFDrive)
}