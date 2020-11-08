package jamesTelemetryMenu

open class TelemetryQueue() {

    var queue: MutableList<String> = mutableListOf()
    var lastUserLine= 0

    fun addToQueue(line: Int, text: String) {
        replaceLine(line, text)
        if (line > lastUserLine)
            lastUserLine = line
    }

    fun replaceLine(line: Int, text: String) {

//        Adds empty lines into the list if necessary
        while (queue.size <= line)
            queue.add(" ")

//        Add content
        queue[line] = text
    }
}