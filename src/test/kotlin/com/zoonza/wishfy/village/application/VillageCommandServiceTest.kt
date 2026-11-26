package com.zoonza.wishfy.village.application

import com.zoonza.wishfy.common.error.BusinessException
import com.zoonza.wishfy.village.domain.VillageErrorCode
import com.zoonza.wishfy.village.domain.VillageName
import com.zoonza.wishfy.village.fake.FakeVillageRepository
import com.zoonza.wishfy.village.fixture.CreateVillageCommandFixture.createVillageCommand
import com.zoonza.wishfy.village.fixture.VillageFixture.village
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VillageCommandServiceTest {
    private lateinit var villageRepository: FakeVillageRepository
    private lateinit var villageCommandService: VillageCommandService

    @BeforeEach
    fun setUp() {
        villageRepository = FakeVillageRepository()
        villageCommandService = VillageCommandService(villageRepository)
    }

    @Test
    fun `마을을 생성하면 마을이 저장된다`() {
        villageCommandService.create(createVillageCommand(ownerId = 1L, villageName = "행복마을"))

        val village = villageRepository.findAllByOwnerId(1L).single()
        village.villageName shouldBe VillageName("행복마을")
    }

    @Test
    fun `마을을 생성하면 촌장이 주민으로 함께 저장된다`() {
        villageCommandService.create(createVillageCommand(ownerId = 1L))

        val village = villageRepository.findAllByOwnerId(1L).single()
        village.residents.map { it.memberId } shouldContainExactly listOf(1L)
    }

    @Test
    fun `마을을 2개 가진 회원은 마을을 하나 더 생성할 수 있다`() {
        villageRepository.save(village(id = 1L, ownerId = 1L))
        villageRepository.save(village(id = 2L, ownerId = 1L))

        villageCommandService.create(createVillageCommand(ownerId = 1L))

        villageRepository.countByOwnerId(1L) shouldBe 3L
    }

    @Test
    fun `마을을 3개 가진 회원은 마을을 더 생성할 수 없다`() {
        villageRepository.save(village(id = 1L, ownerId = 1L))
        villageRepository.save(village(id = 2L, ownerId = 1L))
        villageRepository.save(village(id = 3L, ownerId = 1L))

        val exception = shouldThrow<BusinessException> {
            villageCommandService.create(createVillageCommand(ownerId = 1L))
        }

        exception.errorCode shouldBe VillageErrorCode.CREATION_LIMIT_REACHED
        villageRepository.countByOwnerId(1L) shouldBe 3L
    }

    @Test
    fun `다른 회원이 가진 마을 수는 생성 제한에 포함되지 않는다`() {
        villageRepository.save(village(id = 1L, ownerId = 2L))
        villageRepository.save(village(id = 2L, ownerId = 2L))
        villageRepository.save(village(id = 3L, ownerId = 2L))

        villageCommandService.create(createVillageCommand(ownerId = 1L))

        villageRepository.countByOwnerId(1L) shouldBe 1L
    }

    @Test
    fun `마을 이름 형식이 올바르지 않으면 마을을 생성할 수 없다`() {
        shouldThrow<IllegalArgumentException> {
            villageCommandService.create(createVillageCommand(villageName = "행복 마을!"))
        }

        villageRepository.count() shouldBe 0
    }
}
