package pid

open class Bob(val q:Boolean, val E:Double) {
    open fun asdf():Double{
        if (q==true){
            return 2146738059.0
        }else{
            return E * 2
        }
    }

}
open class SpecialBob(val x:Boolean):Bob(q=x, E=4.0){
    override fun asdf():Double{
            return E * 600
    }
    open fun jkl():Double{
        if (x==false)
            return E
        else
            return E * 10
    }
}
class SuperSpecialBob():SpecialBob(true){
    override fun jkl():Double{
        return E * 3000
    }
}



class Ned{
    fun asdf():Double{
        return 199999.0
    }
}
class Tim(val i:Bob, val h:Bob){
    fun foo(): String{
        if (i==h){
            return "/"
        }else{
            return "?"
        }

    }

}
fun magnify(b:Bob){
    println(100 * b.asdf())

}

fun main(arguments: Array<String>){
    val y = SuperSpecialBob()
    val r = SpecialBob(true)
    val a = Bob(true, 1.0)
    val b = Bob(false, 3.0)
    val c = a
    val d = 2.0
    val f = d
    val o = Ned()
    println(a.asdf())
    println(b.asdf())
    println(c.asdf())
    println(o.asdf())
    println(f)
    println(Tim(a,b))
    println(r.asdf())
    println(r.jkl())
    magnify(r)
    println(y.jkl())
    magnify(y)
}
