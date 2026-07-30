# Fix Firestore Deserialization Error for Car model

Firebase Firestore requires a no-argument constructor to deserialize objects into custom Kotlin classes. In Kotlin, a data class only has a no-argument constructor if all its properties have default values. The `Car` class is currently missing a default value for the `name` property.

## Proposed Changes

### [CarLog App]

#### [MODIFY] [Car.kt](file:///Users/musa/AndroidStudioProjects/CarLog/app/src/main/java/com/example/carlog/model/Car.kt)
- Add a default value to the `name` property in the `Car` data class to ensure a no-argument constructor is generated.

## Verification Plan

### Automated Tests
- I will check if the code compiles after the change.

### Manual Verification
- The user should run the app and verify that the `FATAL EXCEPTION: main` with `Could not deserialize object` error no longer occurs when loading car data from Firestore.
