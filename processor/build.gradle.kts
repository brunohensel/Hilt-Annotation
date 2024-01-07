val kspVersion: String by project
val kotlinPoetVersion: String by project

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup:kotlinpoet-ksp:$kotlinPoetVersion")
    implementation("com.squareup:kotlinpoet:$kotlinPoetVersion")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}