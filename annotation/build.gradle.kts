val hiltVersion: String by project

plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("com.google.dagger:hilt-core:$hiltVersion")
}