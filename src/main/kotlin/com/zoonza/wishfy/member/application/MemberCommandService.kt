package com.zoonza.wishfy.member.application

import com.zoonza.wishfy.common.error.BusinessException
import com.zoonza.wishfy.member.application.dto.RegisterMemberCommand
import com.zoonza.wishfy.member.application.dto.UpdateNicknameCommand
import com.zoonza.wishfy.member.application.dto.UpdatePasswordCommand
import com.zoonza.wishfy.member.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberCommandService(
    private val passwordEncoder: PasswordEncoder,
    private val memberRepository: MemberRepository
) {
    fun register(command: RegisterMemberCommand) {
        val loginId = requireUniqueLoginId(command.loginId)
        val rawPassword = RawPassword(command.rawPassword)
        val nickname = Nickname(command.nickname)
        val member = Member.register(loginId, rawPassword, nickname, passwordEncoder)

        memberRepository.save(member)
    }

    @Transactional
    fun updateNickname(command: UpdateNicknameCommand) {
        val member = getMemberBy(command.memberId)
        val newNickname = Nickname(command.newNickname)

        member.changeNickname(newNickname)
    }

    @Transactional
    fun updatePassword(command: UpdatePasswordCommand) {
        val member = getMemberBy(command.memberId)
        val newPassword = RawPassword(command.newPassword)

        member.changePassword(command.currentPassword, newPassword, passwordEncoder)
    }

    private fun requireUniqueLoginId(loginId: String): LoginId {
        val loginId = LoginId(loginId)

        if (memberRepository.existsByLoginId(loginId)) {
            throw BusinessException(MemberErrorCode.LOGIN_ID_ALREADY_EXISTS)
        }

        return loginId
    }

    private fun getMemberBy(memberId: Long): Member =
        memberRepository.findById(memberId)
            ?: throw BusinessException(MemberErrorCode.NOT_FOUND)
}