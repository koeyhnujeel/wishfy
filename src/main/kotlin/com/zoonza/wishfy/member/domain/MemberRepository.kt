package com.zoonza.wishfy.member.domain

interface MemberRepository {
    fun existsByLoginId(loginId: LoginId): Boolean

    fun save(member: Member): Member

    fun findById(id: Long): Member?
}