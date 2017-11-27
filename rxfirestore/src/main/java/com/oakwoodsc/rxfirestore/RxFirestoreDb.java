package com.oakwoodsc.rxfirestore;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Brandon Trautmann
 */

public final class RxFirestoreDb {

    @NonNull
    @CheckResult
    public static Observable<QuerySnapshot> queryChanges(@NonNull Query query) {
        return Observable.create(new QueryChangesOnSubscribe(query));
    }

    @NonNull
    @CheckResult
    public static <T> Completable set(
            @NonNull DocumentReference ref, @NonNull T value) {
        return Completable.create(new SetOnSubscribe<>(ref, value));
    }

    @NonNull
    @CheckResult
    public static Completable delete(
            @NonNull DocumentReference ref) {
        return Completable.create(new DeleteOnSubscribe(ref));
    }
}
