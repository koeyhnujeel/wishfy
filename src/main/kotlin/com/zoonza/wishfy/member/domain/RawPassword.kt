package com.zoonza.wishfy.member.domain

data class RawPassword(val value: String) {
    init {
        require(PATTERN.matches(value)) {
            "비밀번호는 영문자와 숫자를 각각 하나 이상 포함한 ${MIN_LENGTH}~${MAX_LENGTH}자여야 합니다."
        }
    }

    override fun toString(): String = "RawPassword(****)"

    companion object {
        private const val MIN_LENGTH = 6
        private const val MAX_LENGTH = 21
        private val PATTERN = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{$MIN_LENGTH,$MAX_LENGTH}$")
    }
}
