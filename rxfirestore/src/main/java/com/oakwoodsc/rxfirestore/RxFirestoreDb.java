package com.oakwoodsc.rxfirestore;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * The main class used to interact with Cloud Firestore
 */
public final class RxFirestoreDb {

  /**
   * Listens for {@link DocumentSnapshot}s at the given {@link DocumentReference}
   *
   * @param documentReference the {@link DocumentReference} to listen to {@link DocumentSnapshot}s on
   * @return {@link Observable<DocumentSnapshot>}
   */
  @NonNull
  @CheckResult
  public static Observable<DocumentSnapshot> documentSnapshots(@NonNull DocumentReference documentReference) {
    return Observable.create(new DocumentSnapshotsOnSubscribe(documentReference));
  }

  /**
   * Listens for {@link QuerySnapshot}s at the given {@link Query}
   *
   * @param query the {@link Query} to listen to {@link QuerySnapshot}s on
   * @return {@link Observable<QuerySnapshot>}
   */
  @NonNull
  @CheckResult
  public static Observable<QuerySnapshot> querySnapshots(@NonNull Query query) {
    return Observable.create(new QuerySnapshotsOnSubscribe(query));
  }

  /**
   * A convenience method that grabs the {@link DocumentChange}s from a {@link QuerySnapshot} and
   * emits them one by one, so you can act on each item as it comes down the stream.
   *
   * @param query the {@link Query} to listen to {@link QuerySnapshot}s on and grab
   *              {@link DocumentChange}s from
   * @return {@link Observable<DocumentChange>}
   */
  @NonNull
  @CheckResult
  public static Observable<DocumentChange> documentChanges(@NonNull Query query) {
    return Observable.create(new DocumentChangesOnSubscribe(query));
  }

  /**
   * Listens for {@link QuerySnapshot}s at the given {@link Query}, but instead of passing them
   * directly back like {@link RxFirestoreDb#querySnapshots(Query)}, this bundles the resulting
   * {@link DocumentSnapshot}s in a {@link QueryObjectsResponse<T>}, which contains a {@link java.util.List}
   * of T objects, and {@link java.util.List} of {@link String}s that are the IDs of the
   * {@link DocumentReference}. This allows RxFirestore to have no knowledge of how you've defined your object
   * <p>
   * This method will likely be deprecated in the future because {@link QueryObjectsResponse<T>} should
   * really contain a {@link java.util.Map} and not two {@link java.util.List}s. Also, we could create
   * an object that library consumers can extend which would contain a document ID itself, and then we could
   * pass back a simple object.
   *
   * @param query       the {@link Query} to listen to {@link QuerySnapshot}s on
   * @param objectClass {@link Class} of the desired model
   * @param <T>         type of the model
   * @return {@link Observable<QueryObjectsResponse>}
   */
  @NonNull
  @CheckResult
  public static <T> Observable<QueryObjectsResponse<T>> queryObjects(@NonNull Query query, Class<T> objectClass) {
    return Observable.create(new QueryObjectsOnSubscribe<>(query, objectClass));
  }

  /**
   * Listens for {@link DocumentSnapshot}s at the given {@link DocumentReference}, but instead of
   * passing them directly back like {@link RxFirestoreDb#documentSnapshots(DocumentReference)},
   * this bundles the resulting {@link DocumentSnapshot} in a {@link QueryObjectResponse<T>}, which
   * contains the single T object and a {@link String} that is the ID of the {@link DocumentReference}
   * This allows RxFirestore to have no knowledge of how you've defined your object
   *
   * @param documentReference the {@link DocumentReference} to listen to {@link DocumentSnapshot}s on
   * @param objectClass       {@link Class} of the desired model
   * @param <T>               type of the model
   * @return {@link Observable<QueryObjectResponse>}
   */
  @NonNull
  @CheckResult
  public static <T> Observable<QueryObjectResponse<T>> queryObject(@NonNull DocumentReference documentReference, Class<T> objectClass) {
    return Observable.create(new QueryObjectOnSubscribe<>(documentReference, objectClass));
  }

  /**
   * Creates or overwrites a document at the given {@link DocumentReference}
   * <p>
   * Note, this is a <b>blocking</b> call because of how Firestore handles offline persistence.
   * That means the onComplete() callback will not be called if the user is offline, so it is recommended
   * not to block the UI until this completes
   *
   * @param reference the {@link DocumentReference} to create or set the document at
   * @param value     the object to place at the {@link DocumentReference}
   * @param <T>       the type of the model
   * @return {@link Completable}
   */
  @NonNull
  @CheckResult
  public static <T> Completable set(@NonNull DocumentReference reference, @NonNull T value) {
    return Completable.create(new SetOnSubscribe<>(reference, value, false));
  }

  /**
   * Creates or merges a document at the given {@link DocumentReference}
   * <p>
   * Note, this is a <b>blocking</b> call because of how Firestore handles offline persistence.
   * That means the onComplete() callback will not be called if the user is offline, so it is recommended
   * not to block the UI until this completes
   *
   * @param reference the {@link DocumentReference} to create or set the document at
   * @param value     the object to place at the {@link DocumentReference}
   * @param <T>       the type of the model
   * @return {@link Completable}
   */
  @NonNull
  @CheckResult
  public static <T> Completable setAndMerge(@NonNull DocumentReference reference, @NonNull T value) {
    return Completable.create(new SetOnSubscribe<>(reference, value, true));
  }

