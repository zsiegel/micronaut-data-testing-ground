package com.example

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.repository.CrudRepository
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID
import javax.transaction.Transactional

@MappedEntity("users")
data class User(
    @field:Id
    @field:GeneratedValue
    val id: UUID? = null,
    val name: String,
    val email: String
)

@Transactional
@JdbcRepository
interface CrudUserRepo : CrudRepository<User, UUID> {

    fun findByEmail(email: String): User?

}

@Transactional
@JdbcRepository
interface CrudSuspendRepo : CrudRepository<User, UUID> {

    suspend fun findByEmail(email: String): User?

}

@Transactional
@JdbcRepository
interface CoroutineUserRepo : CoroutineCrudRepository<User, UUID> {

    suspend fun findByEmail(email: String): User?

}