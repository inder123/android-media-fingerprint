package com.singhinderjeet.mediafingerprint;

import android.graphics.Bitmap;

public interface ImageFingerprint {
    long getFingerPrint(Bitmap bitmap);
    int hammingDistance(long fingerprint1, long fingerprint2);
    boolean isSimilar(long fingerprint1, long fingerprint2);
}
