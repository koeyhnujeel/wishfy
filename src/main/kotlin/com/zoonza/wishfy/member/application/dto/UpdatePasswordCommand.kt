package com.zoonza.wishfy.member.application.dto

data class UpdatePasswordCommand(
    val memberId: Long,
    val currentPassword: String,
    val newPassword: String
)
