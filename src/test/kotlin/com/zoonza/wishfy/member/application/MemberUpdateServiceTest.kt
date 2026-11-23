package com.zoonza.wishfy.member.application

import com.zoonza.wishfy.member.domain.LoginId
import com.zoonza.wishfy.member.domain.Nickname
import com.zoonza.wishfy.member.fake.FakeMemberRepository
import com.zoonza.wishfy.member.fake.FakePasswordEncoder
import com.zoonza.wishfy.member.fixture.MemberFixture.member
import com.zoonza.wishfy.member.fixture.RegisterMemberCommandFixture.registerMemberCommand
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MemberUpdateServiceTest {
    private lateinit var memberRepository: FakeMemberRepository
    private lateinit var passwordEncoder: FakePasswordEncoder
    private lateinit var memberUpdateService: MemberUpdateService

    @BeforeEach
    fun setUp() {
        memberRepository = FakeMemberRepository()
        passwordEncoder = FakePasswordEncoder()
        memberUpdateService = MemberUpdateService(passwordEncoder, memberRepository)
    }

    @Test
    fun `회원가입하면 회원이 저장된다`() {
        memberUpdateService.register(registerMemberCommand(loginId = "zoonza", nickname = "준자"))

        val saved = memberRepository.findByLoginId(LoginId("zoonza")).shouldNotBeNull()
        saved.nickname shouldBe Nickname("준자")
    }

    @Test
    fun `회원가입하면 비밀번호가 인코딩되어 저장된다`() {
        memberUpdateService.register(registerMemberCommand(loginId = "zoonza", rawPassword = "password1234"))

        val saved = memberRepository.findByLoginId(LoginId("zoonza")).shouldNotBeNull()
        passwordEncoder.matches("password1234", saved.encodedPassword) shouldBe true
    }

    @Test
    fun `대문자가 섞인 아이디로 가입하면 소문자로 바뀌어 저장된다`() {
        memberUpdateService.register(registerMemberCommand(loginId = "ZoonZa"))

        memberRepository.findByLoginId(LoginId("zoonza")).shouldNotBeNull()
    }

    @Test
    fun `이미 사용 중인 아이디로는 가입할 수 없다`() {
        memberRepository.save(member(loginId = "zoonza"))

        val exception = shouldThrow<IllegalArgumentException> {
            memberUpdateService.register(registerMemberCommand(loginId = "zoonza"))
        }

        exception.message shouldContain "이미 사용 중인 아이디"
    }

    @Test
    fun `대소문자만 다른 아이디도 이미 사용 중인 아이디로 판단한다`() {
        memberRepository.save(member(loginId = "zoonza"))

        shouldThrow<IllegalArgumentException> {
            memberUpdateService.register(registerMemberCommand(loginId = "ZOONZA"))
        }
    }

    @Test
    fun `아이디가 중복되면 회원이 저장되지 않는다`() {
        memberRepository.save(member(loginId = "zoonza"))

        runCatching { memberUpdateService.register(registerMemberCommand(loginId = "zoonza")) }

        memberRepository.count() shouldBe 1
    }

    @Test
    fun `닉네임이 같아도 아이디가 다르면 가입할 수 있다`() {
        memberRepository.save(member(loginId = "zoonza", nickname = "준자"))

        memberUpdateService.register(registerMemberCommand(loginId = "another", nickname = "준자"))

        memberRepository.count() shouldBe 2
    }

    @Test
    fun `아이디 형식이 올바르지 않으면 가입할 수 없다`() {
        shouldThrow<IllegalArgumentException> {
            memberUpdateService.register(registerMemberCommand(loginId = "zz"))
        }

        memberRepository.count() shouldBe 0
    }

    @Test
    fun `닉네임 형식이 올바르지 않으면 가입할 수 없다`() {
        shouldThrow<IllegalArgumentException> {
            memberUpdateService.register(registerMemberCommand(nickname = "준자!"))
        }

        memberRepository.count() shouldBe 0
    }
}
