package com.zoonza.wishfy.village.internal.domain

import com.zoonza.wishfy.common.error.BusinessException
import java.time.Instant

class Village(
    val id: Long = 0L,
    var ownerId: Long,
    var villageName: VillageName,
    val residents: MutableList<Resident>,
    val createdAt: Instant,
    var updatedAt: Instant,
) {
    fun join(memberId: Long, joinedAt: Instant = Instant.now()) {
        if (this.residents.any { it.isSameMember(memberId) }) {
            throw BusinessException(VillageErrorCode.ALREADY_RESIDENT)
        }

        if (residents.size >= MAX_RESIDENTS) {
            throw BusinessException(VillageErrorCode.RESIDENT_LIMIT_REACHED)
        }

        this.residents.add(Resident(memberId, joinedAt))
        this.updatedAt = joinedAt
    }

    fun leave(memberId: Long, leaveAt: Instant = Instant.now()) {
        if (ownerId == memberId) {
            throw BusinessException(VillageErrorCode.OWNER_CANNOT_LEAVE)
        }

        this.residents.removeIf { it.isSameMember(memberId) }
        this.updatedAt = leaveAt
    }

    companion object {
        private const val MAX_VILLAGES_PER_OWNER = 3
        private const val MAX_RESIDENTS = 5

        fun create(
            ownerId: Long,
            villageName: VillageName,
            ownedVillageCount: Long,
            createdAt: Instant = Instant.now(),
        ): Village {
            if (ownedVillageCount >= MAX_VILLAGES_PER_OWNER) {
                throw BusinessException(VillageErrorCode.CREATION_LIMIT_REACHED)
            }

            return Village(
                ownerId = ownerId,
                villageName = villageName,
                residents = mutableListOf(Resident(ownerId, createdAt)),
                createdAt = createdAt,
                updatedAt = createdAt
            )
        }
    }
}