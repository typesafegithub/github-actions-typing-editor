import com.charleskorn.kaml.Yaml
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.decodeFromString

suspend fun fetchManifest(actionCoords: String): Metadata? {
    val yaml = client.get(urlString = "https://raw.githubusercontent.com/${actionCoords.replace("@", "/")}/action.yml")
        .body<String>()
    return try {
        myYaml.decodeFromString<Metadata>(yaml)
    } catch (e: Exception) {
        println("Exception: $e")
        null
    }
}

private val myYaml = Yaml(configuration = Yaml.default.configuration.copy(strictMode = false))
