package us.brainstormz.path

import us.brainstormz.localization.PositionAndRotation

interface Path {
    fun length():Double
    fun positionAt(distance:Double): PositionAndRotation
}