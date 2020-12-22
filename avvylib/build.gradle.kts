plugins {
    id("com.android.library")
    id("kotlin-android")
    id("jacoco-android")
    id("com.github.panpf.bintray-publish")
}
val kotlinVersion: String by project
val version = "1.1.1"
android {
    setCompileSdkVersion(AndroidModuleSpecs.compileSdkVersion)

    defaultConfig {
        setTargetSdkVersion(AndroidModuleSpecs.targetSdkVersion)
        setMinSdkVersion(AndroidModuleSpecs.minSdkVersion)
        versionCode = 1
        versionName = version
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        maybeCreate("release").apply {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

configure<com.github.panpf.bintray.publish.PublishExtension> {
    userOrg = "vitorhugods"
    repoName = "AvatarView"
    groupId = "xyz.schwaab"
    artifactId = "avvylib"
    publishVersion = version
    desc = "A circular ImageView with border, progress animation and customizable highlights for Android"
    website = "https://github.com/vitorhugods/AvatarView"
    isSign = true
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("androidx.annotation:annotation:1.1.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:core:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
