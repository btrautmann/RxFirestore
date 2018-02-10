package com.oakwoodsc.rxfirestore;

/**
 * Created by brandontrautmann on 2/9/18.
 */

public class QueryObjectResponse<T> {

  private final T object;
  private final String documentId;

  public QueryObjectResponse(T object, String documentId) {
    this.object = object;
    this.documentId = documentId;
  }

  public T getObject() {
    return object;
  }

  public String getDocumentId() {
    return documentId;
  }
}
