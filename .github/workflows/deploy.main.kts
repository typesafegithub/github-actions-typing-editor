#!/usr/bin/env kotlin
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.2.0")

@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("gradle:actions__setup-gradle:v4")
@file:DependsOn("actions:configure-pages:v5")
@file:DependsOn("actions:upload-pages-artifact:v3")
@file:DependsOn("actions:deploy-pages:v4")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.ConfigurePages
import io.github.typesafegithub.workflows.actions.actions.DeployPages
import io.github.typesafegithub.workflows.actions.actions.UploadPagesArtifact
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.domain.Environment
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow

workflow(
    name = "Deploy",
    on = listOf(Push(branches = listOf("main"))),
    sourceFile = __FILE__,
    permissions = mapOf(
        Permission.Contents to Mode.Read,
        Permission.Pages to Mode.Write,
        Permission.IdToken to Mode.Write,
    ),
) {
    job(
        id = "deploy",
        runsOn = RunnerType.UbuntuLatest,
        environment = Environment(
            name = "github-pages",
            url = null,
        )
    ) {
        uses(action = Checkout())
        uses(action = ActionsSetupGradle())
        run(command = "./gradlew wasmJsBrowserDistribution")
        uses(action = ConfigurePages())
        uses(action = UploadPagesArtifact(
            path = "composeApp/build/dist/wasmJs/productionExecutable/"
        ))
        uses(action = DeployPages())
    }
}
