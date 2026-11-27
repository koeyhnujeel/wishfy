package com.zoonza.wishfy.member.internal.domain

@ConsistentCopyVisibility
data class LoginId private constructor(val value: String) {
    init {
        require(PATTERN.matches(value)) {
            "아이디는 영문자를 하나 이상 포함하고, 영문자와 숫자로 구성된 ${MIN_LENGTH}~${MAX_LENGTH}자여야 합니다."
        }
    }

    companion object {
        private const val MIN_LENGTH = 4
        private const val MAX_LENGTH = 20
        private val PATTERN = Regex("^(?=.*[a-z])[a-z0-9]{${MIN_LENGTH},${MAX_LENGTH}}$")

        private fun from(value: String): LoginId = LoginId(value.lowercase())

        operator fun invoke(value: String): LoginId = from(value)

        fun parseOrNull(value: String): LoginId? {
            val normalized = value.lowercase()
            return if (PATTERN.matches(normalized)) LoginId(normalized) else null
        }
    }
}
