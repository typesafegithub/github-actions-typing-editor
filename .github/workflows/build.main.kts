#!/usr/bin/env kotlin
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.15.0")

@file:Repository("https://github-workflows-kt-bindings.colman.com.br/binding/")
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("gradle:actions__setup-gradle:v3")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Build",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "build",
        runsOn = RunnerType.UbuntuLatest,
    ) {
        uses(action = Checkout())
        uses(action = ActionsSetupGradle())
        run(command = "./gradlew build")
    }
}.writeToFile()
