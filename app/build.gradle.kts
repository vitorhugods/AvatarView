plugins {
    id("com.android.application")
    id("jacoco-android")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    setCompileSdkVersion(AndroidModuleSpecs.compileSdkVersion)

    defaultConfig {
        setTargetSdkVersion(AndroidModuleSpecs.targetSdkVersion)
        setMinSdkVersion(19)
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        maybeCreate("release").apply {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    val kotlinVersion: String by project
    implementation(project(":avvylib"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.exifinterface:exifinterface:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")
    implementation("com.github.QuadFlask:colorpicker:0.0.13")
    implementation("com.xw.repo:bubbleseekbar:3.19")
    implementation("com.squareup.picasso:picasso:2.71828")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:core:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
