package com.singhinderjeet.mediafingerprint;

import java.io.File;

import android.graphics.Bitmap;

public interface ImageFingerprinter {
    long create(File imageFile);
    long create(Bitmap bitmap);
    int hammingDistance(long fingerprint1, long fingerprint2);
    boolean similar(long fingerprint1, long fingerprint2);
}
