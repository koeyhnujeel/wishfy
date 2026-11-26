package com.zoonza.wishfy.village.domain

interface VillageRepository {
    fun countByOwnerId(ownerId: Long): Long

    fun save(village: Village): Village
}