package com.zoonza.wishfy

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<WishfyApplication>().with(TestcontainersConfiguration::class).run(*args)
}
