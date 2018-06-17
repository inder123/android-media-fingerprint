package com.singhinderjeet.mediafingerprint;

import java.io.File;

interface FingerprintStrategy {
    long create(File imageFile);
    boolean similar(long fingerprint1, long fingerprint2);
}
