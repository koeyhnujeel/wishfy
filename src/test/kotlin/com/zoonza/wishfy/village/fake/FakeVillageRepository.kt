package com.zoonza.wishfy.village.fake

import com.zoonza.wishfy.village.domain.Village
import com.zoonza.wishfy.village.domain.VillageRepository

class FakeVillageRepository : VillageRepository {
    private val villages = mutableMapOf<Long, Village>()
    private var sequence = 0L

    override fun countByOwnerId(ownerId: Long): Long =
        villages.values.count { it.ownerId == ownerId }.toLong()

    override fun save(village: Village): Village {
        val saved = if (village.id == 0L) village.withId(++sequence) else village
        sequence = maxOf(sequence, saved.id)
        villages[saved.id] = saved
        return saved
    }

    override fun findById(id: Long): Village? = villages[id]

    fun findAllByOwnerId(ownerId: Long): List<Village> =
        villages.values.filter { it.ownerId == ownerId }

    fun count(): Int = villages.size

    private fun Village.withId(id: Long): Village = Village(
        id = id,
        ownerId = ownerId,
        villageName = villageName,
        residents = residents,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
