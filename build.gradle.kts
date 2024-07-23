import org.gradle.internal.impldep.org.bouncycastle.cms.RecipientId.password
import org.gradle.internal.impldep.org.eclipse.jgit.lib.ObjectChecker.type

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.16.0"
}

group = "com.c2ray.idea.plugin"
version = "1.0.4"

repositories {
//    maven {
//        url = uri("https://plugins.gradle.org/m2/")
//    }
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version = "LATEST-EAP-SNAPSHOT"
    updateSinceUntilBuild = false
    type.set("IU") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

dependencies {
    compileOnly(fileTree("resource/libs"))
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        sinceBuild.set("241")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
