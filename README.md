# android-media-fingerprint
A library to create fingerprints for media files

Examples:
```
ImageFingerprinter fingerprinter = new PerceptualHashFingerprinter();
long fingerprint1 = fingerprinter.create(bitmap1);
long fingerprint2 = fingerprinter.create(bitmap2);
boolean similar = fingerprinter.similar(fingerprint1, fingerprint2);
```
# Use with Gradle
add to your repositories

```
repositories {
    maven { url "https://jitpack.io" }
}
```

In your app build.gradle, add:  `implementation "com.github.inder123:android-media-fingerprint:1.0.0"`
