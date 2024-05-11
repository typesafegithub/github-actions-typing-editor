#!/usr/bin/env kotlin
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.15.0")

@file:Repository("https://github-workflows-kt-bindings.colman.com.br/binding/")
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("gradle:actions__setup-gradle:v3")
@file:DependsOn("actions:configure-pages:v5")
@file:DependsOn("actions:upload-pages-artifact:v3")
@file:DependsOn("actions:deploy-pages:v4")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.ConfigurePages
import io.github.typesafegithub.workflows.actions.actions.DeployPages
import io.github.typesafegithub.workflows.actions.actions.UploadPagesArtifact
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Deploy",
    on = listOf(Push(branches = listOf("main"))),
    sourceFile = __FILE__.toPath(),
) {
    job(id = "deploy", runsOn = RunnerType.UbuntuLatest) {
        uses(action = Checkout())
        uses(action = ActionsSetupGradle())
        run(command = "./gradlew wasmJsBrowserDistribution")
        uses(action = ConfigurePages())
        uses(action = UploadPagesArtifact(path = "build/dist/wasmJs/productionExecutable/"))
        uses(action = DeployPages())
    }
}.writeToFile()