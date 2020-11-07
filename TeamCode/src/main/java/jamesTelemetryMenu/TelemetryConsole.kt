package jamesTelemetryMenu

import org.firstinspires.ftc.robotcore.external.Telemetry

open class TelemetryConsole(telemetry: Telemetry) {

    var queue: MutableList<String> = mutableListOf()
    val telemetry = telemetry
    var lastUserLine= 0

    fun display(line: Int, text: String) {
        replaceLine(line, text)

        if (line > lastUserLine)
            lastUserLine = line

        queueToTelemetry()
    }

    open fun replaceLine(line: Int, text: String) {

//        Adds empty lines into the list if necessary
        while (queue.size <= line)
            queue.add(" ")

//        Add content
        queue[line] = text
    }

    open fun queueToTelemetry() {
        telemetry.clearAll()
        for (i in (1 until queue.size)) {
            telemetry.addLine(queue[i])
        }
        telemetry.update()
    }
}