package com.zoonza.wishfy.member.internal.domain

data class Nickname(val value: String) {
    init {
        require(PATTERN.matches(value)) {
            "사용자 이름은 한글, 영문, 숫자, 언더바(_)로 구성된 1~${MAX_LENGTH}자여야 합니다."
        }
    }

    companion object {
        private const val MAX_LENGTH = 21
        private val PATTERN = Regex("^[가-힣a-zA-Z0-9_]{1,$MAX_LENGTH}$")
    }
}

