package com.zoonza.wishfy.village.internal.domain

interface VillageRepository {
    fun countByOwnerId(ownerId: Long): Long

    fun save(village: Village): Village

    fun findById(id: Long): Village?
}