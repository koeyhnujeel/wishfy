package com.zoonza.wishfy.member.internal.fixture

import com.zoonza.wishfy.member.internal.domain.LoginId
import com.zoonza.wishfy.member.internal.domain.Member
import com.zoonza.wishfy.member.internal.domain.MemberRole
import com.zoonza.wishfy.member.internal.domain.Nickname
import java.time.Instant

object MemberFixture {
    fun member(
        id: Long = 1L,
        loginId: String = "zoonza",
        encodedPassword: String = "encoded:password1234",
        nickname: String = "준자",
        role: MemberRole = MemberRole.MEMBER,
        createdAt: Instant = Instant.parse("2026-11-01T00:00:00Z"),
        updatedAt: Instant = createdAt
    ): Member = Member(
        id = id,
        loginId = LoginId(loginId),
        encodedPassword = encodedPassword,
        nickname = Nickname(nickname),
        role = role,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
