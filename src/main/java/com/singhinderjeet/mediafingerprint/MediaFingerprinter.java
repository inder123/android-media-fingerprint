package com.singhinderjeet.mediafingerprint;

import java.io.File;

public class MediaFingerprinter {
    private final PerceptualHashFingerprinter pHash = new PerceptualHashFingerprinter();
    private final Md5Fingerprinter md5 = new Md5Fingerprinter();

    public long createFingerprint(File mediaFile, String mimeType) {
        return getStrategy(mimeType).create(mediaFile);
    }

    public boolean similarFingerprints(long fingerprint1, long fingerprint2, String mimeType) {
        return getStrategy(mimeType).similar(fingerprint1, fingerprint2);
    }

    private FingerprintStrategy getStrategy(String mimeType) {
        if (mimeType != null && mimeType.contains("image")) {
            return pHash;
        } else {
            return md5;
        }
    }
}
