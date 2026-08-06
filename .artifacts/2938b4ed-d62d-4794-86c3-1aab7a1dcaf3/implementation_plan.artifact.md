# Fix SecurityException: Unknown calling package name 'com.google.android.gms'

The application is encountering a `java.lang.SecurityException` with the message "Unknown calling package name 'com.google.android.gms'" when interacting with Google Play Services. This is likely due to a combination of high SDK versions and configuration issues with package visibility or Google Services initialization.

## User Review Required

> [!IMPORTANT]
> The project uses `compileSdk 37`, which is a future/preview version. I will downgrade it to the stable `35` (Android 15) to ensure compatibility with current Google Play Services.

## Proposed Changes

### Build Configuration

#### [MODIFY] [app/build.gradle.kts](file:///Users/musa/AndroidStudioProjects/CarLog/app/build.gradle.kts)
- Downgrade `compileSdk` to `35`.
- Move the `google-services` plugin application to the bottom of the file (using `apply(plugin = ...)` syntax) to ensure it correctly captures the `applicationId`.

### Android Manifest

#### [MODIFY] [AndroidManifest.xml](file:///Users/musa/AndroidStudioProjects/CarLog/app/src/main/AndroidManifest.xml)
- Expand the `<queries>` block to include `com.google.android.gsf` (Google Services Framework).
- Add the `com.google.android.gms.version` metadata tag, which is sometimes required for manual verification by GMS brokers.

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify the build completes successfully.

### Manual Verification
- Deploy the app to the device/emulator.
- Check Logcat to ensure the "Failed to get service from broker" error no longer appears during app startup or when fetching the user profile.
