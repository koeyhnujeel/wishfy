package com.zoonza.wishfy.village.internal.domain

import java.time.Instant

data class Resident(
    val memberId: Long,
    val joinedAt: Instant
) {
    fun isSameMember(memberId: Long): Boolean = this.memberId == memberId
}
