package com.zoonza.wishfy.member.internal.domain

interface MemberRepository {
    fun existsByLoginId(loginId: LoginId): Boolean

    fun save(member: Member): Member

    fun findById(id: Long): Member?

    fun findByLoginId(loginId: LoginId): Member?
}