package com.zoonza.wishfy.member.internal.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotContain
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class RawPasswordTest {

    @ParameterizedTest
    @ValueSource(strings = ["password1234", "1234abcd", "Pass1234", "a1b2c3"])
    fun `영문자와 숫자가 모두 포함된 비밀번호를 생성할 수 있다`(value: String) {
        RawPassword(value).value shouldBe value
    }

    @Test
    fun `6자 비밀번호는 생성할 수 있다`() {
        RawPassword("abc123").value shouldBe "abc123"
    }

    @Test
    fun `21자 비밀번호는 생성할 수 있다`() {
        val value = "a".repeat(20) + "1"

        RawPassword(value).value shouldBe value
    }

    @Test
    fun `5자 이하 비밀번호는 생성할 수 없다`() {
        shouldThrow<IllegalArgumentException> { RawPassword("abc12") }
    }

    @Test
    fun `22자 이상 비밀번호는 생성할 수 없다`() {
        shouldThrow<IllegalArgumentException> { RawPassword("a".repeat(21) + "1") }
    }

    @Test
    fun `빈 문자열로는 비밀번호를 생성할 수 없다`() {
        shouldThrow<IllegalArgumentException> { RawPassword("") }
    }

    @ParameterizedTest
    @ValueSource(strings = ["password", "PASSWORD", "abcdefgh"])
    fun `영문자로만 된 비밀번호는 생성할 수 없다`(value: String) {
        shouldThrow<IllegalArgumentException> { RawPassword(value) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["123456", "12345678"])
    fun `숫자로만 된 비밀번호는 생성할 수 없다`(value: String) {
        shouldThrow<IllegalArgumentException> { RawPassword(value) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["pass1234!", "pass_1234", "pass 1234", "비밀번호1234"])
    fun `특수문자나 공백 한글이 포함된 비밀번호는 생성할 수 없다`(value: String) {
        shouldThrow<IllegalArgumentException> { RawPassword(value) }
    }

    @Test
    fun `문자열로 출력할 때 비밀번호 원문이 노출되지 않는다`() {
        RawPassword("password1234").toString() shouldNotContain "password1234"
    }
}
