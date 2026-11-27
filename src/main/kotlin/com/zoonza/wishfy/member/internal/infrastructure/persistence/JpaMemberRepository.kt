package com.zoonza.wishfy.member.internal.infrastructure.persistence

import com.zoonza.wishfy.member.internal.domain.LoginId
import com.zoonza.wishfy.member.internal.domain.Member
import com.zoonza.wishfy.member.internal.domain.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class JpaMemberRepository(
    private val jpaRepository: MemberJpaRepository
) : MemberRepository {

    override fun existsByLoginId(loginId: LoginId): Boolean {
        return jpaRepository.existsByLoginId(loginId)
    }

    override fun save(member: Member): Member {
        return jpaRepository.save(member)
    }

    override fun findById(id: Long): Member? {
        return jpaRepository.findByIdOrNull(id)
    }

    override fun findByLoginId(loginId: LoginId): Member? {
        return jpaRepository.findByLoginId(loginId)
    }
}