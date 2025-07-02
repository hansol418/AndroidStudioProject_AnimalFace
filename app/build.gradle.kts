plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
    id("kotlin-kapt")
}

android {
    namespace = "com.project.animalface_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.animalface_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding {
        enable = true
    }
}

dependencies {

    implementation ("androidx.core:core-splashscreen:1.0.1")

    implementation("androidx.viewpager2:viewpager2:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    implementation("com.github.bumptech.glide:okhttp3-integration:4.12.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.9")

    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
    implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")


    implementation(libs.androidx.paging.common.android)
    implementation(libs.androidx.paging.runtime.ktx)

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.fragment:fragment-ktx:1.3.6")

    implementation("androidx.webkit:webkit:1.8.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")
    ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("com.jakewharton.threetenabp:threetenabp:1.4.4")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
