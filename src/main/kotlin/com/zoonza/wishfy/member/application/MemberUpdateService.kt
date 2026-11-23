package com.zoonza.wishfy.member.application

import com.zoonza.wishfy.member.application.dto.RegisterMemberCommand
import com.zoonza.wishfy.member.domain.LoginId
import com.zoonza.wishfy.member.domain.Member
import com.zoonza.wishfy.member.domain.MemberRepository
import com.zoonza.wishfy.member.domain.Nickname
import com.zoonza.wishfy.member.domain.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberUpdateService(
    private val passwordEncoder: PasswordEncoder,
    private val memberRepository: MemberRepository
) {
    fun register(command: RegisterMemberCommand) {
        val loginId = requireUniqueLoginId(command.loginId)
        val rawPassword = command.rawPassword
        val nickname = Nickname(command.nickname)
        val member = Member.register(loginId, rawPassword, nickname, passwordEncoder)

        memberRepository.save(member)
    }

    private fun requireUniqueLoginId(loginId: String): LoginId {
        val loginId = LoginId(loginId.lowercase())

        if (memberRepository.existsByLoginId(loginId)) {
            throw IllegalArgumentException("이미 사용 중인 아이디입니다.")
        }

        return loginId
    }
}