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

public class DeleteOnSubscribe implements CompletableOnSubscribe {

    private final DocumentReference reference;

    public DeleteOnSubscribe(DocumentReference reference) {
        this.reference = reference;
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

        reference.delete().addOnCompleteListener(listener);
    }
}
