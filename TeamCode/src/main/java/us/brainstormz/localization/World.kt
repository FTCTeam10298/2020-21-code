package us.brainstormz.localization

import locationTracking.Coordinate

interface World {
    fun currentPositionAndRotation(): Coordinate
}