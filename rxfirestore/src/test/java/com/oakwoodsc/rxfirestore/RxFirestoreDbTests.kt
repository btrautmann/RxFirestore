package com.oakwoodsc.rxfirestore

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.subscribers.TestSubscriber
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class RxFirestoreDbTests {

  private val documentSnapshotListener = argumentCaptor<EventListener<DocumentSnapshot>>()

  @Before

  fun setUp() {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  fun sanityTest() {
    assertEquals(4, (2 + 2).toLong())
  }

  @Test
  fun testDocumentSnapshots() {
    val docReference = mock<DocumentReference>()
    val snapshot = mock<DocumentSnapshot>()

    with(TestSubscriber.create<DocumentSnapshot>()) {
      RxFirestoreDb.documentSnapshots(docReference).subscribe()

      docReference.verifySnapshotListenerAdded()

    }

  }

  private fun DocumentReference.verifySnapshotListenerAdded() {
    verify(this, times(1))
      .addSnapshotListener(documentSnapshotListener.capture())
  }

}


