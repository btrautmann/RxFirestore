package com.oakwoodsc.rxfirestore;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

/**
 * Created by Brandon Trautmann
 */

public class SetOnSubscribe<T> implements CompletableOnSubscribe {

  private final DocumentReference reference;

  private final T value;

  public SetOnSubscribe(DocumentReference reference, T value) {
    this.reference = reference;
    this.value = value;
  }

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

    reference.set(value).addOnCompleteListener(listener);

  }
}
