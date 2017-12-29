package com.oakwoodsc.rxfirestore;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;

/**
 * Created by Brandon Trautmann
 */

public class DocumentSnapshotsOnSubscribe implements ObservableOnSubscribe<DocumentSnapshot> {

    private final DocumentReference documentReference;
    private ListenerRegistration registration;

    public DocumentSnapshotsOnSubscribe(DocumentReference documentReference) {
        this.documentReference = documentReference;
    }

    @Override
    public void subscribe(final ObservableEmitter<DocumentSnapshot> emitter) throws Exception {
        final EventListener<DocumentSnapshot> listener = new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (!emitter.isDisposed()) {
                    if (e == null) {
                        emitter.onNext(documentSnapshot);
                    } else {
                        emitter.onError(new Throwable(e.getMessage()));
                    }
                }
            }

        };

        registration = documentReference.addSnapshotListener(listener);

        emitter.setDisposable(Disposables.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                registration.remove();
            }
        }));
    }


}
