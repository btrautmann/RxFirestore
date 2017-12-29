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
Run a transaction (a series of reads and writes) for the given database instance. Takes the `FirebaseFirestore` (database) to run `Transaction` on, and the `Transaction<TReturn>`. Returns a `Completable<TReturn>`. Subscribers should implement `onComplete()` and `onError()`.

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

**Notes**: I'm currently researching ways to get rid of the last argument and do the backgrounding fully with RxJava. The current implementation of `DeleteCollectionOnSubscribe` is exactly how Firebase recommends doing it in the documentation.

Relevant class: [`DeleteCollectionOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/DeleteCollectionOnSubscribe.java)

Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/manage-data/delete-data).

***

### Listen for Realtime Updates
##### Note: For the following, read the warning regarding subscriptions at the end of the docs.
##### Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/query-data/listen).

##### `RxFirestoreDb.querySnapshots()`

Listen for snapshots at the given `Query`. Takes the `Query` to listen to. Returns an `Observable<QuerySnapshot>`. Subscribers should implement `onNext()`, `onComplete()` and `onError()`.

Relevant class: [`QuerySnapshotsOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/QuerySnapshotsOnSubscribe.java)

Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/query-data/listen).

***
##### `RxFirestoreDb.documentSnapshots()`
Listen for snapshots at the given `DocumentReference`. Takes the `DocumentReference` to listen to. Returns an `Observable<DocumentReference>`. Subscribers should implement `onNext()`, `onComplete()` and `onError()`.

Relevant class: [`DocumentSnapshotsOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/DocumentSnapshotsOnSubscribe.java)

Relevant Firestore documentation [here](https://firebase.google.com/docs/firestore/query-data/listen).

***
##### `RxFirestoreDb.documentChanges()`

A convenience method that grabs the `DocumentChange`s from a `QuerySnapshot` and emits them one by one, so you can act on each item as it comes down the stream. Takes the `Query` to listen to. Returns an `Observable<DocumentReference>`. Subscribers should implement `onNext()`, `onComplete()` and `onError()`.

Example:
`
subscription = RxFirestoreDb.documentChanges(query)
                    .filter(filter)
                    .compose(RxUtils.applyObservableSchedulers())
                    .subscribe(this::handleChange, this::handleErrorEvent);
`

Relevant class: [`DocumentChangesOnSubscribe`](https://github.com/btrautmann/RxFirestore/blob/master/rxfirestore/src/main/java/com/oakwoodsc/rxfirestore/DocumentChangesOnSubscribe.java)


Note on Realtime Updates: _It's important that you keep track of realtime update subscriptions and call `dispose()` when they're no longer needed. From the Firestore [documentation](https://firebase.google.com/docs/firestore/query-data/listen):_
