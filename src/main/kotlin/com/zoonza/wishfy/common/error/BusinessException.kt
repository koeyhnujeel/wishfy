package com.zoonza.wishfy.common.error

class BusinessException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message) {
}