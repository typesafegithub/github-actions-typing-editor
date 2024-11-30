import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient

val client = HttpClient()

@Composable
fun App() {
    MaterialTheme {
        var actionCoords by remember { mutableStateOf("actions/checkout@v4") }
        var manifest: Metadata? by remember { mutableStateOf(null) }
        var typingFromAction: Typing? by remember { mutableStateOf(null) }
        var typingFromCatalog: Typing? by remember { mutableStateOf(null) }

        LaunchedEffect(actionCoords) {
            manifest = fetchManifest(actionCoords)
            typingFromAction = fetchTypingFromAction(actionCoords)
            typingFromCatalog = fetchTypingFromCatalog(actionCoords)
        }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Action coords:")
            TextField(
                value = actionCoords,
                onValueChange = { actionCoords = it },
            )
            Spacer(Modifier.height(10.dp))

            Row {
                manifest(manifest)
                typing(typingFromAction, source = "action")
                typing(typingFromCatalog, source = "catalog")
            }
        }
    }
}

@Composable
private fun manifest(manifest: Metadata?) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Text("Manifest:")
        Text("Inputs", fontWeight = FontWeight.Bold)
        if (manifest?.inputs?.isEmpty() == true) {
            Text("<none>")
        }
        manifest?.inputs?.forEach {
            Text(it.key)
        }

        Spacer(Modifier.height(10.dp))

        Text("Outputs", fontWeight = FontWeight.Bold)
        if (manifest?.outputs?.isEmpty() == true) {
            Text("<none>")
        }
        manifest?.outputs?.forEach {
            Text(it.key)
        }
    }
}

@Composable
private fun typing(typing: Typing?, source: String) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Text("Typing from $source:")
        Text("Inputs", fontWeight = FontWeight.Bold)
        if (typing?.inputs?.isEmpty() == true) {
            Text("<none>")
        }
        typing?.inputs?.forEach {
            Text("${it.key}: ${it.value.type}")
        }

        Spacer(Modifier.height(10.dp))

        Text("Outputs", fontWeight = FontWeight.Bold)
        if (typing?.outputs?.isEmpty() == true) {
            Text("<none>")
        }
        typing?.outputs?.forEach {
            Text("${it.key}: ${it.value.type}")
        }
    }
}
