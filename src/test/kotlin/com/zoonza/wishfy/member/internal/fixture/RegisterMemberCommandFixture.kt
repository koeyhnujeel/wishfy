package com.zoonza.wishfy.member.internal.fixture

import com.zoonza.wishfy.member.internal.application.dto.RegisterMemberCommand

object RegisterMemberCommandFixture {
    fun registerMemberCommand(
        loginId: String = "zoonza",
        rawPassword: String = "password1234",
        nickname: String = "준자"
    ): RegisterMemberCommand = RegisterMemberCommand(
        loginId = loginId,
        rawPassword = rawPassword,
        nickname = nickname
    )
}
