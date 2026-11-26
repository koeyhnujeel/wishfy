package com.zoonza.wishfy.village.domain

import com.zoonza.wishfy.common.error.ErrorCode

enum class VillageErrorCode(
    override val code: String,
    override val message: String,
    override val status: Int
) : ErrorCode {
    CREATION_LIMIT_REACHED("VILLAGE-001", "마을은 최대 3개까지 만들 수 있습니다.", 409),
    RESIDENT_LIMIT_REACHED("VILLAGE-002", "마을 주민은 최대 5명까지 참여할 수 있습니다.", 409),
    ALREADY_RESIDENT("VILLAGE-003", "이미 참여 중인 마을입니다.", 409),
}