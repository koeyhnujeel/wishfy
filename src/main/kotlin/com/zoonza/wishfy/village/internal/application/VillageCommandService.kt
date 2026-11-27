package com.zoonza.wishfy.village.internal.application

import com.zoonza.wishfy.common.error.BusinessException
import com.zoonza.wishfy.village.internal.application.dto.CreateVillageCommand
import com.zoonza.wishfy.village.internal.application.dto.JoinVillageCommand
import com.zoonza.wishfy.village.internal.application.dto.LeaveVillageCommand
import com.zoonza.wishfy.village.internal.domain.Village
import com.zoonza.wishfy.village.internal.domain.VillageErrorCode
import com.zoonza.wishfy.village.internal.domain.VillageName
import com.zoonza.wishfy.village.internal.domain.VillageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    @Transactional
    fun join(command: JoinVillageCommand) {
        val village = getVillageBy(command.villageId)

        village.join(command.memberId)
    }

    @Transactional
    fun leave(command: LeaveVillageCommand) {
        val village = getVillageBy(command.villageId)

        village.leave(command.memberId)
    }

    private fun getVillageBy(villageId: Long): Village =
        villageRepository.findById(villageId)
            ?: throw BusinessException(VillageErrorCode.NOT_FOUND)
}