import com.charleskorn.kaml.Yaml
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.decodeFromString

suspend fun fetchManifest(actionCoords: String): Metadata? {
    val yaml = listOf("yml", "yaml").firstNotNullOfOrNull { extension ->
        val response = client
            .get(urlString = "https://raw.githubusercontent.com/${actionCoords.replace("@", "/")}/action.$extension")
        if (response.status == HttpStatusCode.OK) {
            response.body<String>()
        } else {
            null
        }
    } ?: return null

    return try {
        myYaml.decodeFromString<Metadata>(yaml)
    } catch (e: Exception) {
        println("Exception: $e")
        null
    }
}

private val myYaml = Yaml(configuration = Yaml.default.configuration.copy(strictMode = false))
