package com.oakwoodsc.rxfirestore;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;

/**
 * Created by brandontrautmann on 2/3/18.
 */

public class QueryObjectsOnSubscribe<T> implements ObservableOnSubscribe<QueryObjectsResponse<T>> {

  private final Query query;
  private final Class<T> objectClass;
  private ListenerRegistration registration;

  public QueryObjectsOnSubscribe(Query query, Class<T> objectClass) {
    this.query = query;
    this.objectClass = objectClass;
  }

  @Override
  public void subscribe(final ObservableEmitter<QueryObjectsResponse<T>> emitter) throws Exception {
    final EventListener<QuerySnapshot> listener = new EventListener<QuerySnapshot>() {
      @Override
      public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
        if (!emitter.isDisposed()) {
          if (e == null) {
            List<T> objects = new ArrayList<>();
            List<String> documentIds = new ArrayList<>();

            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
              T object = document.toObject(objectClass);
              String documentId = document.getId();
              objects.add(object);
              documentIds.add(documentId);
            }

            emitter.onNext(new QueryObjectsResponse<>(objects, documentIds));
          } else {
            emitter.onError(e);
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
