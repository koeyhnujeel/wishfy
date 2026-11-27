package com.zoonza.wishfy.member.internal.application.dto

data class UpdateNicknameCommand(
    val memberId: Long,
    val newNickname: String
)
