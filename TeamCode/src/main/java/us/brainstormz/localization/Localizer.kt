package us.brainstormz.localization

import locationTracking.Coordinate

interface Localizer {
    fun currentPositionAndRotation(): Coordinate
    fun recalculatePositionAndRotation()
    fun setPositionAndRotation(x: Double? = null, y: Double? = null, r: Double? = null)
}