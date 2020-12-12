package jamesTelemetryMenu

import org.firstinspires.ftc.robotcore.external.Telemetry

open class TelemetryConsole(private val telemetry: Telemetry) {

    var queue: MutableList<String> = mutableListOf()
    var lastUserLine= 0

//    fun addToQueue(line: Int, text: String) {
//        replaceLine(line, text)
//        if (line > lastUserLine)
//            lastUserLine = line
//    }

    fun replaceLine(line: Int, text: String) {

//        Adds empty lines into the list if necessary
        while (queue.size <= line)
            queue.add(" ")

//        Add content
        queue[line] = text
    }

    fun display(line: Int, text: String) {
        replaceLine(line, text)

        if (line > lastUserLine)
            lastUserLine = line

        queueToTelemetry()
    }

    fun queueToTelemetry() {
        telemetry.clearAll()
        for (i in (1 until queue.size)) {
            telemetry.addLine(queue[i])
        }
        println("its here")
        telemetry.update()
    }
}