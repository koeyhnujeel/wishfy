package com.zoonza.wishfy.member.internal.fake

import com.zoonza.wishfy.member.internal.domain.LoginId
import com.zoonza.wishfy.member.internal.domain.Member
import com.zoonza.wishfy.member.internal.domain.MemberRepository

class FakeMemberRepository : MemberRepository {
    private val members = mutableMapOf<Long, Member>()
    private var sequence = 0L

    override fun existsByLoginId(loginId: LoginId): Boolean =
        members.values.any { it.loginId == loginId }

    override fun save(member: Member): Member {
        val saved = if (member.id == 0L) member.withId(++sequence) else member
        sequence = maxOf(sequence, saved.id)
        members[saved.id] = saved
        return saved
    }

    override fun findById(id: Long): Member? = members[id]

    override fun findByLoginId(loginId: LoginId): Member? =
        members.values.find { it.loginId == loginId }

    fun count(): Int = members.size

    private fun Member.withId(id: Long): Member = Member(
        id = id,
        loginId = loginId,
        encodedPassword = encodedPassword,
        nickname = nickname,
        role = role,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
