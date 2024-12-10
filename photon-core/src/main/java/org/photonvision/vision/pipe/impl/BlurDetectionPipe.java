package org.photonvision.vision.pipe.impl;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.photonvision.vision.pipe.MutatingPipe;
import org.photonvision.vision.pipe.CVPipe;

import static org.opencv.core.Core.meanStdDev;

public class BlurDetectionPipe extends CVPipe<Mat, Double, BlurDetectionPipe.BlurDetectionParams> {
    @Override
    protected Double process(Mat in) {
        Mat destination = new Mat();
        Imgproc.Laplacian(in, destination, 3);

        MatOfDouble pMean = new MatOfDouble();
        MatOfDouble pStdDev = new MatOfDouble();
        meanStdDev(destination, pMean, pStdDev);

        double sum = 0.0;
        double[] arr = pStdDev.toArray();
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        return Math.pow(sum, 2);
    }

    public static class BlurDetectionParams {
        //Default blur settings
        public static BlurDetectionParams DEFAULT = new BlurDetectionParams(0.2);

        // Blur threshold
        private final double threshhold;

        public BlurDetectionParams(double threshhold) {
            this.threshhold = threshhold;
        }

        public double getThreshhold() {
            return threshhold;
        }
    }
}
