package com.singhinderjeet.mediafingerprint;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

final class PerceptualHashFingerprinter implements FingerprintStrategy {
    // Based on https://github.com/gavinliu/SimilarPhoto

    /**
     * @param imageFile File containing the image
     * @return 0 if fingerprint couldn't be calculated, a valid fingerprint otherwise
     */
    @Override
    public long create(File imageFile) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            long fingerprint = create(bitmap);
            bitmap.recycle();
            return fingerprint;
        } catch (Throwable e) { // handle errors (like out of memory) as well as exceptions
            return 0;
        }
    }

    public long create(Bitmap bitmap) {
        bitmap = scale(bitmap);
        double[][] grayPixels = getGrayPixels(bitmap);
        bitmap.recycle();
        double grayAvg = getGrayAvg(grayPixels);
        return getFingerPrint(grayPixels, grayAvg);
    }

    public int hammingDistance(long fingerprint1, long fingerprint2) {
        int dist = 0;
        long result = fingerprint1 ^ fingerprint2;
        while (result != 0) {
            ++dist;
            result &= result - 1;
        }
        return dist;
    }

    @Override
    public boolean similar(long fingerprint1, long fingerprint2) {
        return fingerprint1 != 0 && fingerprint2 != 0 && hammingDistance(fingerprint1, fingerprint2) <= 5;
    }

    private Bitmap scale(Bitmap bitmap) {
        float scale_width = 8.0f / bitmap.getWidth();
        float scale_height = 8.0f / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale_width, scale_height);

        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return scaledBitmap;
    }

    private static long getFingerPrint(double[][] pixels, double avg) {
        int width = pixels[0].length;
        int height = pixels.length;

        byte[] bytes = new byte[height * width];

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (pixels[i][j] >= avg) {
                    bytes[i * height + j] = 1;
                    sb.append("1");
                } else {
                    bytes[i * height + j] = 0;
                    sb.append("0");
                }
            }
        }

        long fingerprint1 = 0;
        long fingerprint2 = 0;
        for (int i = 0; i < 64; i++) {
            if (i < 32) {
                fingerprint1 += (bytes[63 - i] << i);
            } else {
                fingerprint2 += (bytes[63 - i] << (i - 31));
            }
        }

        return (fingerprint2 << 32) + fingerprint1;
    }

    private static double getGrayAvg(double[][] pixels) {
        int width = pixels[0].length;
        int height = pixels.length;
        int count = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                count += pixels[i][j];
            }
        }
        return count / (width * height);
    }


    private static double[][] getGrayPixels(Bitmap bitmap) {
        int width = 8;
        int height = 8;
        double[][] pixels = new double[height][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j] = computeGrayValue(bitmap.getPixel(i, j));
            }
        }
        return pixels;
    }

    private static double computeGrayValue(int pixel) {
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = (pixel) & 255;
        return 0.3 * red + 0.59 * green + 0.11 * blue;
    }
}
