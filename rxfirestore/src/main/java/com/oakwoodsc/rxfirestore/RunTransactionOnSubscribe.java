package com.oakwoodsc.rxfirestore;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

/**
 * Created by Brandon Trautmann
 */

public class RunTransactionOnSubscribe<T> implements CompletableOnSubscribe {

    private Transaction.Function<T> transaction;
    private FirebaseFirestore database;

    public RunTransactionOnSubscribe(FirebaseFirestore database, Transaction.Function<T> transaction) {
        this.database = database;
        this.transaction = transaction;
    }

    @Override
    public void subscribe(final CompletableEmitter emitter) throws Exception {

        final OnCompleteListener<T> listener = new OnCompleteListener<T>() {
            @Override
            public void onComplete(@NonNull Task<T> task) {

                if (!emitter.isDisposed()) {
                    if (!task.isSuccessful()) {
                        emitter.onError(task.getException());
                    } else {
                        emitter.onComplete();
                    }
                }

            }
        };

        database.runTransaction(transaction).addOnCompleteListener(listener);
    }
}
