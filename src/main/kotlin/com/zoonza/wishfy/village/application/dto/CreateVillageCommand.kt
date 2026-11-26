package com.zoonza.wishfy.village.application.dto

data class CreateVillageCommand(
    val ownerId: Long,
    val villageName: String,
)
