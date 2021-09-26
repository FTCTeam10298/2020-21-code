package us.brainstormz.pathfinder

import us.brainstormz.localization.PositionAndRotation
import us.brainstormz.localization.World
import us.brainstormz.path.Path

interface PathFinder {
    fun calculatePath(world: World, from: PositionAndRotation, to: PositionAndRotation):Path
}