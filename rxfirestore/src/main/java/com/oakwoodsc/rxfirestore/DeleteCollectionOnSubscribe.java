package com.oakwoodsc.rxfirestore;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

/**
 * Created by Brandon Trautmann
 */

public class DeleteCollectionOnSubscribe implements CompletableOnSubscribe {

  private CollectionReference collectionReference;
  private int batchSize;
  private Executor executor;

  public DeleteCollectionOnSubscribe(CollectionReference collectionReference, int batchSize, Executor executor) {
    this.collectionReference = collectionReference;
    this.batchSize = batchSize;
    this.executor = executor;
  }

  /**
   * Delete all documents in a collection. Uses an Executor to perform work on a background
   * thread. This does *not* automatically discover and delete subcollections.
   */
  @Override
  public void subscribe(final CompletableEmitter emitter) throws Exception {

    final OnCompleteListener<Void> listener = new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {

        if (!emitter.isDisposed()) {
          if (!task.isSuccessful()) {
            emitter.onError(task.getException());
          } else {
            emitter.onComplete();
          }
        }

      }
    };

    Callable<Void> callable = new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        // Get the first batch of documents in the collection
        Query query = collectionReference.orderBy(FieldPath.documentId()).limit(batchSize);

        // Get a list of deleted documents
        List<DocumentSnapshot> deleted = deleteQueryBatch(query);

        // While the deleted documents in the last batch indicate that there
        // may still be more documents in the collection, page down to the
        // next batch and delete again
        while (deleted.size() >= batchSize) {
          // Move the query cursor to start after the last doc in the batch
          DocumentSnapshot last = deleted.get(deleted.size() - 1);
          query = collectionReference.orderBy(FieldPath.documentId())
              .startAfter(last.getId())
              .limit(batchSize);

          deleted = deleteQueryBatch(query);
        }

        return null;
      }
    };


    Tasks.call(executor, callable).addOnCompleteListener(listener);
  }

  /**
   * Delete all results from a query in a single WriteBatch. Must be run on a worker thread
   * to avoid blocking/crashing the main thread.
   */
  @WorkerThread
  private List<DocumentSnapshot> deleteQueryBatch(final Query query) throws Exception {
    QuerySnapshot querySnapshot = Tasks.await(query.get());

    WriteBatch batch = query.getFirestore().batch();
    for (DocumentSnapshot snapshot : querySnapshot) {
      batch.delete(snapshot.getReference());
    }
    Tasks.await(batch.commit());

    return querySnapshot.getDocuments();
  }
}
