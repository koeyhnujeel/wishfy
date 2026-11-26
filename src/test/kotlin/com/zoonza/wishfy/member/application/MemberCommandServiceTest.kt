package com.zoonza.wishfy.member.application

import com.zoonza.wishfy.common.error.BusinessException
import com.zoonza.wishfy.member.domain.LoginId
import com.zoonza.wishfy.member.domain.MemberErrorCode
import com.zoonza.wishfy.member.domain.Nickname
import com.zoonza.wishfy.member.fake.FakeMemberRepository
import com.zoonza.wishfy.member.fake.FakePasswordEncoder
import com.zoonza.wishfy.member.fixture.MemberFixture.member
import com.zoonza.wishfy.member.fixture.RegisterMemberCommandFixture.registerMemberCommand
import com.zoonza.wishfy.member.fixture.UpdateNicknameCommandFixture.updateNicknameCommand
import com.zoonza.wishfy.member.fixture.UpdatePasswordCommandFixture.updatePasswordCommand
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MemberCommandServiceTest {
    private lateinit var memberRepository: FakeMemberRepository
    private lateinit var passwordEncoder: FakePasswordEncoder
    private lateinit var memberCommandService: MemberCommandService

    @BeforeEach
    fun setUp() {
        memberRepository = FakeMemberRepository()
        passwordEncoder = FakePasswordEncoder()
        memberCommandService = MemberCommandService(passwordEncoder, memberRepository)
    }

    @Test
    fun `회원가입하면 회원이 저장된다`() {
        memberCommandService.register(registerMemberCommand(loginId = "zoonza", nickname = "준자"))

        val saved = memberRepository.findByLoginId(LoginId("zoonza")).shouldNotBeNull()
        saved.nickname shouldBe Nickname("준자")
    }

    @Test
    fun `회원가입하면 비밀번호가 인코딩되어 저장된다`() {
        memberCommandService.register(registerMemberCommand(loginId = "zoonza", rawPassword = "password1234"))

        val saved = memberRepository.findByLoginId(LoginId("zoonza")).shouldNotBeNull()
        passwordEncoder.matches("password1234", saved.encodedPassword) shouldBe true
    }

    @Test
    fun `대문자가 섞인 아이디로 가입하면 소문자로 바뀌어 저장된다`() {
        memberCommandService.register(registerMemberCommand(loginId = "ZoonZa"))

        memberRepository.findByLoginId(LoginId("zoonza")).shouldNotBeNull()
    }

    @Test
    fun `이미 사용 중인 아이디로는 가입할 수 없다`() {
        memberRepository.save(member(loginId = "zoonza"))

        val exception = shouldThrow<BusinessException> {
            memberCommandService.register(registerMemberCommand(loginId = "zoonza"))
        }

        exception.errorCode shouldBe MemberErrorCode.LOGIN_ID_ALREADY_EXISTS
    }

    @Test
    fun `대소문자만 다른 아이디도 이미 사용 중인 아이디로 판단한다`() {
        memberRepository.save(member(loginId = "zoonza"))

        val exception = shouldThrow<BusinessException> {
            memberCommandService.register(registerMemberCommand(loginId = "ZOONZA"))
        }

        exception.errorCode shouldBe MemberErrorCode.LOGIN_ID_ALREADY_EXISTS
    }

    @Test
    fun `아이디가 중복되면 회원이 저장되지 않는다`() {
        memberRepository.save(member(loginId = "zoonza"))

        runCatching { memberCommandService.register(registerMemberCommand(loginId = "zoonza")) }

        memberRepository.count() shouldBe 1
    }

    @Test
    fun `닉네임이 같아도 아이디가 다르면 가입할 수 있다`() {
        memberRepository.save(member(loginId = "zoonza", nickname = "준자"))

        memberCommandService.register(registerMemberCommand(loginId = "another", nickname = "준자"))

        memberRepository.count() shouldBe 2
    }

    @Test
    fun `아이디 형식이 올바르지 않으면 가입할 수 없다`() {
        shouldThrow<IllegalArgumentException> {
            memberCommandService.register(registerMemberCommand(loginId = "zz"))
        }

        memberRepository.count() shouldBe 0
    }

    @Test
    fun `닉네임 형식이 올바르지 않으면 가입할 수 없다`() {
        shouldThrow<IllegalArgumentException> {
            memberCommandService.register(registerMemberCommand(nickname = "준자!"))
        }

        memberRepository.count() shouldBe 0
    }

    @Test
    fun `닉네임을 변경하면 회원의 닉네임이 바뀐다`() {
        memberRepository.save(member(id = 1L, nickname = "준자"))

        memberCommandService.updateNickname(updateNicknameCommand(memberId = 1L, newNickname = "새닉네임"))

        memberRepository.findById(1L).shouldNotBeNull().nickname shouldBe Nickname("새닉네임")
    }

    @Test
    fun `다른 회원과 같은 닉네임으로도 변경할 수 있다`() {
        memberRepository.save(member(id = 1L, loginId = "zoonza", nickname = "준자"))
        memberRepository.save(member(id = 2L, loginId = "another", nickname = "다른사람"))

        memberCommandService.updateNickname(updateNicknameCommand(memberId = 2L, newNickname = "준자"))

        memberRepository.findById(2L).shouldNotBeNull().nickname shouldBe Nickname("준자")
    }

    @Test
    fun `존재하지 않는 회원의 닉네임은 변경할 수 없다`() {
        val exception = shouldThrow<BusinessException> {
            memberCommandService.updateNickname(updateNicknameCommand(memberId = 999L))
        }

        exception.errorCode shouldBe MemberErrorCode.NOT_FOUND
    }

    @Test
    fun `새 닉네임 형식이 올바르지 않으면 닉네임이 변경되지 않는다`() {
        memberRepository.save(member(id = 1L, nickname = "준자"))

        shouldThrow<IllegalArgumentException> {
            memberCommandService.updateNickname(updateNicknameCommand(memberId = 1L, newNickname = "준자!"))
        }

        memberRepository.findById(1L).shouldNotBeNull().nickname shouldBe Nickname("준자")
    }

    @Test
    fun `비밀번호 형식이 올바르지 않으면 가입할 수 없다`() {
        shouldThrow<IllegalArgumentException> {
            memberCommandService.register(registerMemberCommand(rawPassword = "password"))
        }

        memberRepository.count() shouldBe 0
    }

    @Test
    fun `현재 비밀번호가 맞으면 새 비밀번호로 변경된다`() {
        memberRepository.save(member(id = 1L, encodedPassword = passwordEncoder.encode("oldPassword1234")))

        memberCommandService.updatePassword(
            updatePasswordCommand(memberId = 1L, currentPassword = "oldPassword1234", newPassword = "newPassword1234")
        )

        val member = memberRepository.findById(1L).shouldNotBeNull()
        passwordEncoder.matches("newPassword1234", member.encodedPassword) shouldBe true
        passwordEncoder.matches("oldPassword1234", member.encodedPassword) shouldBe false
    }

    @Test
    fun `현재 비밀번호가 틀리면 비밀번호를 변경할 수 없다`() {
        memberRepository.save(member(id = 1L, encodedPassword = passwordEncoder.encode("oldPassword1234")))

        val exception = shouldThrow<BusinessException> {
            memberCommandService.updatePassword(
                updatePasswordCommand(memberId = 1L, currentPassword = "wrongPassword1234")
            )
        }

        exception.errorCode shouldBe MemberErrorCode.CURRENT_PASSWORD_MISMATCH
        val member = memberRepository.findById(1L).shouldNotBeNull()
        passwordEncoder.matches("oldPassword1234", member.encodedPassword) shouldBe true
    }

    @Test
    fun `현재 비밀번호를 형식에 맞지 않게 입력해도 현재 비밀번호 불일치로 처리된다`() {
        memberRepository.save(member(id = 1L, encodedPassword = passwordEncoder.encode("oldPassword1234")))

        val exception = shouldThrow<BusinessException> {
            memberCommandService.updatePassword(updatePasswordCommand(memberId = 1L, currentPassword = "abc"))
        }

        exception.errorCode shouldBe MemberErrorCode.CURRENT_PASSWORD_MISMATCH
    }

    @Test
    fun `새 비밀번호 형식이 올바르지 않으면 비밀번호가 변경되지 않는다`() {
        memberRepository.save(member(id = 1L, encodedPassword = passwordEncoder.encode("oldPassword1234")))

        shouldThrow<IllegalArgumentException> {
            memberCommandService.updatePassword(
                updatePasswordCommand(memberId = 1L, currentPassword = "oldPassword1234", newPassword = "123456")
            )
        }

        val member = memberRepository.findById(1L).shouldNotBeNull()
        passwordEncoder.matches("oldPassword1234", member.encodedPassword) shouldBe true
    }

    @Test
    fun `존재하지 않는 회원의 비밀번호는 변경할 수 없다`() {
        val exception = shouldThrow<BusinessException> {
            memberCommandService.updatePassword(updatePasswordCommand(memberId = 999L))
        }

        exception.errorCode shouldBe MemberErrorCode.NOT_FOUND
    }
}
