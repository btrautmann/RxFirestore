package com.oakwoodsc.rxfirestorekt

import com.google.firebase.firestore.*
import com.oakwoodsc.rxfirestore.QueryObjectResponse
import com.oakwoodsc.rxfirestore.QueryObjectsResponse
import com.oakwoodsc.rxfirestore.RxFirestoreDb
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.Executor

fun DocumentReference.documentSnapshots(): Observable<DocumentSnapshot> =
  RxFirestoreDb.documentSnapshots(this)

fun Query.querySnapshots(): Observable<QuerySnapshot> =
  RxFirestoreDb.querySnapshots(this)

fun Query.documentChanges(): Observable<DocumentChange> =
  RxFirestoreDb.documentChanges(this)

fun <T> Query.queryObjects(objectClass: Class<T>): Observable<QueryObjectsResponse<T>> =
  RxFirestoreDb.queryObjects(this, objectClass)

fun <T> DocumentReference.queryObject(objectClass: Class<T>): Observable<QueryObjectResponse<T>> =
  RxFirestoreDb.queryObject(this, objectClass)

fun <T> DocumentReference.setDoc(value: T): Completable =
  RxFirestoreDb.set(this, value)

fun <T> DocumentReference.setAndMergeDoc(value: T): Completable =
  RxFirestoreDb.setAndMerge(this, value)

fun DocumentReference.updateDoc(updates: Map<String, Any>): Completable =
  RxFirestoreDb.update(this, updates)

fun DocumentReference.deleteDoc(): Completable =
  RxFirestoreDb.delete(this)

fun <T> CollectionReference.addDoc(value: T): Completable =
  RxFirestoreDb.add(this, value)

fun <T> CollectionReference.addDocAsSingle(value: T): Single<DocumentReference> =
  RxFirestoreDb.addAsSingle(this, value)

fun <T> FirebaseFirestore.run(transaction: Transaction.Function<T>)
  : Completable = RxFirestoreDb.runTransaction(this, transaction)

fun WriteBatch.commitNow(): Completable =
  RxFirestoreDb.commitBatch(this)

fun CollectionReference.deleteCollection(batchSize: Int, executor: Executor): Completable =
  RxFirestoreDb.deleteCollection(this, batchSize, executor)

fun CollectionReference.getCollection(): Single<QuerySnapshot> =
  RxFirestoreDb.getCollection(this)

