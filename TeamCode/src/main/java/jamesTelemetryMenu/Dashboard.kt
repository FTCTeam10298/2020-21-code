package jamesTelemetryMenu

import org.firstinspires.ftc.robotcore.external.Telemetry

open class Dashboard(telemetry: Telemetry) {

    var queue: MutableList<String> = mutableListOf()
    private val telemetry = telemetry

    open fun display(line: Int, text: String) {
        replaceLine(line, text)
        queueToTelemetry()
    }

    open fun replaceLine(line: Int, text: String) {

//        Adds empty lines into the list if necessary
        while (queue.size <= line)
            queue.add(" ")

//        Adds or subtracts cursor
        if (queue[line].startsWith("-"))
            queue[line] = queue[line].replaceFirst("-", " ")
    }

    open fun queueToTelemetry() {
        telemetry.clearAll()
        for (i in (1 until queue.size)) {
            telemetry.addLine(queue[i])
        }
        telemetry.update()
    }
}