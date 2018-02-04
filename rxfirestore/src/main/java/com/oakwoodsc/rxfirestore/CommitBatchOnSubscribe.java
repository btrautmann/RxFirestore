package com.oakwoodsc.rxfirestore;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.WriteBatch;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

/**
 * Created by Brandon Trautmann
 */

public class CommitBatchOnSubscribe implements CompletableOnSubscribe {

  private WriteBatch batch;

  public CommitBatchOnSubscribe(WriteBatch batch) {
    this.batch = batch;
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

    batch.commit().addOnCompleteListener(listener);
  }
}
