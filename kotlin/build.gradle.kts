plugins {
    id("java")
    kotlin("jvm") version "1.9.0"
    application
}

group = "au.com.krynj.aoc.util"

repositories {
    mavenCentral()
    mavenLocal()
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
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("au.com.krynj.aoc.twentyfour.Main")
}

tasks {
    named<JavaExec>("run") {
        group = "application"
        description = "Run the main Kotlin application"
    }
}
