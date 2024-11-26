package mhk

/*
    클래스의 변수가 불변값일 경우 dsl 문법으로 초기화가 불가능하다
    그래서 가변 변수를 갖는 Builder 클래스를 이용해 dsl 문법을 사용하고 만들고자 하는 객체로 build 하는 방법이 있다
 */

fun phone(block: PhoneBuilder.() -> Unit): Phone {
    val phoneBuilder = PhoneBuilder()
    phoneBuilder.block()
    return phoneBuilder.build()
}

class PhoneBuilder(
    var number: String = "",
    var name: String = "",
    var image: String = "",
    var shortCutNumber: String = ""
) {
    fun build(): Phone {
        return Phone(number, name, image, shortCutNumber)
    }
}
