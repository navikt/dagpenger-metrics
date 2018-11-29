import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    kotlin("jvm") version "1.3.10"
    id("com.diffplug.gradle.spotless") version "3.13.0"
    id("maven-publish")
    id("signing")
    id("io.codearte.nexus-staging") version "0.12.0"
}

apply {
    plugin("com.diffplug.gradle.spotless")
}

repositories {
    mavenCentral()
    maven("http://packages.confluent.io/maven/")
    maven("https://dl.bintray.com/kotlin/ktor")
    maven("https://dl.bintray.com/kotlin/kotlinx")
    maven("https://dl.bintray.com/kittinunf/maven")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

//java {
//    sourceCompatibility = JavaVersion.VERSION_1_8
//    targetCompatibility = JavaVersion.VERSION_1_8
//}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

group = "no.nav.dagpenger"
version = "0.1.0-SNAPSHOT"

val kotlinLoggingVersion = "1.4.9"
val prometheusVersion = "0.5.0"

dependencies {
    implementation(kotlin("stdlib"))

    api("io.prometheus:simpleclient_common:$prometheusVersion")
    api("io.prometheus:simpleclient_hotspot:$prometheusVersion")

    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("junit:junit:4.12")
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create("mavenJava", MavenPublication::class.java) {
            from(components["java"])
            artifact(sourcesJar.get())

            pom {
                name.set("dagpenger-metrics")
                description.set("")
                url.set("https://github.com/navikt/dagpenger-metrics")
                withXml {
                    asNode().appendNode("packaging", "jar")
                }
                licenses {
                    license {
                        name.set("MIT License")
                        name.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        organization.set("NAV (Arbeids- og velferdsdirektoratet) - The Norwegian Labour and Welfare Administration")
                        organizationUrl.set("https://www.nav.no")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/navikt/dagpenger-metrics.git")
                    developerConnection.set("scm:git:git://github.com/navikt/dagpenger-metrics.git")
                    url.set("https://github.com/navikt/dagpenger-metrics")
                }
            }
        }
    }

    repositories {
        maven {
            credentials {
                username = System.getenv("OSSRH_JIRA_USERNAME")
                password = System.getenv("OSSRH_JIRA_PASSWORD")
            }
            val version = project.version as String
            url = if (version.endsWith("-SNAPSHOT")) {
                uri("https://oss.sonatype.org/content/repositories/snapshots")
            } else {
                uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
        }
    }
}

ext["signing.gnupg.keyName"] = System.getenv("GPG_KEY_NAME")
ext["signing.gnupg.passphrase"] = System.getenv("GPG_PASSPHRASE")
ext["signing.gnupg.useLegacyGpg"] = true

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}

nexusStaging {
    username = System.getenv("OSSRH_JIRA_USERNAME")
    password = System.getenv("OSSRH_JIRA_PASSWORD")
    packageGroup = "no.nav"
}

spotless {
    kotlin {
        ktlint()
    }
    kotlinGradle {
        target("*.gradle.kts", "additionalScripts/*.gradle.kts")
        ktlint()
    }
}
