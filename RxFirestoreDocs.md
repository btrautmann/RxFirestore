# RxFirestore Documentation

[`RxFirestoreDb`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/RxFirestoreDb.java) is the main class for interacting with the RxJava implementation of Cloud Firestore. This class will contain the methods to help you create, delete, update and listen for changes from documents and collections.

### Add Data
###### `RxFirestoreDb.set()`
Used to create or overwrite a document at the given `DocumentReference`. Returns a `Completable`. Subscribers should implement `onComplete()` and `onError()`.

Relevant class: [`SetOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/SetOnSubscribe.java)

Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/manage-data/add-data).

***

###### `RxFirestoreDb.add()`
Used to add a document at the given `CollectionReference`. Returns a `Completable`. Subscribers should implement `onComplete()` and `onError()`.

This is similar to `RxFirestoreDb.set()`, but allows Firestore to [auto-generate an ID]() for this document.

Relevant class: [`AddOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/AddOnSubscribe.java)

Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/manage-data/add-data).

***

### Delete Data
###### `RxFirestoreDb.delete()`
Used to delete a document at the given `DocumentReference`. Returns a `Completable`. Subscribers should implement `onComplete()` and `onError()`.

Relevant class: [`DeleteOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/DeleteOnSubscribe.java)

Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/manage-data/delete-data).

***

### Listen for Realtime Updates
###### `RxFirestoreDb.queryChanges()`
Used to listen for changes at the given `Query`. Returns an `Observable<QuerySnapshot>`. Subscribers should implement `onNext()`, `onComplete()` and `onError()`.

It's important that you keep track of the subscription and call `dispose()` when it's no longer needed. From the Firestore [documentation](https://firebase.google.com/docs/firestore/query-data/listen): 

> When you are no longer interested in listening to your data, you must detach your listener so that your event callbacks stop getting called. This allows the client to stop using bandwidth to receive updates.

Relevant class: [`QueryChangesOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/QueryChangesOnSubscribe.java)

Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/query-data/listen).
