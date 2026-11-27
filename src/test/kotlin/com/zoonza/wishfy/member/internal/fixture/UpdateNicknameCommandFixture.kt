package com.zoonza.wishfy.member.internal.fixture

import com.zoonza.wishfy.member.internal.application.dto.UpdateNicknameCommand

object UpdateNicknameCommandFixture {
    fun updateNicknameCommand(
        memberId: Long = 1L,
        newNickname: String = "새닉네임"
    ): UpdateNicknameCommand = UpdateNicknameCommand(
        memberId = memberId,
        newNickname = newNickname
    )
}
