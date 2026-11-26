package com.zoonza.wishfy.village.fixture

import com.zoonza.wishfy.village.application.dto.CreateVillageCommand

object CreateVillageCommandFixture {
    fun createVillageCommand(
        ownerId: Long = 1L,
        villageName: String = "행복마을",
    ): CreateVillageCommand = CreateVillageCommand(
        ownerId = ownerId,
        villageName = villageName,
    )
}
