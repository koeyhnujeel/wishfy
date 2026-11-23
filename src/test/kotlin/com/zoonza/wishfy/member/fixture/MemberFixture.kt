package com.zoonza.wishfy.member.fixture

import com.zoonza.wishfy.member.domain.LoginId
import com.zoonza.wishfy.member.domain.Member
import com.zoonza.wishfy.member.domain.Nickname
import java.time.Instant

object MemberFixture {
    fun member(
        id: Long = 1L,
        loginId: String = "zoonza",
        encodedPassword: String = "encoded:password1234",
        nickname: String = "준자",
        createdAt: Instant = Instant.parse("2026-11-01T00:00:00Z"),
        updatedAt: Instant = createdAt
    ): Member = Member(
        id = id,
        loginId = LoginId(loginId),
        encodedPassword = encodedPassword,
        nickname = Nickname(nickname),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
