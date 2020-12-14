package jamesTelemetryMenu

open class PairSystem {

    /*private*/ val matches: MutableList<String> = mutableListOf("")

    fun addPair(item1: String, item2: String) {
        matches.add(item1 + item2)
    }

    fun getRaw(predicate: String): String = matches.filter{ it.contains(predicate) }.toString()

    fun getMatches(predicate: String): String = getRaw(predicate).replace(predicate,"").removePrefix("[").removeSuffix("]")
}

open class OptionSystem: PairSystem() {

}

open class MenuChoices {
    /*private*/ val choices = OptionSystem()
    private val links = PairSystem()

    fun addChoiceItem(Choice: String, item: String) {
        choices.addPair(Choice, item)
    }

    fun addLink(item: String, Choice: String) {
        links.addPair(item, Choice)
    }

    fun getOptions(choice: String): String = choices.getMatches(choice)

    fun getLink(item: String): String = links.getMatches(item)
}