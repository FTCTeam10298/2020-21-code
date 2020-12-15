package jamesTelemetryMenu

class PairSystem {

    val matches: MutableList<String> = mutableListOf("")

    fun addPair(item1: String, item2: String) {
        matches.add(item1 + item2)
    }

    fun getRaw(predicate: String): String = matches.filter{ it.contains(predicate) }.toString()

    fun getMatches(predicate: String): String = getRaw(predicate).replace(predicate,"").removePrefix("[").removeSuffix("]")
}

class OptionSystem {

    private val options = PairSystem()
    private val items = PairSystem()

    fun addOption(item: String, option: String) {
        val id: Int = items.matches.size

        if (options.matches.contains(option)) {
            items.addPair(option, options.getMatches(option))
        } else {
            options.addPair(item, id.toString())
            items.addPair(option, id.toString())
        }
    }

    fun getItems(option: String): String = items.getMatches(options.getMatches(option))

    fun listOfOptions(): MutableList<String> = options.matches

}

open class MenuSystem {
    /*private*/ val options = OptionSystem()
    private val links = PairSystem()

    fun addChoiceItem(option: String, item: String) {
        options.addOption(option, item)
    }

    fun getItems(option: String): String = options.getItems(option)

    fun addLink(item: String, Choice: String) {
        links.addPair(item, Choice)
    }

    fun getLink(item: String): String = links.getMatches(item)
}