plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.marketplaceapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.marketplaceapp"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))

// Firebase Authentication (para Login y Registro)
    implementation("com.google.firebase:firebase-auth")

// Cloud Firestore (LA LIBRERÍA DE BASE DE DATOS)
    implementation("com.google.firebase:firebase-firestore")

// Opcional: Firebase Storage (para subir fotos de productos)
    implementation("com.google.firebase:firebase-storage")

    // Realtime Database (para guardar productos)
    implementation("com.google.firebase:firebase-database")

    //  Librería Picasso para mostrar imágenes desde URL
    implementation("com.squareup.picasso:picasso:2.8")
}