  /**
   * Updates a document at the given {@link DocumentReference}
   * <p>
   * Note, this is a <b>blocking</b> call because of how Firestore handles offline persistence.
   * That means the onComplete() callback will not be called if the user is offline, so it is recommended
   * not to block the UI until this completes
   *
   * @param reference the {@link DocumentReference} containing the document to update
   * @param updates   a {@link HashMap} of updates to apply to the document
   * @return {@link Completable}
   */
  @NonNull
  @CheckResult
  public static Completable update(@NonNull DocumentReference reference, @NonNull Map<String, Object> updates) {
    return Completable.create(new UpdateOnSubscribe(reference, updates));
  }

  /**
   * Deletes the document at the given {@link DocumentReference}
   * <p>
   * Note, this is a <b>blocking</b> call because of how Firestore handles offline persistence.
   * That means the onComplete() callback will not be called if the user is offline, so it is recommended
   * not to block the UI until this completes
   *
   * @param reference the {@link DocumentReference} containing the document to delete
   * @return {@link Completable}
   */
  @NonNull
  @CheckResult
  public static Completable delete(@NonNull DocumentReference reference) {
    return Completable.create(new DeleteOnSubscribe(reference));
  }

  /**
   * Adds a document at the given {@link DocumentReference}
   * <p>
   * Note, this is a <b>blocking</b> call because of how Firestore handles offline persistence.
   * That means the onComplete() callback will not be called if the user is offline, so it is recommended
   * not to block the UI until this completes
   *
   * @param reference the {@link DocumentReference} at which to add the document
   * @return {@link Completable}
   */
  @NonNull
  @CheckResult
  public static <T> Completable add(@NonNull CollectionReference reference, T value) {
    return Completable.create(new AddOnSubscribe<>(reference, value));
  }

  /**
   * Runs a {@link Transaction} (series of reads and writes) for the given {@link FirebaseFirestore}
   * instance
   * <p>
   * Note, this is a <b>blocking</b> call because of how Firestore handles offline persistence.
   * That means the onComplete() callback will not be called if the user is offline, so it is recommended
   * not to block the UI until this completes
   *
   * @param database    the {@link FirebaseFirestore} database instance to run the {@link Transaction}
   *                    on
   * @param transaction the {@link Transaction} to run
   * @param <T>         the type of {@link Transaction.Function}
   * @return {@link Completable}
   */
  @NonNull
  @CheckResult
  public static <T> Completable runTransaction(@NonNull FirebaseFirestore database,
                                               @NonNull Transaction.Function<T> transaction) {
    return Completable.create(new RunTransactionOnSubscribe<>(database, transaction));
  }

  /**
   * Comments a {@link WriteBatch} (series of writes <b>only</b>)
   * <p>
   * Note, this is a <b>blocking</b> call because of how Firestore handles offline persistence.
   * That means the onComplete() callback will not be called if the user is offline, so it is recommended
   * not to block the UI until this completes
   *
   * @param batch the {@link WriteBatch} to be committed
   * @return {@link Completable}
   */
  @NonNull
  @CheckResult
  public static Completable commitBatch(@NonNull WriteBatch batch) {
    return Completable.create(new CommitBatchOnSubscribe(batch));
  }

  /**
   * Deletes an entire collection at the given {@link CollectionReference}
   * <p>
   * Note, this is a <b>blocking</b> call because of how Firestore handles offline persistence.
   * That means the onComplete() callback will not be called if the user is offline, so it is recommended
   * not to block the UI until this completes. Also, it would be smart to use an artificially high batch size,
   * because the first deletion batch will never complete if the user is offline, thus causing the entire
   * collection to not be deleted.
   * <p>
   * If you are using this method to clean up sub-collections of a deleted document, consider using
   * a Firestore Function instead.
   *
   * @param collectionReference the {@link CollectionReference} pointing to the collection to be deleted
   * @param batchSize           the size of batches to delete in
   * @param executor            the {@link Executor} to use when running delete {@link com.google.android.gms.tasks.Task}s
   * @return
   */
  @NonNull
  @CheckResult
  public static Completable deleteCollection(@NonNull CollectionReference collectionReference,
                                             int batchSize, @NonNull Executor executor) {
    return Completable.create(new DeleteCollectionOnSubscribe(collectionReference,
        batchSize, executor));
  }

  /**
   * Gets the {@link QuerySnapshot} for the given {@link CollectionReference}. This is different from
   * the real-time listening methods in that it completes upon fetching the collection
   *
   * @param collectionReference the {@link CollectionReference} containing the collection to fetch
   * @return {@link Single<QuerySnapshot>}
   */
  @NonNull
  @CheckResult
  public static Single<QuerySnapshot> getCollection(@NonNull CollectionReference collectionReference) {
    return Single.create(new GetCollectionOnSubscribe(collectionReference));
  }


}
