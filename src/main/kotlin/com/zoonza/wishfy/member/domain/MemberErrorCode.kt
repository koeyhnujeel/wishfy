package com.zoonza.wishfy.member.domain

import com.zoonza.wishfy.common.error.ErrorCode

enum class MemberErrorCode(
    override val code: String,
    override val message: String,
    override val status: Int
) : ErrorCode {
    LOGIN_ID_ALREADY_EXISTS("MEMBER-001", "이미 사용 중인 아이디입니다.", 409),
    NOT_FOUND("MEMBER-002", "사용자 정보를 찾을 수 없습니다.", 404),
}