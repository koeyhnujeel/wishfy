package com.zoonza.wishfy.member.application

import com.zoonza.wishfy.common.error.BusinessException
import com.zoonza.wishfy.member.application.dto.RegisterMemberCommand
import com.zoonza.wishfy.member.application.dto.UpdateNicknameCommand
import com.zoonza.wishfy.member.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    @Transactional
    fun updateNickname(command: UpdateNicknameCommand) {
        val newNickname = Nickname(command.newNickname)
        val member = memberRepository.findById(command.memberId)
            ?: throw BusinessException(MemberErrorCode.NOT_FOUND)

        member.changeNickname(newNickname)
    }

    private fun requireUniqueLoginId(loginId: String): LoginId {
        val loginId = LoginId(loginId.lowercase())

        if (memberRepository.existsByLoginId(loginId)) {
            throw BusinessException(MemberErrorCode.LOGIN_ID_ALREADY_EXISTS)
        }

        return loginId
    }
}