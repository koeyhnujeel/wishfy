package com.zoonza.wishfy.member.domain

data class LoginId(val value: String) {
    init {
        require(PATTERN.matches(value)) {
            "아이디는 영문자를 하나 이상 포함하고, 영문자와 숫자로 구성된 ${MIN_LENGTH}~${MAX_LENGTH}자여야 합니다."
        }
    }

    companion object {
        private const val MIN_LENGTH = 4
        private const val MAX_LENGTH = 20
        private val PATTERN = Regex("^(?=.*[a-z])[a-z0-9]{${MIN_LENGTH},${MAX_LENGTH}}$")
    }
}
