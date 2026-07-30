# Walkthrough - Firestore Deserialization Fix

I have fixed the `RuntimeException` that was causing the app to crash when deserializing Firestore data into the `Car` model.

## Changes Made

### [CarLog App]

#### [Car.kt](file:///Users/musa/AndroidStudioProjects/CarLog/app/src/main/java/com/example/carlog/model/Car.kt)

I added a default value to the `name` property. This ensures that Kotlin generates a no-argument constructor for the `Car` data class, which is required by Firebase Firestore's `CustomClassMapper`.

```diff
 data class Car(
     val id: String = UUID.randomUUID().toString(),
-    val name: String,
+    val name: String = "",
     val model: String? = null,
```

## Verification Results

### Automated Tests
- Ran `analyze_file` on `Car.kt` and confirmed there are no syntax errors related to the change.

### Manual Verification
- The app should now be able to fetch and display car data from Firestore without crashing.
