package us.brainstormz.pathexecution

import us.brainstormz.localization.Localizer
import us.brainstormz.motion.DifferentialDrivePlatform
import us.brainstormz.path.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class DifferentialDrivePathExecutor(
    private val localizer: Localizer,
    private val drivePlatform: DifferentialDrivePlatform): PathExecutor {

    override fun doPath(path: Path):Future<Boolean> {

        val result = CompletableFuture<Boolean>()

        object:Thread(){
            override fun run() {
                TODO("Patrick will implement me")

//                val allDrives = drivePlatform.leftDrive() + drivePlatform.rightDrive()
//
//                allDrives.forEach {
//                    it.power = 1.0
//                }
//                sleep(1500)
//                result.complete(true)

            }
        }.start()

        return result
    }
}