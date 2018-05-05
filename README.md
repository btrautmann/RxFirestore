# RxFirestore
RxFirestore is an RxJava2 wrapper for [Cloud Firestore](https://firebase.google.com/docs/firestore/), which is a new database solution by Firebase and is currently in beta. It provides a more friendly API than that of Firestore itself for those familiar with RxJava. Check out the [RxFirestore docs](https://github.com/btrautmann/RxFirestore/blob/master/RxFirestoreDocs.md) for more information on the API.

[ ![Download](https://api.bintray.com/packages/oakwoodsc/maven/RxFirestore/images/download.svg) ](https://bintray.com/oakwoodsc/maven/RxFirestore/_latestVersion)

#### How to use it:

Kotlin or Java:

- In your project's `build.gradle`, add the following in the `allProjects` `repositories` block:
```
maven {
  // Required until published to Jcenter
  url  "http://dl.bintray.com/oakwoodsc/maven"
}
```

Java:
- Add implementation statement in your app module's `build.gradle`:
```
implementation 'com.oakwoodsc.rxfirestore:rxfirestore:${latestVersion}'
```

Kotlin:
- Add implementation statement in your app module's `build.gradle`:
```
implementation 'com.oakwoodsc.rxfirestore:rxfirestorekt:${latestVersion}'
```

**Current Status:** In Development

**Currently developed with:**
- Firestore 16.0.0
- RxJava 2.1.8

___

### Contributions welcome
I won't be working on this full-time, rather fast enough to support my own needs. With that said, please feel free to contribute as much as you'd like. Tests are encouraged (though there are none yet).

### Thank You
Thank you in advance to [kunny](https://github.com/kunny) for his [RxFirebase](https://github.com/kunny/RxFirebase)  database implementation, as I'll be mainly porting that to support Cloud Firestore.
