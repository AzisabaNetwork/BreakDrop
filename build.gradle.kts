plugins {
    java
}

group = "net.azisaba"
version = "1.0.3-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://jitpack.io/") }
    maven { url = uri("https://mvn.lumine.io/repository/maven-public/") }
}

dependencies {
    compileOnly("org.jetbrains:annotations:23.0.0")
    compileOnly("com.destroystokyo.paper:paper-api:1.15.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.15.2-R0.1-SNAPSHOT")
    compileOnly("io.lumine:Mythic-Dist:4.13.0")
    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.1.200")
}

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
