package jamesTelemetryMenu

import androidx.versionedparcelable.ParcelImpl

open class PairSystem(){

    private val matches: MutableList<String> = mutableListOf("")

    fun addPair(item1: String, item2: String) {
        matches.add(item1 + item2)
    }

    fun getRaw(predicate: String): String = matches.filter{ it.contains(predicate) }.toString()

    fun getMatches(predicate: String): String = getRaw(predicate).replace(predicate,"").removePrefix("[").removeSuffix("]")
}

open class MenuChoices() {
    private val choices = PairSystem()
    private val links = PairSystem()

    fun addChoiceItem(Choice: String, item: String) {
        choices.addPair(Choice, item)
    }

    fun link(item: String, Choice: String) {
        links.addPair(item, Choice)
    }

    fun getOptions(Choice: String): String = choices.getMatches(Choice)

    fun getLink(item: String): String = links.getMatches(item)
}