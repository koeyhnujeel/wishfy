package com.zoonza.wishfy.member

import org.springframework.modulith.ApplicationModule

@ApplicationModule(
    allowedDependencies = [
        "common::error"
    ]
)
class ModuleMetadata {
}