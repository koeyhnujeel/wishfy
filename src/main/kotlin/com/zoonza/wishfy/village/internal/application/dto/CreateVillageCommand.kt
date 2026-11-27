package com.zoonza.wishfy.village.internal.application.dto

data class CreateVillageCommand(
    val ownerId: Long,
    val villageName: String,
)
