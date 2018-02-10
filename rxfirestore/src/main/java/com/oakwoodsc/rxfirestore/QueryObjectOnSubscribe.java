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
 * Created by brandontrautmann on 2/3/18.
 */

public class QueryObjectOnSubscribe<T> implements ObservableOnSubscribe<QueryObjectResponse<T>> {

  private final DocumentReference documentReference;
  private final Class<T> objectClass;
  private ListenerRegistration registration;

  public QueryObjectOnSubscribe(DocumentReference documentReference, Class<T> objectClass) {
    this.documentReference = documentReference;
    this.objectClass = objectClass;
  }

  @Override
  public void subscribe(final ObservableEmitter<QueryObjectResponse<T>> emitter) throws Exception {
    final EventListener<DocumentSnapshot> listener = new EventListener<DocumentSnapshot>() {
      @Override
      public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
        if (!emitter.isDisposed()) {
          if (e == null) {
            if (documentSnapshot.exists()) {
              T object = documentSnapshot.toObject(objectClass);
              String documentId = documentSnapshot.getId();
              emitter.onNext(new QueryObjectResponse<>(object, documentId));
            } else {
              emitter.onError(new IllegalAccessException("This document does not exist"));
            }
          } else {
            emitter.onError(e);
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
