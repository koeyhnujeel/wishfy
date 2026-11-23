package com.zoonza.wishfy.member.fixture

import com.zoonza.wishfy.member.application.dto.RegisterMemberCommand

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
