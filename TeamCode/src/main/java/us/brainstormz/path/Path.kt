package us.brainstormz.path

import locationTracking.Coordinate

interface Path {
    fun length():Double
    fun positionAt(distance:Double):Coordinate
}