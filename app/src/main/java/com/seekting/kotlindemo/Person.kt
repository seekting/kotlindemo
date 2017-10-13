package com.seekting.kotlindemo

import com.seekting.kotlindemo.test1.Man
import com.seekting.kotlindemo.test1.TestActivity

/**
 * Created by Administrator on 2017/9/22.
 */
data class Person(
        var id: Long,
        var name: String,
        var age: Int? = null

) {
    fun print() {
        var ss = toString()
//        Log.d("seekting", ss)
    }

    fun isOlder(other: Person): Boolean? {
        if (this.age != null) {
            if (other.age != null) {
                val a = other.age + 1
            }
        }
        return true
    }
}

interface OnClickListener {
    fun onClick()
    fun onTouch()
}

class Button(var listener: OnClickListener? = null) {
    fun performClick() {
        println("performClick")
        listener!!.onClick()
    }

}

fun Button.setOnClickListener(listener: String.() -> Int) {
    this.listener = object : OnClickListener {
        override fun onTouch() {
        }

        override fun onClick() {
            listener.toString()
        }
    }
}

class Test() {

}

fun main(args: Array<String>) {

//    val button = Button()
//    button.setOnClickListener { this.length }
//    button.performClick()
//    call { println("gogogo"); 1 }
//    val a = "xx"
//    val res = a.run {
//        println(this);1
//    }
//    println(res)
//    val j = (m:Int, n:Int)->m*n

//    mn(1, 2, { f: Float, s: String ->
//        val result = "i=${f}s=$s"
//        print(result)
//        f.toByte()//最后一行就是返回
//
//    })
//    val codeblock = { f: Float, s: String, b: Boolean ->
//        println("hello $s,b=$b")
//        f.toByte()
//    }
//
//    println(codeblock.javaClass)
//    for (m in codeblock.javaClass.methods) {
//        if (m.name == "invoke") {
//            val a = m.invoke(codeblock, 1.2f, "tt", true)
//            println("a=$a")
//        } else if (m.name == "getArity") {
//            val a = m.invoke(codeblock)
//            println("a=$a")
//        }
//
//    }
//
//    run {
//        println("run!")
//    }
//    val t = Test()
//    for (m in t.javaClass.methods) {
//        println("test:${m.name}")
//    }
//    mn(1, 2, codeblock)
//    val result: Byte = codeblock(1.2f, "3")
//    if (m.name == "invoke") {
//        m.invoke(codeblock, 1.2f,"tt")
//    }
//    val codeblock = { s: String ->
//        println("hello ")
//
//    }
//    fun <T, R> with(receiver: T, f: T.() -> R): R = receiver.f()
//    val array = Person1("seekting")
//    val a = with(array) {
//        say()
//    }
//    a.let {
//        println("let")
//    }
//    print(array)
//    val s = "a"
//    with(s, codeblock)
//    with(s, codeblock)

//    val person = Person1("seekting")
//    person.let {
//        it.say()
//
////        person1.say()
////        i
//    }
//    "seekting".let {
//        println(it.get(0))
//    }
    val person = Person1("seekting")
//    person.let {
//        it.say()
//        it.sleep()
//        it.wakeup()
//    }
//    val a = with(person, {
//        person.wakeup()
//        1
//    })
//    println(a)
//    with(person) {
//    }
//
//    myWith(person) {
//
//    }

    val man = Man(1, "seekting")
    myWith(man) {

    }
//    with(man) {
//        man.age = 10
//        man.name = "seekting"
//        man.say()
//    }
//    with(man) {
//        man.t(1, "a")
//    }

    val a = { a: Man ->
        a.age = 11
        a.name = "11"


    }
    "111".toUpperCase()
//    study(man) {
//        age = 11
//        name = "222"
//    }

    val person1 = Person(1, "seekting", 30)
    person1.block()
    person1.print()
    study(person1) {
        person1.age = 11
        person1.name = "11"
    }
    with(person) {
        person1.age = 11
        person1.name = "11"
    }
    person1.apply {
        this.age = 11
        //it.age 报错

    }
    person1.also {
        it.age = 11
        //this.age=11 报错
    }

    person1.also { p: Person ->
        p.age = 11
        //this.age=11 报错
    }
    run { "hello" }

    myRun("hello") {
        println(it)
    }
    val block1 = { s: String ->
        println(s)
    }
    myRun1("hello", block1) {

    }
    "".toUpperCase()
    var str: String? = ""
    strLenSafe(str)


    val s: String? = null
    s.isNullOrBlank()
    val person2: Person? = null
    if (person2 != null) {
        person2.print()
    }
    person2.hasName()

}

inline fun Person?.hasName(): Boolean {
    if (this == null) {
        return false
    }
    return name != null
}

fun playPerson(person: Person) {
    println("play person $person")
}

class MyGroup(


) {
    lateinit var person: Person
    fun setUp() {
        person = Person(1L, "seekting", 2)
    }

    fun pri() {
        println(person.age)
    }
}

fun Person.block() {
    this.age = 11
    this.name = "11"
}

fun study(person: Person, block: Person.() -> Unit) {
    person.print()
    person.block()
    person
}


fun <R> study1(r: R, block: R.() -> Unit) {
    r.block()
}

//fun <T, R> with(t: T, block: T.(Int, String) -> R): R {
//
//    return t.block(2, "11")
//}
fun test(block1: (Person1) -> Unit) {
    block1
}

class Person1(val name: String) {
    fun say() {
        println("I am $name")
    }

    fun sleep() {
        println("$name sleep")
    }

    fun wakeup() {
        println("$name wakeup")
    }
}


inline fun invokeBlock(a: () -> Int, b: () -> Int): Int {
    return a() + b()
}

inline fun mn(n: Int, m: Int, block: (Float, String) -> Byte): Boolean {
    block(3.14f, "PI")
    return m * n > 0

}

public inline fun call(block: () -> Int) {
    val value = block()
    block.also { }
    println("value=$value")

}

public inline fun <T, R> T.run(t: T, block: () -> R): R {
    println("t:$t")
    return block()
}

//public inline fun <T> T.also(block: (T) -> Unit): T { block(this); return this }
//inline fun <T> with(t: T, body: T.() -> Unit) { t.body() }
//public inline fun <R> run(block: () -> R): R = block()

//public inline fun <T, R> with(receiver: T, block: T.() -> R): R = receiver.block()
//public inline fun <T, R> with(receiver: T, block: T.() -> R): R {
//    return receiver.block()
//
//}


fun <T, R> myWith(t: T, block: (T) -> R): R {
    return block(t)
}

fun myRun(t: String, block: (String) -> Unit) {
    block(t)
}

fun myRun1(t: String, block: (String) -> Unit, block1: (String) -> Unit) {
    block(t)
}

inline fun TestActivity.test() {}

fun strLenSafe(s: String?): Int =
        if (s != null) s.length else 0

