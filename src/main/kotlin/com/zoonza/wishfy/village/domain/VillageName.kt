package com.zoonza.wishfy.village.domain

data class VillageName(val value: String) {
    init {
        require(PATTERN.matches(value)) {
            "마을 이름은 한글, 영문, 숫자로 구성된 ${MIN_LENGTH}~${MAX_LENGTH}자여야 합니다."
        }
    }

    companion object {
        private const val MIN_LENGTH = 1
        private const val MAX_LENGTH = 11
        private val PATTERN = Regex("^[가-힣a-zA-Z0-9]{$MIN_LENGTH,$MAX_LENGTH}$")
    }
}
