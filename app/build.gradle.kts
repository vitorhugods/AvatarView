plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    setCompileSdkVersion(AndroidModuleSpecs.compileSdkVersion)

    defaultConfig {
        targetSdk = AndroidModuleSpecs.targetSdkVersion
        minSdk = AndroidModuleSpecs.minSdkVersion
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        maybeCreate("release").apply {
            isMinifyEnabled = true
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
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.exifinterface:exifinterface:1.3.3")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.github.QuadFlask:colorpicker:0.0.13")
    implementation("com.xw.repo:bubbleseekbar:3.19")
    implementation("com.squareup.picasso:picasso:2.71828")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:core-ktx:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
