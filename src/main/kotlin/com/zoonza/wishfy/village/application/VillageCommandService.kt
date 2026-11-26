package com.zoonza.wishfy.village.application

import com.zoonza.wishfy.village.application.dto.CreateVillageCommand
import com.zoonza.wishfy.village.domain.Village
import com.zoonza.wishfy.village.domain.VillageName
import com.zoonza.wishfy.village.domain.VillageRepository
import org.springframework.stereotype.Service

@Service
class VillageCommandService(
    private val villageRepository: VillageRepository,
) {
    fun create(command: CreateVillageCommand) {
        val villageName = VillageName(command.villageName)
        val count = villageRepository.countByOwnerId(command.ownerId)
        val village = Village.create(command.ownerId, villageName, count)

        villageRepository.save(village)
    }
}