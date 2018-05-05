# RxFirestore
RxFirestore is an RxJava2 wrapper for [Cloud Firestore](https://firebase.google.com/docs/firestore/), which is a new database solution by Firebase and is currently in beta. It provides a more friendly API than that of Firestore itself for those familiar with RxJava.

[ ![Download](https://api.bintray.com/packages/oakwoodsc/maven/RxFirestore/images/download.svg) ](https://bintray.com/oakwoodsc/maven/RxFirestore/_latestVersion)

#### What does RxFirestore support?
Check out the [RxFirestore docs](https://github.com/btrautmann/RxFirestore/blob/master/RxFirestoreDocs.md).

#### How to use it:
- In your project's `build.gradle`, add the following in the `allProjects` `repositories` block:
```
maven {
  // Required until published to Jcenter
  url  "http://dl.bintray.com/oakwoodsc/maven"
}
```
- Add implementation statement in your app module's `build.gradle`:
```
implementation 'com.oakwoodsc.rxfirestore:rxfirestore:1.0.0'
```
- You're all set!

**Current Status:** In Development

**Currently developed with:**
- Firestore 16.0.0
- RxJava 2.1.5

___

### Contributions welcome
I won't be working on this full-time, rather fast enough to support my own needs. With that said, please feel free to contribute as much as you'd like. Tests are encouraged (though there are none yet).

**Simply:**
- Clone the repo
- Add features
- Manually test by running assembleDebug or assembleRelease on the rxfirestore module and use that aar in your Android project
- Unit test

### Thank You
Thank you in advance to [kunny](https://github.com/kunny) for his [RxFirebase](https://github.com/kunny/RxFirebase)  database implementation, as I'll be mainly porting that to support Cloud Firestore.
*
