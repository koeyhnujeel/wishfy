package com.zoonza.wishfy.member.internal.fixture

import com.zoonza.wishfy.member.internal.application.dto.UpdatePasswordCommand

object UpdatePasswordCommandFixture {
    fun updatePasswordCommand(
        memberId: Long = 1L,
        currentPassword: String = "oldPassword1234",
        newPassword: String = "newPassword1234"
    ): UpdatePasswordCommand = UpdatePasswordCommand(
        memberId = memberId,
        currentPassword = currentPassword,
        newPassword = newPassword
    )
}
