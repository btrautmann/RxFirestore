package com.oakwoodsc.rxfirestore;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

/**
 * Created by Brandon Trautmann
 */

public class UpdateOnSubscribe implements CompletableOnSubscribe {

  private final DocumentReference reference;
  private final Map<String, Object> updates;

  public UpdateOnSubscribe(DocumentReference reference, Map<String, Object> updates) {
    this.reference = reference;
    this.updates = updates;
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

    reference.update(updates).addOnCompleteListener(listener);

  }
}
