package com.zoonza.wishfy.village.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class VillageNameTest {

    @ParameterizedTest
    @ValueSource(strings = ["행복마을", "Happy", "마을1", "Village123", "우리Village1"])
    fun `한글 영문 숫자로 된 마을 이름을 생성할 수 있다`(value: String) {
        VillageName(value).value shouldBe value
    }

    @Test
    fun `1자 마을 이름은 생성할 수 있다`() {
        VillageName("집").value shouldBe "집"
    }

    @Test
    fun `11자 마을 이름은 생성할 수 있다`() {
        val value = "가".repeat(11)

        VillageName(value).value shouldBe value
    }

    @Test
    fun `12자 이상 마을 이름은 생성할 수 없다`() {
        shouldThrow<IllegalArgumentException> { VillageName("가".repeat(12)) }
    }

    @Test
    fun `빈 문자열로는 마을 이름을 생성할 수 없다`() {
        shouldThrow<IllegalArgumentException> { VillageName("") }
    }

    @ParameterizedTest
    @ValueSource(strings = ["행복 마을", " 행복마을", "행복마을 ", "   "])
    fun `공백이 포함된 마을 이름은 생성할 수 없다`(value: String) {
        shouldThrow<IllegalArgumentException> { VillageName(value) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["행복_마을", "행복마을!", "행복-마을", "마을🏠", "행복\n마을"])
    fun `특수문자나 이모지 줄바꿈이 포함된 마을 이름은 생성할 수 없다`(value: String) {
        shouldThrow<IllegalArgumentException> { VillageName(value) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["ㅎㅂ", "행ㅂ마을", "ㅋㅋㅋ"])
    fun `완성되지 않은 한글 자모가 포함된 마을 이름은 생성할 수 없다`(value: String) {
        shouldThrow<IllegalArgumentException> { VillageName(value) }
    }

    @Test
    fun `값이 같은 마을 이름은 동등하다`() {
        VillageName("행복마을") shouldBe VillageName("행복마을")
    }
}
