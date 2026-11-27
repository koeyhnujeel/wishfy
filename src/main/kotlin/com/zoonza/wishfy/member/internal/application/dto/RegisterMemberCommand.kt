package com.zoonza.wishfy.member.internal.application.dto

data class RegisterMemberCommand(
    val loginId: String,
    val rawPassword: String,
    val nickname: String
)
