package mhk

import com.fasterxml.jackson.annotation.JsonCreator

@JvmInline
value class PhoneNumber private constructor(val number: String) {
    companion object {
        private val PHONE_NUMBER_REGEX = Regex("^\\d{2,3}-\\d{3,4}-\\d{4}\$")

        @JsonCreator
        fun from(phoneNumber: String): PhoneNumber {
            return PhoneNumber(phoneNumber)
        }

        operator fun invoke(phoneNumber: String): PhoneNumber = from(phoneNumber)

    }

    init {
        validate(number)
    }

    private fun validate(number: String) {
        require(PHONE_NUMBER_REGEX.matches(number)) { "Invalid phone number: $number" }
    }
}
