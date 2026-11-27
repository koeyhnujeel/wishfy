package com.zoonza.wishfy.village

import org.springframework.modulith.ApplicationModule

@ApplicationModule(
    allowedDependencies = [
        "common::error"
    ]
)
class ModuleMetadata {
}