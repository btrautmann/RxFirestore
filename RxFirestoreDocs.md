# RxFirestore Documentation

[`RxFirestoreDb`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/RxFirestoreDb.java) is the main class for interacting with the RxJava implementation of Cloud Firestore. This class will contain the methods to help you create, delete, update and listen for changes from documents and collections.

### Add Data
##### `RxFirestoreDb.set()`
Create or overwrite a document at the given `DocumentReference`. Takes the `DocumentReference` and `T` value to be set. Returns a `Completable`. Subscribers should implement `onComplete()` and `onError()`.

Relevant class: [`SetOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/SetOnSubscribe.java)

Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/manage-data/add-data).

***

##### `RxFirestoreDb.add()`
Add a document at the given `CollectionReference`. Takes the `DocumentReference` and `T` value to be added. Returns a `Completable`. Subscribers should implement `onComplete()` and `onError()`.

This is similar to `RxFirestoreDb.set()`, but allows Firestore to [auto-generate an ID]() for this document.

Relevant class: [`AddOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/AddOnSubscribe.java)

Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/manage-data/add-data).

***

### Transactions and Batched Writes
##### `RxFirestoreDb.runTransaction()`
Run a transaction (a series of reads and writes) for the given database instance. Takes the `FirebaseFirestore` (database) to run `Transaction` on, and the `Transaction<TReturn>`. Returns a `Completable<TReturn>`. Subscribers should implement `onComplete()` and `onError()`

Relevant class: [`RunTransactionOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/RunTransactionOnSubscribe.java)

Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/manage-data/transactions).

***

##### `RxFirestoreDb.commitBatch()`
Commit a `WriteBatch` (a series of writes only). Takes the `WriteBatch` to be committed. Returns a `Completable` Subscribers should implement`onComplete()` and `onError()`.

Relevant class: [`CommitBatchOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/CommitBatchOnSubscribe.java)

Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/manage-data/transactions).

***

### Delete Data
##### `RxFirestoreDb.delete()`
Delete a document at the given `DocumentReference`. Takes the `DocumentReference` to delete. Returns a `Completable`. Subscribers should implement `onComplete()` and `onError()`.

Relevant class: [`DeleteOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/DeleteOnSubscribe.java)

Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/manage-data/delete-data).

***

##### `RxFirestoreDb.deleteCollection()`
Delete an entire collection. Takes the `CollectionReference` for which to delete all documents, the batch size to delete in (i.e. how many documents are deleted at once, done repeatedly until entire collection is deleted), and an `Executor` to use when running the `Task`. Returns a `Completable`. Subscribers should implement `onComplete()` and `onError()`.

**Note**: I'm currently researching ways to get rid of the last argument and do the backgrounding fully with RxJava.

Relevant class: [`DeleteCollectionOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/DeleteCollectionOnSubscribe.java)

***

### Listen for Realtime Updates
##### `RxFirestoreDb.queryChanges()`
Listen for changes at the given `Query`. Takes the `Query` to listen to. Returns an `Observable<QuerySnapshot>`. Subscribers should implement `onNext()`, `onComplete()` and `onError()`.

It's important that you keep track of the subscription and call `dispose()` when it's no longer needed. From the Firestore [documentation](https://firebase.google.com/docs/firestore/query-data/listen): 

> When you are no longer interested in listening to your data, you must detach your listener so that your event callbacks stop getting called. This allows the client to stop using bandwidth to receive updates.

Relevant class: [`QueryChangesOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/QueryChangesOnSubscribe.java)

Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/query-data/listen).
