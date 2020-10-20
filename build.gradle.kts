@file:Suppress("UNREACHABLE_CODE", "UNUSED_VARIABLE")

plugins {
    id("org.jetbrains.kotlin.js") version "1.4.10"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    // FIXME if specify `generateExternals = true`, then occur compilation errors.
    implementation(npm("@types/google-apps-script", "1.0.16", generateExternals = false))
    implementation(devNpm("@google/clasp", "2.3.0"))
}

kotlin {
    js {
        moduleName = "example"
        browser()
    }
}

tasks {
    val nodeModulesDir = "${rootProject.buildDir}/js/node_modules"

    // --- clasp

    // register "clasp{TaskName}" task
    fun registerClasp(taskName: String, vararg args: String) {
        val claspCommand = "$nodeModulesDir/.bin/clasp"
        register<Exec>("clasp${taskName.capitalize()}") {
            group = "clasp"
            commandLine(claspCommand, taskName, *args)
        }
    }

    val scriptUrl = "https://script.google.com/d/${TODO("Specify the your project url.")}/edit"
    val claspRoot = "dist"
    val claspJsonPath = ".clasp.json"

    registerClasp("login")
    registerClasp("clone", scriptUrl, "--rootDir", claspRoot)
    registerClasp("push")

    clean {
        delete(claspRoot)
        delete(claspJsonPath)
    }

    // --- dukat (generate external apis from *.d.ts)

    val typeDefinitionFiles = arrayOf(
        "google-apps-script.spreadsheet.d.ts",
        "google-apps-script.html.d.ts"
        // TODO Specify the necessary files.
    )
    val externalSrc = "src/main/kotlin/externals"

    register<Exec>("generateExternalsMinimal") {
        dependsOn("generateExternals") // FIXME if specify `kotlinNpmInstall`, then not work.
        doFirst {
            delete("externals") // generateExternals generated
        }

        group = "dukat"
        val dukatCommand = "$nodeModulesDir/.bin/dukat"
        val files = typeDefinitionFiles.joinToString(separator = " ") { "$nodeModulesDir/@types/google-apps-script/$it" }
        commandLine("bash", "-c", "$dukatCommand -d $externalSrc $files")
    }

    clean {
        delete(externalSrc)
    }

    // --- Kotlin/JS

    withType(org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile::class.java).named("compileKotlinJs") {
        kotlinOptions.moduleKind = "plain"
        doLast {
            copy {
                from("$nodeModulesDir/kotlin") {
                    include("*.js")
                }
                from(kotlinOptions.outputFile)
                into(claspRoot)
            }
            appendFilePushOrderAttr(claspJsonPath)
        }
    }
}