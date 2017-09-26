# Lambda的一点理解

## 初探
Lambda表达，可以看成是一个代码块，先来一个栗子热热身.
```kotlin
      val codeblock = { f: Float, s: String ->
          println("hello $s")
          f.toByte()
      }

```
0. 以上的codeblock就是一个代码块，也可以理解成一个方法，它的两个参数分别为Float,String
0. 它的返回类型是Byte


val codeblock暴露了它是一种类型，那它是什么类型呢？打印它
```kotlin
println(codeblock.javaClass)

输出：class com.seekting.kotlindemo.PersonKt$main$codeblock$1
```
我们可以理解成它是一个内部类，且名字叫1


那它有什么方法呢？打印它
```java
   for (m in codeblock.javaClass.methods) {
        println(m.name)

    }
  输出：  
    invoke
    invoke
    toString
    getArity
    wait
    wait
    wait
    equals
    hashCode
    getClass
    notify
    notifyAll
```

你会发现比其它类多了些什么，那我新建一个Test类，看看它会有什么方法

```java
class Test() {

}
 val t = Test()
    for (m in t.javaClass.methods) {
        println("test:${m.name}")
    }

打印结果：

test:wait
test:wait
test:wait
test:equals
test:toString
test:hashCode
test:getClass
test:notify
test:notifyAll
```

可以看出多了三个方法getArity，invoke，invoke

从这点可以看出，lambda表达就是一个类，它有invoke方法，那我们通过反射调一下吧

```kotlin
    for (m in codeblock.javaClass.methods) {
        if (m.name == "invoke") {
            val a = m.invoke(codeblock, 1.2f, "tt")
            println("a=$a")
        } else if (m.name == "getArity") {
            val a = m.invoke(codeblock)
            println("a=$a")
        }

    }
    
    输出:
    hello tt
    a=1
    hello tt
    a=1
    a=2

```

通过输出发现调两个invoke调用了代码块:

```kotlin
 println("hello $s")
          f.toByte()
```
并通过代码块返回了1
那getArity返回2是几个意思年，arity是参数数量的意思，难道和参数列表有关？改代码块试试
```kotlin
    for (m in codeblock.javaClass.methods) {
        if (m.name == "invoke") {
            val a = m.invoke(codeblock, 1.2f, "tt", true)
            println("a=$a")
        } else if (m.name == "getArity") {
            val a = m.invoke(codeblock)
            println("a=$a")
        }

    }

输出：

hello tt,b=true
a=1
hello tt,b=true
a=1
a=3
```
果然它是参数个数的意思

总结一下，代码块可以理解成java的一个类，且有invoke方法

看看这一行代码：
```kotlin
val result: Byte = codeblock(1.2f, "3")
```
以上代码就是调用了该对象的invoke方法而已。

## 进阶：

我们知道一个方法，它的参数可以是任意类型，那Lambda也是一种类型（变相类型）

申明一个mn方法，它有Int m,Int n,Lambda block三个形参，返回Boolean
你可以认为block是一个类型，此类型有一个invoke方法，它接收Float,String类型的参数，返回Byte (没绕进去吧我的哥)
```kotlin
inline fun mn(n: Int, m: Int, block: (Float, String) -> Byte): Boolean {
    block(3.14f, "PI")//其实是调用对象的invoke方法
    return m * n > 0

}

mn(1, 2, { f: Float, s: String ->
    val result = "i=${f}s=$s"
    print(result)
    f.toByte()//最后一行就是返回

})

```

1. mn为一个方法Boolean是它的返回
1. mn有三个参数:m,n,代码块block
1. 代码块的的返回类型是Byte类型
1. 这个代码块需要调用的人实现，但是这个代码块依赖两个参数Float,String
1. 实现block代码块的人会得到Float，String参数，可能通过这两个参数操作，为result服务
1. block的Float,String两个参数由mn方法提供

## 解迷Standard.kt里的一些函数库

### run

```kotlin
 run {
        println("run!")
    }

```
这是系统里自带的库函数，看起来挺厉害的样子，来分析一下怎么实现的吧

```kotlin
@kotlin.internal.InlineOnly
public inline fun <R> run(block: () -> R): R = block()

```

