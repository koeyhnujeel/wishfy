package com.zoonza.wishfy.village.domain

import com.zoonza.wishfy.common.error.BusinessException
import com.zoonza.wishfy.village.fixture.VillageFixture.village
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.Instant

class VillageTest {

    private val createdAt = Instant.parse("2026-11-26T09:00:00Z")
    private val joinedAt = Instant.parse("2026-11-26T10:00:00Z")

    // ---------- 마을 생성 ----------

    @Test
    fun `마을을 생성하면 입력한 촌장과 이름을 가진 마을이 만들어진다`() {
        val village = Village.create(ownerId = 1L, villageName = VillageName("행복마을"), ownedVillageCount = 0)

        village.ownerId shouldBe 1L
        village.villageName shouldBe VillageName("행복마을")
    }

    @Test
    fun `마을을 생성하면 촌장이 첫 번째 주민으로 참여한다`() {
        val village = Village.create(ownerId = 1L, villageName = VillageName("행복마을"), ownedVillageCount = 0)

        village.residents.map { it.memberId } shouldContainExactly listOf(1L)
    }

    @Test
    fun `마을을 생성하면 생성 시각 수정 시각 촌장의 참여 시각이 모두 같다`() {
        val village = Village.create(
            ownerId = 1L,
            villageName = VillageName("행복마을"),
            ownedVillageCount = 0,
            createdAt = createdAt,
        )

        village.createdAt shouldBe createdAt
        village.updatedAt shouldBe createdAt
        village.residents.single().joinedAt shouldBe createdAt
    }

    @Test
    fun `마을을 2개 가지고 있으면 마을을 하나 더 생성할 수 있다`() {
        val village = Village.create(ownerId = 1L, villageName = VillageName("행복마을"), ownedVillageCount = 2)

        village.ownerId shouldBe 1L
    }

    @Test
    fun `마을을 3개 가지고 있으면 마을을 더 생성할 수 없다`() {
        val exception = shouldThrow<BusinessException> {
            Village.create(ownerId = 1L, villageName = VillageName("행복마을"), ownedVillageCount = 3)
        }

        exception.errorCode shouldBe VillageErrorCode.CREATION_LIMIT_REACHED
    }

    // ---------- 주민 추가 ----------

    @Test
    fun `주민을 추가하면 마을의 주민이 된다`() {
        val village = village(ownerId = 1L)

        village.addResident(memberId = 2L, joinedAt = joinedAt)

        village.residents.map { it.memberId } shouldContainExactly listOf(1L, 2L)
    }

    @Test
    fun `주민을 추가하면 참여 시각이 기록되고 마을의 수정 시각도 같은 시각으로 갱신된다`() {
        val village = village(ownerId = 1L)

        village.addResident(memberId = 2L, joinedAt = joinedAt)

        village.residents.single { it.isSameMember(2L) }.joinedAt shouldBe joinedAt
        village.updatedAt shouldBe joinedAt
    }

    @Test
    fun `이미 주민인 회원은 다시 추가할 수 없다`() {
        val village = village(ownerId = 1L, residentIds = listOf(1L, 2L))

        val exception = shouldThrow<BusinessException> { village.addResident(memberId = 2L) }

        exception.errorCode shouldBe VillageErrorCode.ALREADY_RESIDENT
    }

    @Test
    fun `촌장도 이미 주민이므로 다시 추가할 수 없다`() {
        val village = village(ownerId = 1L)

        val exception = shouldThrow<BusinessException> { village.addResident(memberId = 1L) }

        exception.errorCode shouldBe VillageErrorCode.ALREADY_RESIDENT
    }

    @Test
    fun `촌장을 포함해 주민이 4명이면 한 명 더 추가할 수 있다`() {
        val village = village(ownerId = 1L, residentIds = listOf(1L, 2L, 3L, 4L))

        village.addResident(memberId = 5L)

        village.residents shouldHaveSize 5
    }

    @Test
    fun `촌장을 포함해 주민이 5명이면 더 추가할 수 없다`() {
        val village = village(ownerId = 1L, residentIds = listOf(1L, 2L, 3L, 4L, 5L))

        val exception = shouldThrow<BusinessException> { village.addResident(memberId = 6L) }

        exception.errorCode shouldBe VillageErrorCode.RESIDENT_LIMIT_REACHED
    }

    @Test
    fun `정원이 찬 마을에 이미 주민인 회원이 참여하면 중복 참여로 처리된다`() {
        val village = village(ownerId = 1L, residentIds = listOf(1L, 2L, 3L, 4L, 5L))

        val exception = shouldThrow<BusinessException> { village.addResident(memberId = 3L) }

        exception.errorCode shouldBe VillageErrorCode.ALREADY_RESIDENT
    }

    @Test
    fun `주민 추가에 실패하면 주민 목록과 수정 시각이 그대로 유지된다`() {
        val village = village(ownerId = 1L, residentIds = listOf(1L, 2L, 3L, 4L, 5L))
        val beforeUpdatedAt = village.updatedAt

        runCatching { village.addResident(memberId = 6L, joinedAt = joinedAt) }

        village.residents shouldHaveSize 5
        village.updatedAt shouldBe beforeUpdatedAt
    }
}
