package com.example

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container


class PostgresContainer(image: String = "postgres:13.4") : PostgreSQLContainer<PostgresContainer>(image)

fun postgresContainer(): PostgresContainer {
    return PostgresContainer()
        .withExposedPorts(5432)
        .withDatabaseName("uuid-test")
}

open class PostgreSqlTestWithMigrations {

    @Container
    val postgres = postgresContainer()

    private val config = ClassicConfiguration()

    @BeforeEach
    fun migrate() {
        postgres.start()
        config.setDataSource(postgres.jdbcUrl, postgres.username, postgres.password)
        Flyway(config).migrate()
    }

    @AfterEach
    fun clean() {
        postgres.start()
        Flyway(config).clean()
    }
}