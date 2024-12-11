import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("java")
    kotlin("jvm")
//    application
    id("org.jetbrains.compose")
}

group = "au.com.krynj.aoc.util"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("au.com.krynj.aoc:aoc-kotlin-framework:0.1.0-SNAPSHOT") {
        artifact {
            classifier = "tests"
        }
    }
    implementation("au.com.krynj.aoc:aoc-kotlin-framework:0.1.0-SNAPSHOT")
    implementation("au.com.krynj.aoc:aoc-kotlin-utils:0.1.0-SNAPSHOT")
    implementation(compose.desktop.currentOs)
}

tasks.test {
    useJUnitPlatform()
}

//application {
//    mainClass.set("au.com.krynj.aoc.twentyfour.Main")
//}
//
//tasks.register<JavaExec>("runCLI") {
//    group = "application"
//    description = "Run the main Kotlin application"
//}

compose.desktop {
    application {
        mainClass = "MainCMPKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "aoc-kotlin-cmp"
            packageVersion = "1.0.0"
        }
    }
}
