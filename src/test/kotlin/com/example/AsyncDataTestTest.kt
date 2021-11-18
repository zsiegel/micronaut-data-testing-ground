package com.example

import io.micronaut.context.ApplicationContext
import io.micronaut.data.model.query.builder.sql.Dialect
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AsyncDataTestTest : PostgreSqlTestWithMigrations() {

    @Inject
    lateinit var context: ApplicationContext

    @BeforeEach
    fun getProperties() {
        val props = mapOf(
            "r2dbc.datasources.default.url" to "r2dbc:postgresql://${postgres.host}:${postgres.firstMappedPort}/${postgres.databaseName}",
            "r2dbc.datasources.default.username" to postgres.username,
            "r2dbc.datasources.default.password" to postgres.password,
            "r2dbc.datasources.default.dialect" to Dialect.POSTGRES
        )
        context = ApplicationContext.run(props)
    }

    @Test
    fun sanity() {
        assertTrue(true)
    }

    @Test
    fun verifySyncRepo() {
        syncRepo().save(testUser())
        assertEquals("Testing 123", syncRepo().findByEmail("test@micronaut.io")!!.name)
    }

    // To make suspend work in the two tests below you must enable the following
    //
    // implementation("io.micronaut.data:micronaut-data-r2dbc")
    // testImplementation("org.testcontainers:r2dbc")

    @Test
    fun verifySyncRepoWithSuspend() = runBlocking {
        syncSuspendRepo().save(testUser())
        assertEquals("Testing 123", syncSuspendRepo().findByEmail("test@micronaut.io")!!.name)
    }

    @Test
    fun verifySuspendUserRepo() = runBlocking {
        suspendUserRepo().save(testUser())
        assertEquals("Testing 123", suspendUserRepo().findByEmail("test@micronaut.io")!!.name)
    }

    private fun syncRepo(): CrudUserRepo {
        return context.getBean(CrudUserRepo::class.java)
    }

    private fun syncSuspendRepo(): CrudSuspendRepo {
        return context.getBean(CrudSuspendRepo::class.java)
    }

    private fun suspendUserRepo(): CoroutineUserRepo {
        return context.getBean(CoroutineUserRepo::class.java)
    }

    private fun testUser(): User {
        return User(
            email = "test@micronaut.io",
            name = "Testing 123"
        )
    }

}
