package com.oakwoodsc.rxfirestore;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

/**
 * Created by Brandon Trautmann
 */

public class AddOnSubscribe<T> implements CompletableOnSubscribe {

  private final CollectionReference reference;
  private final T value;

  public AddOnSubscribe(CollectionReference reference, T value) {
    this.reference = reference;
    this.value = value;
  }

  @Override
  public void subscribe(final CompletableEmitter emitter) throws Exception {
    final OnCompleteListener<DocumentReference> listener =
        new OnCompleteListener<DocumentReference>() {
          @Override
          public void onComplete(@NonNull Task<DocumentReference> task) {

            if (!emitter.isDisposed()) {
              if (!task.isSuccessful()) {
                emitter.onError(task.getException());
              } else {
                emitter.onComplete();
              }
            }

          }
        };

    reference.add(value).addOnCompleteListener(listener);
  }
}
