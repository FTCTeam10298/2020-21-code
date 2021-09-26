package us.brainstormz.pathexecution

import us.brainstormz.path.Path
import java.util.concurrent.Future

interface PathExecutor {
    fun doPath(path: Path): Future<Boolean>
}