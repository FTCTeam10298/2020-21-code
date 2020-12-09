package jamesTelemetryMenu

open class PairSystem(private val list: MutableList<String>) {
    fun addPair(item1: String, item2: String) {
        list.add(item1 + item2)
    }

    fun getRaw(item: String): String = list.filter{ it.contains(item) }.toString()

    fun getMatch(item: String): String = getRaw(item).replace(item,"").removePrefix("[").removeSuffix("]")

    fun getBoth(item: String): String = getRaw(item).removePrefix("[").removeSuffix("]")
}

//class IdSystem(private val list: MutableList<String>): PairSystem(list) {
//    fun addItem(name: String) {
//        addPair(name, list.size.toString())
//    }
//
//    fun getRaw(id: Int): String = getRaw(id.toString())
//
//    fun getId(name: String): Int = getRaw(name).replaceBefore(name, "").replace(name,"").removeSuffix("]").toInt()
//
//    fun getName(id: Int): String = getRaw(id).replaceAfter(id.toString(), "").replace(id.toString(),"").removePrefix("[")
//
//    fun getName(name: String): String = getName(getId(name))
//
//}

class LinkSystem {

    private val linkList: MutableList<String> = mutableListOf("")
    private val links = PairSystem(linkList)

    private val optionList: MutableList<String> = mutableListOf("")
    private val options = PairSystem(optionList)

    fun addOption(choice: String,  option: String) {
        options.addPair(choice, option)
    }

    fun linkOption(option: String, choice: String) {
        links.addPair(choice, option)
    }

    fun getMatch(item: String): String = options.getMatch(item)

    fun getBoth(item: String): String = options.getBoth(item)

}