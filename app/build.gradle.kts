plugins {
    id("com.android.application")
    id("realm-android")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        viewBinding = true
        compose = true
    }
    packagingOptions{
        // Exclude file to avoid
        // Error: Duplicate files during packaging of APK
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/services/javax.annotation.processing.Processor")
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/LICENSE.md")
    }
}

realm {
    isSyncEnabled = true
}



dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    implementation("io.realm:realm-gradle-plugin:10.16.1")
// define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.11.0"))

    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    //cloudinary
    implementation("com.cloudinary:cloudinary-android:2.3.1")
    implementation("com.cloudinary:cloudinary-android-download:2.3.1")
    implementation("com.cloudinary:cloudinary-android-preprocess:2.3.1")
    //mail package
    // https://mvnrepository.com/artifact/com.sun.activation/jakarta.activation
    implementation("com.sun.activation:jakarta.activation:2.0.1")
    // https://mvnrepository.com/artifact/com.sun.mail/jakarta.mail
    implementation("com.sun.mail:jakarta.mail:2.0.1")
    //load image from url
    implementation("com.squareup.picasso:picasso:2.8")
    //swipe card
    implementation("com.lorentzos.swipecards:library:1.0.9@aar")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("com.android.support.test.espresso:espresso-contrib:3.0.2")
    androidTestImplementation("androidx.test:core:1.5.0")
    testImplementation("org.robolectric:robolectric:4.2.1")
}