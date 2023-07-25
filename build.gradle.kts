plugins {
    java
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.5"
    id("xyz.jpenilla.run-paper") version "2.1.0" // Adds runServer and runMojangMappedServer tasks for testing

}

group = "de.threeseconds"
version = "0.0.1-SNAPSHOT"
description = "FreeBuild"

java {
    // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
    mavenLocal()
    maven ( "https://jitpack.io" )
    maven ( "https://repo.dmulloy2.net/repository/public/" )
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.26")
    paperweight.paperDevBundle("1.20-R0.1-SNAPSHOT")
    compileOnly("de.privateseconds:CoreSystem:1.0.0-SNAPSHOT")
    compileOnly("de.privateseconds:PermissionCenterModulePaper:1.0.0-SNAPSHOT")
    compileOnly("com.github.BlueMap-Minecraft:BlueMapAPI:v2.5.1")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    implementation("redis.clients:jedis:4.4.3")
    // paperweight.foliaDevBundle("1.20-R0.1-SNAPSHOT")
    // paperweight.devBundle("com.example.paperfork", "1.20-R0.1-SNAPSHOT")
}

tasks {
    // Configure reobfJar to run when invoking the build task
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
        val props = mapOf(
                "name" to project.name,
                "version" to project.version,
                "description" to project.description,
                "apiVersion" to "1.20"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}