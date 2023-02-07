package com.programmersbox.fullmultiplatformcompose.utils

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class NetworkVersions {

    companion object {
        const val githubRepoUrl = "https://github.com/jakepurple13/Full-Multiplatform-Compose-Plugin"
    }

    private val json = Json {
        isLenient = true
        prettyPrint = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val client = HttpClient {
        install(ContentNegotiation) { json(json) }
    }

    suspend fun getVersions(remoteVersions: Boolean) = runCatching {
        check(remoteVersions)
        client
            .get("https://raw.githubusercontent.com/jakepurple13/Full-Multiplatform-Compose-Plugin/main/versions.json")
            .bodyAsText()
            .let { json.decodeFromString<ProjectVersions>(it) }
    }.getOrElse {
        it.printStackTrace()
        ProjectVersions(
            kotlinVersion = "1.8.0",
            agpVersion = "7.3.0",
            composeVersion = "1.3.0"
        )
    }
}

@Serializable
data class ProjectVersions(
    val kotlinVersion: String,
    val agpVersion: String,
    val composeVersion: String
)