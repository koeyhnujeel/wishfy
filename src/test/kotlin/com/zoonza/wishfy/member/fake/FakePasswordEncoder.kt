package com.zoonza.wishfy.member.fake

import com.zoonza.wishfy.member.domain.PasswordEncoder

class FakePasswordEncoder : PasswordEncoder {
    override fun encode(rawPassword: String): String = PREFIX + rawPassword

    override fun matches(rawPassword: String, encodedPassword: String): Boolean =
        encode(rawPassword) == encodedPassword

    companion object {
        const val PREFIX = "encoded:"
    }
}
