package com.oakwoodsc.rxfirestore;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by Brandon Trautmann
 */

public class GetCollectionOnSubscribe implements SingleOnSubscribe<QuerySnapshot> {

  private final CollectionReference collectionReference;

  public GetCollectionOnSubscribe(CollectionReference collectionReference) {
    this.collectionReference = collectionReference;
  }

  @Override
  public void subscribe(final SingleEmitter<QuerySnapshot> emitter) throws Exception {
    OnCompleteListener<QuerySnapshot> listener = new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (!emitter.isDisposed()) {
          if (task.isSuccessful()) {
            emitter.onSuccess(task.getResult());

          } else {
            emitter.onError(task.getException());

          }
        }

      }
    };

    collectionReference.get()
        .addOnCompleteListener(listener);
  }
}
