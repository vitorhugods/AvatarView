import org.jetbrains.kotlin.utils.sure

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("convention.publication")
}
val kotlinVersion: String by project
group = "xyz.schwaab"
version = "1.2.0"

android {
    setCompileSdkVersion(AndroidModuleSpecs.compileSdkVersion)

    defaultConfig {
        targetSdk = AndroidModuleSpecs.targetSdkVersion
        minSdk = AndroidModuleSpecs.minSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        maybeCreate("release").apply {
            isMinifyEnabled = false
        }
    }

    publishing {
        singleVariant("release") {
            withJavadocJar()
            withSourcesJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create("release", MavenPublication::class) {
                from(components.getByName("release"))
            }
        }
    }
}

dependencies {
    compileOnly("androidx.annotation:annotation:1.4.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:core-ktx:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.4.0")
}
