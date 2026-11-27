package com.zoonza.wishfy.member.internal.infrastructure.security

import com.zoonza.wishfy.member.internal.domain.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder as SpringBCryptPasswordEncoder

@Component
class BCryptPasswordEncoder : PasswordEncoder {
    private val passwordEncoder: SpringBCryptPasswordEncoder = SpringBCryptPasswordEncoder()

    override fun encode(rawPassword: String): String {
        return checkNotNull(passwordEncoder.encode(rawPassword)) { "비밀번호 인코딩 결과가 null 입니다." }
    }

    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
}