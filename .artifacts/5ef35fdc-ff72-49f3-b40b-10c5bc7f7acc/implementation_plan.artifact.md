# Fix SecurityException: Unknown calling package name 'com.google.android.gms'

The application is encountering a `java.lang.SecurityException` with the message "Unknown calling package name 'com.google.android.gms'" when interacting with Google Play Services (via Firebase). This typically occurs due to package visibility restrictions introduced in Android 11 (API 30) or misconfigurations in the app's identity verification.

## User Review Required

> [!IMPORTANT]
> The project is currently using very new/preview versions of the Android SDK (`compileSdk 37`, `targetSdk 36`) and Android Gradle Plugin (`9.3.1`). These versions might have experimental behavior or bugs related to package visibility and Google Play Services integration.

## Proposed Changes

### Android Manifest

#### [MODIFY] [AndroidManifest.xml](file:///Users/musa/AndroidStudioProjects/CarLog/app/src/main/AndroidManifest.xml)
- Add a `<queries>` block to declare visibility for the Google Play Services package (`com.google.android.gms`). This is the most common fix for "Failed to get service from broker" errors on Android 11+.

### Build Configuration

#### [MODIFY] [build.gradle.kts](file:///Users/musa/AndroidStudioProjects/CarLog/app/build.gradle.kts)
- Downgrade `compileSdk` and `targetSdk` to stable versions (API 35) to ensure compatibility with current Google Play Services SDKs, unless there is a specific reason to use preview versions.
- Adjust the `compileSdk` syntax to the standard format if the current one is causing issues.

## Verification Plan

### Automated Tests
- Build the project using `./gradlew assembleDebug` to ensure manifest merging and configuration are correct.

### Manual Verification
- Run the app on a device/emulator with Google Play Services.
- Verify that the `SplashScreen` completes and navigates to the `LoginScreen` or `MainScreen` without the "Failed to get service from broker" error.
- Test Firebase Authentication (login/register) to ensure GMS interaction is working.
