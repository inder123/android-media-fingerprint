package com.singhinderjeet.mediafingerprint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This fingerprinter uses MD5 of a file to create a fingerprint.
 * Note that MD5 makes for a really bad fingerprint since it only equates for exact matches.
 * Even a small variation will generate a vastly different fingerprint.
 */
final class Md5Fingerprinter implements FingerprintStrategy {

    /**
     * @param file File containing the contents
     * @return 0 if fingerprint couldn't be calculated, a valid fingerprint otherwise
     */
    @Override
    public long create(File file) {
        try {
            String md5 = calculateMD5(file);
            return md5.hashCode();
        } catch (Throwable e) { // handle errors (like out of memory) as well as exceptions
            return 0;
        }
    }

    @Override
    public boolean similar(long fingerprint1, long fingerprint2) {
        return fingerprint1 == fingerprint2;
    }

    private static String calculateMD5(File file) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        try (InputStream is = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        }
    }
}
