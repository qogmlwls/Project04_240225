plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.project04_240225"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.project04_240225"
        minSdk = 24
        targetSdk = 33
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
    buildFeatures {
        viewBinding = true
    }

//
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.recaptcha:recaptcha:18.4.0")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Gson 컨버터 사용시
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp 로깅 인터셉터
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("com.google.android.gms:play-services-auth-api-phone:18.0.2")

    implementation("com.google.android.gms:play-services-safetynet:17.0.0")
    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    // 이미지 동그랗게 만드는 라이브러리
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.code.gson:gson:2.8.8")
//    implementation 'com.google.code.gson:gson:2.8.8' // 최신 버전을 확인해주세요

    implementation("com.kakao.sdk:v2-all:2.20.1")
//
//    implementation("com.kakao.sdk:v2-user:2.20.0")
//    implementation("com.kakao.sdk:v2-cert:2.20.0")

    implementation("com.navercorp.nid:oauth:5.9.0")
//    implementation("com.navercorp.nid:oauth-jdk8:5.9.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("androidx.legacy:legacy-support-core-utils:1.0.0")
    implementation("androidx.browser:browser:1.4.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.2.1")
    implementation("com.airbnb.android:lottie:3.1.0")
//    implementation(files("libs/oauth-5.9.0.aar"))
////    implementation(project(":oauth-5.9.0"))s
//

//
//    implementation "com.kakao.sdk:v2-user:2.20.0" // 카카오 로그인 API 모듈
//    implementation "com.kakao.sdk:v2-share:2.20.0" // 카카오톡 공유 API 모듈
//    implementation "com.kakao.sdk:v2-talk:2.20.0" // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
//    implementation "com.kakao.sdk:v2-friend:2.20.0" // 피커 API 모듈
//    implementation "com.kakao.sdk:v2-navi:2.20.0" // 카카오내비 API 모듈
//    implementation "com.kakao.sdk:v2-cert:2.20.0" // 카카오톡 인증 서비스 API 모듈
}