1. 泛型了一个R
0. run方法有一个变相类型（Lambda）
0. R=block()可以理解为
```java
{
 return block.invoke()
}

```

你一定会好奇:run方法明明有一个参数，为何不见了呢？
如果最后一个参数是block()可以用大括号括起来.我们也可以写一个这样的方法myRun

```kotlin
fun myRun(t: String, block: (String) -> Unit) {
    block(t)
}
 myRun("hello") {
        println(it)
    }

```

但是如果是这样呢？它要两个block，这个时候就只能是第一个block1通过传参数
```kotlin
fun myRun1(t: String, block: (String) -> Unit, block1: (String) -> Unit) {
    block(t)
}
 val block1 = { s: String ->
        println(s)
    }
myRun1("hello", block1) {

}
``` 
### let
```kotlin
public inline fun <T, R> T.let(block: (T) -> R): R = block(this)

```
1. T,R泛型，代表任何对象都可以调用let方法
0. let方法有一个参数是变相类型（Lambda）参数是T,返回是R
0. 方法实体是调用了这个Lambda的invoke方法，并把T当成参数传进去
以下是let的例子
```kotlin
 "seekting".let {
        println(it.get(0))
    }
    val person = Person1("seekting")
    person.let {
        it.say()
        it.sleep()
        it.wakeup()
    }

```

再看with函数
```kotlin
public inline fun <T, R> with(receiver: T, block: T.() -> R): R = receiver.block()

```
1. 泛型T,R
1. 第一个参数是一个对象，第二个参数是（Lambda）该Lambda有点特别:
```kotlin
block: T.() -> R

```
T.()这个代码初看无法理解。通过IDE我偶然发现了，其实T.()是T类的方法的扩展，还记得有这样的代码
```kotlin
public inline fun String.toUpperCase(): String 

```
它扩展了String的方法，但是这个方法不是String类自己的，可以理解成
```java
public static String toUpperCase(String input){

}

```
我用block:Person.()来打比方,它只有一个函数print
```kotlin
 class Person(
        var id: Long,
        var name: String,
        var age: Int

) {
    fun print() {
        var ss = toString()
        Log.d("seekting", ss)
    }
}


fun study(person: Person, block: Person.() -> Unit) {
    person.print()
    person.block()
}
```

但是在study方法里可以调用Person.block方法，说明什么？
说明这个Person扩展了一个方法叫block，而它的实现:
```kotlin
  study(person1) {
        person1.age = 11
        person1.name = "11"
    }

```

你可以理解为有一个方法：
```kotlin
fun Person.block(){
    this.age = 11
    this.name = "11"
}

```

回过头来看with

```kotlin
public inline fun <T, R> with(receiver: T, block: T.() -> R): R = receiver.block()


with(person){
    person1.age = 11
    person1.name = "11"
}
```

1. 它有一个T类型，并扩展了一个函数叫block
1. receiver.block()表示t调用了一个扩展了的函数(但只在with里可见)
1. 而这个block()函数怎么实现的，外部实现的；实现体就是
```kotlin
person1.age = 11
person1.name = "11"

```
### apply

```kotlin
public inline fun <T> T.apply(block: T.() -> Unit): T { block(); return this }

```
1. 任意对象都有一个apply方法，还有一个block方法（只在apply里可见）
1. apply方法调用block方法，block方法由外部自己实现，实现完返回该对象
1. 对比let:任意对象（T）有一个let方法，该方法能把T对象当成参数处理并返block的返回类型 
1. 它们的共同点都会调用block代码块，但是不一样的是:let返回的是block返回类型，apply返回的是原对象
```kotlin
public inline fun <T, R> T.let(block: (T) -> R): R = block(this)

```

### also，apply和also的区别

```kotlin
public inline fun <T> T.also(block: (T) -> Unit): T { block(this); return this }

```

also和apply类似，唯一不一样的是:

1. apply是调用对象的扩展block()方法，因此在block里可以用this,而不能用it

1. 而also是调用一个block(T t)方法，因此block里不能用this,而能用it

also 与apply的不同之处:
```kotlin
 person1.apply {
        this.age

    }
person1.also {
    it.age = 11
}

person1.also { p: Person ->
    p.age = 11
    //this.age=11 报错
}

```

