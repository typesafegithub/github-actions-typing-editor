import androidx.compose.ui.test.*
import kotlin.test.Test

class SmokeTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `loads action manifest`() = runComposeUiTest {
        setContent {
            App()
        }

        waitUntilAtLeastOneExists(hasTestTag("inputName"))

        onAllNodesWithTag("inputName").assertAny(hasText("repository"))
    }
}
