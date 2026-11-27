package com.zoonza.wishfy.member.internal.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class NicknameTest {

    @ParameterizedTest
    @ValueSource(strings = ["준자", "zoonza", "Zoonza", "준자123", "준자_zoonza", "_"])
    fun `한글 영문 숫자 언더바로 된 닉네임을 생성할 수 있다`(value: String) {
        Nickname(value).value shouldBe value
    }

    @Test
    fun `1자 닉네임은 생성할 수 있다`() {
        Nickname("준").value shouldBe "준"
    }

    @Test
    fun `21자 닉네임은 생성할 수 있다`() {
        val value = "가".repeat(21)

        Nickname(value).value shouldBe value
    }

    @Test
    fun `22자 이상 닉네임은 생성할 수 없다`() {
        shouldThrow<IllegalArgumentException> { Nickname("가".repeat(22)) }
    }

    @Test
    fun `빈 문자열로는 닉네임을 생성할 수 없다`() {
        shouldThrow<IllegalArgumentException> { Nickname("") }
    }

    @ParameterizedTest
    @ValueSource(strings = ["준-자", "준자!", "준.자", "준자@", "준 자"])
    fun `언더바 이외의 특수문자나 공백이 포함된 닉네임은 생성할 수 없다`(value: String) {
        shouldThrow<IllegalArgumentException> { Nickname(value) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["ㅈㅈ", "준ㅏ", "ㅋㅋㅋ"])
    fun `완성되지 않은 한글 자모가 포함된 닉네임은 생성할 수 없다`(value: String) {
        shouldThrow<IllegalArgumentException> { Nickname(value) }
    }

    @Test
    fun `값이 같은 닉네임은 동등하다`() {
        Nickname("준자") shouldBe Nickname("준자")
    }
}
