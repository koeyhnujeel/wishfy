package com.zoonza.wishfy.member.internal.application.dto

data class UpdatePasswordCommand(
    val memberId: Long,
    val currentPassword: String,
    val newPassword: String
)
