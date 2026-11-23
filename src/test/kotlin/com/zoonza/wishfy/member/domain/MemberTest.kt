package com.zoonza.wishfy.member.domain

import com.zoonza.wishfy.member.fake.FakePasswordEncoder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class MemberTest {

    private val passwordEncoder = FakePasswordEncoder()

    private fun register(): Member = Member.register(
        loginId = LoginId("zoonza"),
        rawPassword = "password1234",
        nickname = Nickname("준자"),
        passwordEncoder = passwordEncoder
    )

    @Test
    fun `회원가입하면 입력한 아이디와 닉네임을 가진 회원이 생성된다`() {
        val member = register()

        member.loginId shouldBe LoginId("zoonza")
        member.nickname shouldBe Nickname("준자")
    }

    @Test
    fun `회원가입하면 비밀번호는 원문이 아닌 인코딩된 값으로 저장된다`() {
        val member = register()

        member.encodedPassword shouldNotBe "password1234"
        passwordEncoder.matches("password1234", member.encodedPassword) shouldBe true
    }

    @Test
    fun `회원가입 직후에는 생성 시각과 수정 시각이 같다`() {
        val member = register()

        member.createdAt shouldBe member.updatedAt
    }

    @Test
    fun `회원가입 직후에는 아직 식별자가 부여되지 않는다`() {
        val member = register()

        member.id shouldBe 0L
    }
}
