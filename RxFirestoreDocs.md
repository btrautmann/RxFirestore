# RxFirestore Documentation

### Usage
See the java docs within the [RxFirestoreDb](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/RxFirestoreDb.java) class, which outline the methods exposed to interact with your FirebaseFirestore database.

### Assumptions
This library assumes that you have set your Android project up to use `FirebaseFirestore` already. This means you have included the appropriate dependencies and imported your Google Services JSON, and you are passing in authenticated references to the `RxFirestoreDb` methods.

### Important Note
This library provides seamless realtime update methods which work flawlessly. You should, however, take note of the fact that the methods which complete immediately (such as `set()`, `update()`, `runTransaction()`, etc.) are **blocking** in some cases. The `FirebaseFirestore` SDK does NOT signal completion callbacks in the case that the device is offline, because it only considers a database interaction complete if the result is persisted to the backend. In order to get around this, you should make sure that your subscriptions do not block the UI.

### On the roadmap
I have a few things I'd like to see in this library:
- Tests: I haven't focused on these because I've been developing for my own needs and the API is fairly straight forward, but no library is complete without tests.
- Kotlin support: Because Kotlin > Java
