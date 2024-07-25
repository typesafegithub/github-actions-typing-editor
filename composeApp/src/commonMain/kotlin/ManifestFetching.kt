import com.charleskorn.kaml.Yaml
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.decodeFromString

suspend fun fetchManifest(actionCoords: String): Metadata? =
    fetchYaml(urlWithoutExtension = "https://raw.githubusercontent.com/${actionCoords.replace("@", "/")}/action")
        ?.let { decodeYamlFromString(it) }

suspend fun fetchTypingFromAction(actionCoords: String): Typing? =
    fetchYaml(urlWithoutExtension = "https://raw.githubusercontent.com/${actionCoords.replace("@", "/")}/action-types")
        ?.let { decodeYamlFromString(it) }

suspend fun fetchTypingFromCatalog(actionCoords: String): Typing? =
    fetchYaml(urlWithoutExtension = "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/main/typings/${actionCoords.replace("@", "/")}/action-types")
        ?.let { decodeYamlFromString(it) }

suspend fun fetchYaml(urlWithoutExtension: String): String? =
    listOf("yml", "yaml").firstNotNullOfOrNull { extension ->
        val response = client
            .get(urlString = "${urlWithoutExtension}.$extension")
        if (response.status == HttpStatusCode.OK) {
            response.body<String>()
        } else {
            null
        }
    }

private inline fun <reified T> decodeYamlFromString(yaml: String): T? =
    try {
        myYaml.decodeFromString<T>(yaml)
    } catch (e: Exception) {
        println("Exception: $e")
        null
    }

private val myYaml = Yaml(configuration = Yaml.default.configuration.copy(strictMode = false))
