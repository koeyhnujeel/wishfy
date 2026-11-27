package com.zoonza.wishfy

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules

class ModularityTests {

    private val modules = ApplicationModules.of(WishfyApplication::class.java)

    @Test
    fun `모듈 간 의존성 규칙을 지킨다`() {
        modules.verify()
    }
}
