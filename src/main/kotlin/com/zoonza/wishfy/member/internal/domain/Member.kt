package com.zoonza.wishfy.member.internal.domain

import com.zoonza.wishfy.common.error.BusinessException
import java.time.Instant

class Member(
    val id: Long = 0L,
    val loginId: LoginId,
    var encodedPassword: String,
    var nickname: Nickname,
    val role: MemberRole,
    val createdAt: Instant,
    var updatedAt: Instant
) {
    fun changeNickname(newNickname: Nickname) {
        this.nickname = newNickname
        this.updatedAt = Instant.now()
    }

    fun changePassword(
        currentPassword: String,
        newPassword: RawPassword,
        passwordEncoder: PasswordEncoder
    ) {
        if (!verifyPassword(currentPassword, passwordEncoder)) {
            throw BusinessException(MemberErrorCode.CURRENT_PASSWORD_MISMATCH)
        }

        this.encodedPassword = passwordEncoder.encode(newPassword.value)
        this.updatedAt = Instant.now()
    }

    fun verifyPassword(input: String, passwordEncoder: PasswordEncoder): Boolean =
        passwordEncoder.matches(input, this.encodedPassword)

    companion object {
        fun register(
            loginId: LoginId,
            rawPassword: RawPassword,
            nickname: Nickname,
            passwordEncoder: PasswordEncoder
        ): Member {
            val now = Instant.now()
            return Member(
                loginId = loginId,
                encodedPassword = passwordEncoder.encode(rawPassword.value),
                nickname = nickname,
                role = MemberRole.MEMBER,
                createdAt = now,
                updatedAt = now
            )
        }
    }
}