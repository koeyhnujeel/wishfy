package com.zoonza.wishfy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class WishfyApplication

fun main(args: Array<String>) {
    runApplication<WishfyApplication>(*args)
}
