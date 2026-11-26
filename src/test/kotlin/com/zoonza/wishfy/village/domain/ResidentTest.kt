package com.zoonza.wishfy.village.domain

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.Instant

class ResidentTest {

    private val joinedAt = Instant.parse("2026-11-01T00:00:00Z")

    @Test
    fun `회원 ID가 같으면 같은 회원으로 판단한다`() {
        val resident = Resident(memberId = 1L, joinedAt = joinedAt)

        resident.isSameMember(1L) shouldBe true
    }

    @Test
    fun `회원 ID가 다르면 다른 회원으로 판단한다`() {
        val resident = Resident(memberId = 1L, joinedAt = joinedAt)

        resident.isSameMember(2L) shouldBe false
    }

    @Test
    fun `참여 시각이 달라도 회원 ID가 같으면 같은 회원으로 판단한다`() {
        val resident = Resident(memberId = 1L, joinedAt = joinedAt)
        val sameMemberJoinedLater = Resident(memberId = 1L, joinedAt = joinedAt.plusSeconds(60))

        resident.isSameMember(sameMemberJoinedLater.memberId) shouldBe true
    }
}
