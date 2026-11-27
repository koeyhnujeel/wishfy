package com.zoonza.wishfy.village.internal.fixture

import com.zoonza.wishfy.village.internal.domain.Resident
import com.zoonza.wishfy.village.internal.domain.Village
import com.zoonza.wishfy.village.internal.domain.VillageName
import java.time.Instant

object VillageFixture {
    /**
     * residentIds를 지정하지 않으면 촌장만 주민으로 들어 있는 마을을 만든다.
     */
    fun village(
        id: Long = 1L,
        ownerId: Long = 1L,
        name: String = "행복마을",
        residentIds: List<Long> = listOf(ownerId),
        createdAt: Instant = Instant.parse("2026-11-01T00:00:00Z"),
        updatedAt: Instant = createdAt,
    ): Village = Village(
        id = id,
        ownerId = ownerId,
        villageName = VillageName(name),
        residents = residentIds.map { Resident(it, createdAt) }.toMutableList(),
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
