plugins {
    java
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "net.azisaba"
version = "1.0.5+1.21.1"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://jitpack.io/") }
    maven { url = uri("https://mvn.lumine.io/repository/maven-public/") }
    maven { url = uri("https://nexus.neetgames.com/repository/maven-public/") } // for mcMMO
}

dependencies {
    compileOnly("org.jetbrains:annotations:23.0.0")
    compileOnly("io.lumine:Mythic-Dist:5.7.2")
    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.1.196") {
        exclude("com.sk89q.worldguard", "worldguard-core")
        exclude("com.sk89q.worldguard", "worldguard-legacy")
    }
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
}

paperweight.reobfArtifactConfiguration.set(io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION)

tasks {
    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            include("**")
            val tokenReplacementMap = mapOf(
                "version" to project.version,
            )
            filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to tokenReplacementMap)
        }
        filteringCharset = "UTF-8"
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from(projectDir) { include("LICENSE") }
    }

    compileJava {
        options.encoding = "UTF-8"
    }
}
