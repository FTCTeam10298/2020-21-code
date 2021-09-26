package us.brainstormz.pathfinder

import locationTracking.Coordinate
import us.brainstormz.localization.World
import us.brainstormz.path.Path

interface PathFinder {
    fun calculatePath(world: World, from:Coordinate, to:Coordinate):Path
}