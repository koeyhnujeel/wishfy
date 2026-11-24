package com.zoonza.wishfy.member.domain

import com.zoonza.wishfy.common.error.BusinessException
import com.zoonza.wishfy.member.fake.FakePasswordEncoder
import com.zoonza.wishfy.member.fixture.MemberFixture.member
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class MemberTest {

    private val passwordEncoder = FakePasswordEncoder()

    private fun register(): Member = Member.register(
        loginId = LoginId("zoonza"),
        rawPassword = RawPassword("password1234"),
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

    private fun memberWithPassword(rawPassword: String): Member =
        member(encodedPassword = passwordEncoder.encode(rawPassword))

    @Test
    fun `현재 비밀번호와 일치하면 비밀번호 검증에 성공한다`() {
        val member = memberWithPassword("oldPassword1234")

        member.verifyPassword("oldPassword1234", passwordEncoder) shouldBe true
    }

    @Test
    fun `현재 비밀번호와 다르면 비밀번호 검증에 실패한다`() {
        val member = memberWithPassword("oldPassword1234")

        member.verifyPassword("wrongPassword1234", passwordEncoder) shouldBe false
    }

    @Test
    fun `비밀번호를 변경하면 새 비밀번호가 인코딩되어 저장된다`() {
        val member = memberWithPassword("oldPassword1234")

        member.changePassword("oldPassword1234", RawPassword("newPassword1234"), passwordEncoder)

        member.encodedPassword shouldNotBe "newPassword1234"
        member.verifyPassword("newPassword1234", passwordEncoder) shouldBe true
    }

    @Test
    fun `비밀번호를 변경하면 기존 비밀번호로는 일치하지 않는다`() {
        val member = memberWithPassword("oldPassword1234")

        member.changePassword("oldPassword1234", RawPassword("newPassword1234"), passwordEncoder)

        member.verifyPassword("oldPassword1234", passwordEncoder) shouldBe false
    }

    @Test
    fun `비밀번호를 변경하면 수정 시각이 갱신된다`() {
        val member = memberWithPassword("oldPassword1234")
        val beforeUpdatedAt = member.updatedAt

        member.changePassword("oldPassword1234", RawPassword("newPassword1234"), passwordEncoder)

        member.updatedAt shouldBeGreaterThan beforeUpdatedAt
    }

    @Test
    fun `현재 비밀번호가 틀리면 비밀번호를 변경할 수 없다`() {
        val member = memberWithPassword("oldPassword1234")

        val exception = shouldThrow<BusinessException> {
            member.changePassword("wrongPassword1234", RawPassword("newPassword1234"), passwordEncoder)
        }

        exception.errorCode shouldBe MemberErrorCode.CURRENT_PASSWORD_MISMATCH
    }

    @Test
    fun `현재 비밀번호가 틀리면 기존 비밀번호와 수정 시각이 그대로 유지된다`() {
        val member = memberWithPassword("oldPassword1234")
        val beforeEncodedPassword = member.encodedPassword
        val beforeUpdatedAt = member.updatedAt

        runCatching {
            member.changePassword("wrongPassword1234", RawPassword("newPassword1234"), passwordEncoder)
        }

        member.encodedPassword shouldBe beforeEncodedPassword
        member.updatedAt shouldBe beforeUpdatedAt
    }

    @Test
    fun `현재 비밀번호가 비밀번호 형식에 맞지 않아도 틀린 비밀번호로 처리된다`() {
        val member = memberWithPassword("oldPassword1234")

        val exception = shouldThrow<BusinessException> {
            member.changePassword("abc", RawPassword("newPassword1234"), passwordEncoder)
        }

        exception.errorCode shouldBe MemberErrorCode.CURRENT_PASSWORD_MISMATCH
    }
}
