package com.zoonza.wishfy.member.domain

import com.zoonza.wishfy.member.fake.FakePasswordEncoder
import com.zoonza.wishfy.member.fixture.MemberFixture.member
import io.kotest.matchers.comparables.shouldBeGreaterThan
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

    @Test
    fun `닉네임을 변경하면 새 닉네임으로 바뀐다`() {
        val member = member(nickname = "준자")

        member.changeNickname(Nickname("새닉네임"))

        member.nickname shouldBe Nickname("새닉네임")
    }

    @Test
    fun `닉네임을 변경하면 수정 시각이 갱신된다`() {
        val member = member()
        val beforeUpdatedAt = member.updatedAt

        member.changeNickname(Nickname("새닉네임"))

        member.updatedAt shouldBeGreaterThan beforeUpdatedAt
    }

    @Test
    fun `닉네임을 변경해도 생성 시각은 바뀌지 않는다`() {
        val member = member()
        val createdAt = member.createdAt

        member.changeNickname(Nickname("새닉네임"))

        member.createdAt shouldBe createdAt
    }
}
