import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.charleskorn.kaml.Yaml
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString

val client = HttpClient()

@Composable
fun App() {
    MaterialTheme {
        var actionCoords by remember { mutableStateOf("actions/checkout") }
        var manifestYaml by remember { mutableStateOf("<manifest>") }

        LaunchedEffect(actionCoords) {
            println("Changed to $actionCoords")
            val actionManifestYaml = client.get(urlString = "https://raw.githubusercontent.com/$actionCoords/main/action.yml")
                .body<String>()
            manifestYaml = actionManifestYaml
        }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Action coords:")
            TextField(
                value = actionCoords,
                onValueChange = { actionCoords = it },
            )
            Spacer(Modifier.height(10.dp))
            Text("Manifest:")
            Text(manifestYaml)

            val manifest = Yaml.default.decodeFromString<Metadata>(manifestYaml)
            Text("Parsed:")
            Text(manifest.toString())
        }
    }
}

@Serializable
data class Metadata(
    val name: String,
    val description: String,
    val inputs: Map<String, Input> = emptyMap(),
    val outputs: Map<String, Output> = emptyMap(),
)

@Serializable
data class Input(
    val description: String = "",
    val default: String? = null,
    val required: Boolean? = null,
    val deprecationMessage: String? = null,
)

@Serializable
data class Output(
    val description: String = "",
)
