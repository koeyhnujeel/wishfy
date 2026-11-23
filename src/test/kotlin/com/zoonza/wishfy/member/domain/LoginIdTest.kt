package com.zoonza.wishfy.member.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LoginIdTest {

    @Test
    fun `영문 소문자와 숫자로 된 아이디를 생성할 수 있다`() {
        LoginId("zoonza123").value shouldBe "zoonza123"
    }

    @Test
    fun `영문자가 포함되어 있으면 숫자로 시작하는 아이디도 생성할 수 있다`() {
        LoginId("123abc").value shouldBe "123abc"
    }

    @ParameterizedTest
    @ValueSource(strings = ["1234", "01012345678", "20261123"])
    fun `숫자로만 된 아이디는 생성할 수 없다`(value: String) {
        shouldThrow<IllegalArgumentException> { LoginId(value) }
    }

    @Test
    fun `4자 아이디는 생성할 수 있다`() {
        LoginId("abcd").value shouldBe "abcd"
    }

    @Test
    fun `20자 아이디는 생성할 수 있다`() {
        val value = "a".repeat(20)

        LoginId(value).value shouldBe value
    }

    @Test
    fun `3자 이하 아이디는 생성할 수 없다`() {
        shouldThrow<IllegalArgumentException> { LoginId("abc") }
    }

    @Test
    fun `21자 이상 아이디는 생성할 수 없다`() {
        shouldThrow<IllegalArgumentException> { LoginId("a".repeat(21)) }
    }

    @Test
    fun `빈 문자열로는 아이디를 생성할 수 없다`() {
        shouldThrow<IllegalArgumentException> { LoginId("") }
    }

    @ParameterizedTest
    @ValueSource(strings = ["Zoonza", "ZOONZA", "zoonzA"])
    fun `영문 대문자가 포함된 아이디는 생성할 수 없다`(value: String) {
        shouldThrow<IllegalArgumentException> { LoginId(value) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["준자zoonza", "zoon_za", "zoon-za", "zoon.za", "zoon za", "zoonza!"])
    fun `한글이나 특수문자 공백이 포함된 아이디는 생성할 수 없다`(value: String) {
        shouldThrow<IllegalArgumentException> { LoginId(value) }
    }

    @Test
    fun `값이 같은 아이디는 동등하다`() {
        LoginId("zoonza") shouldBe LoginId("zoonza")
    }
}
