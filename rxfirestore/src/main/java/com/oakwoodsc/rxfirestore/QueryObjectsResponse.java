package com.oakwoodsc.rxfirestore;

import java.util.List;

/**
 * Created by brandontrautmann on 2/4/18.
 */

public class QueryObjectsResponse<T> {

  private final List<T> objects;
  private final List<String> documentIds;

  public QueryObjectsResponse(List<T> objects, List<String> documentIds) {
    this.objects = objects;
    this.documentIds = documentIds;
  }

  public List<T> getObjects() {
    return objects;
  }

  public List<String> getDocumentIds() {
    return documentIds;
  }
}
