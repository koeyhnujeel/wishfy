package com.zoonza.wishfy.member.domain

import java.time.Instant

class Member(
    val id: Long = 0L,
    val loginId: LoginId,
    var encodedPassword: String,
    var nickname: Nickname,
    val createdAt: Instant,
    var updatedAt: Instant
) {
    fun changeNickname(newNickname: Nickname) {
        this.nickname = newNickname
        this.updatedAt = Instant.now()
    }

    companion object {
        fun register(
            loginId: LoginId,
            rawPassword: String,
            nickname: Nickname,
            passwordEncoder: PasswordEncoder
        ): Member {
            val now = Instant.now()
            return Member(
                loginId = loginId,
                encodedPassword = passwordEncoder.encode(rawPassword),
                nickname = nickname,
                createdAt = now,
                updatedAt = now
            )
        }
    }
}