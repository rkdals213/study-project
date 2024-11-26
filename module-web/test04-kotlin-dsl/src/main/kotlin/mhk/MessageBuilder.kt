package mhk

fun message(sender: String, content: String, block: Message.() -> Unit): Message {
    val message = Message(sender, content)
    message.block()
    return message
}
