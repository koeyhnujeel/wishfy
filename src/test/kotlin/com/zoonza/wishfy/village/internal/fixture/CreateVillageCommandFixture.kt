package com.zoonza.wishfy.village.internal.fixture

import com.zoonza.wishfy.village.internal.application.dto.CreateVillageCommand

object CreateVillageCommandFixture {
    fun createVillageCommand(
        ownerId: Long = 1L,
        villageName: String = "행복마을",
    ): CreateVillageCommand = CreateVillageCommand(
        ownerId = ownerId,
        villageName = villageName,
    )
}
