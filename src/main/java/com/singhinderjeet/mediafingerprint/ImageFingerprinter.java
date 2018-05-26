package com.singhinderjeet.mediafingerprint;

import android.graphics.Bitmap;

public interface ImageFingerprinter {
    long create(Bitmap bitmap);
    int hammingDistance(long fingerprint1, long fingerprint2);
    boolean similar(long fingerprint1, long fingerprint2);
}
