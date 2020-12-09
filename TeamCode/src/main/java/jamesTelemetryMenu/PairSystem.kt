package jamesTelemetryMenu

open class PairSystem(private val list: MutableList<String>) {
    fun addPair(item1: String, item2: String) {
        list.add(item1 + item2)
    }

    fun getRaw(item: String): String = list.filter{ it.contains(item) }.toString()

    fun getMatch(item: String): String = getRaw(item).replace(item,"").removePrefix("[").removeSuffix("]")
}

class IdSystem(private val list: MutableList<String>): PairSystem(list) {
    fun addItem(name: String) {
        addPair(name, list.size.toString())
    }

    fun getRaw(id: Int): String = getRaw(id.toString())

    fun getId(name: String): Int = getRaw(name).replaceBefore(name, "").replace(name,"").removeSuffix("]").toInt()

    fun getName(id: Int): String = getRaw(id).replaceAfter(id.toString(), "").replace(id.toString(),"").removePrefix("[")

    fun getName(name: String): String = getName(getId(name))

}

class LinkSystem {

    private val idList: MutableList<String> = mutableListOf("")
    private val linkList: MutableList<String> = mutableListOf("")

    private val ids = IdSystem(idList)
    private val links = PairSystem(linkList)

    fun addPair(element1: String, element2: String) {
        ids.addItem(element1)
        ids.addItem(element2)

        links.addM(ids.getId(element1).toString(), ids.getId(element2))
    }

    fun getPair(element)
}