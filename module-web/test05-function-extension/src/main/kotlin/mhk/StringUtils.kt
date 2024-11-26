package mhk

import java.util.regex.Pattern

object StringUtils {
    private const val PHONE_NUMBER_REGEX = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})\$"

    fun String.maskingPhoneNumber(): String {
        val matcher = Pattern.compile(PHONE_NUMBER_REGEX).matcher(this)

        return if (matcher.find()) {
            val maskingText = StringBuffer()
            val target = matcher.group(2) ?: ""

            val length = target.length
            for (i in 0 until length) {
                maskingText.append("*")
            }

            this.replace(target, maskingText.toString())
        } else {
            this
        }
    }
}
