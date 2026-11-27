package com.zoonza.wishfy.member.internal.infrastructure.persistence

import com.zoonza.wishfy.member.internal.domain.LoginId
import com.zoonza.wishfy.member.internal.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<Member, Long> {
    fun existsByLoginId(loginId: LoginId): Boolean

    fun findByLoginId(loginId: LoginId): Member?
}