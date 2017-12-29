package com.oakwoodsc.rxfirestore;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;

/**
 * Created by Brandon Trautmann
 */

public class QuerySnapshotsOnSubscribe implements ObservableOnSubscribe<QuerySnapshot> {

    private final Query query;
    private ListenerRegistration registration;

    public QuerySnapshotsOnSubscribe(Query query) {
        this.query = query;
    }

    @Override
    public void subscribe(final ObservableEmitter<QuerySnapshot> emitter) throws Exception {
        final EventListener<QuerySnapshot> listener = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (!emitter.isDisposed()) {
                    if (e == null) {
                        emitter.onNext(documentSnapshots);
                    } else {
                        emitter.onError(new Throwable(e.getMessage()));
                    }
                }

            }
        };

        registration = query.addSnapshotListener(listener);

        emitter.setDisposable(Disposables.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                registration.remove();
            }
        }));

    }
}
