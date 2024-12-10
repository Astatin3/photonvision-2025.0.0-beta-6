package org.photonvision.vision.pipeline;

import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.photonvision.common.util.ColorHelper;
import org.photonvision.vision.frame.Frame;
import org.photonvision.vision.frame.FrameThresholdType;
import org.photonvision.vision.pipe.CVPipe;
import org.photonvision.vision.pipe.impl.BlurDetectionPipe;
import org.photonvision.vision.pipe.impl.BlurPipe;
import org.photonvision.vision.pipe.impl.CalculateFPSPipe;
import org.photonvision.vision.pipeline.result.CVPipelineResult;

import java.awt.*;
import java.util.List;

public class CustomTestPipeline extends CVPipeline<CVPipelineResult, CustomTestPipelineSettings> {
    private static final FrameThresholdType PROCESSING_TYPE = FrameThresholdType.GREYSCALE;
    private final CalculateFPSPipe calculateFPSPipe = new CalculateFPSPipe();
    private final BlurDetectionPipe blurDetectionPipe = new BlurDetectionPipe();

    private final BlurPipe blurPipe = new BlurPipe();

    public CustomTestPipeline() {
        super(PROCESSING_TYPE);
        settings = new CustomTestPipelineSettings();
    }

    public CustomTestPipeline(CustomTestPipelineSettings settings) {
        super(PROCESSING_TYPE);
        this.settings = settings;
    }

    @Override
    protected void setPipeParamsImpl() {
//        super.setPipeParamsImpl();
        this.released = false;
        blurPipe.setParams(new BlurPipe.BlurParams(5));
        blurDetectionPipe.setParams(new BlurDetectionPipe.BlurDetectionParams(100));


//        if (frameStaticProperties.cameraCalibration != null) {
//            var cameraMatrix = frameStaticProperties.cameraCalibration.getCameraIntrinsicsMat();
//            if (cameraMatrix != null && cameraMatrix.rows() > 0) {
//                var cx = cameraMatrix.get(0, 2)[0];
//                var cy = cameraMatrix.get(1, 2)[0];
//                var fx = cameraMatrix.get(0, 0)[0];
//                var fy = cameraMatrix.get(1, 1)[0];
//            }
//        }
    }

    private static final Scalar FONT_COLOR = ColorHelper.colorToScalar(Color.RED);


    @Override
    protected CVPipelineResult process(Frame frame, CustomTestPipelineSettings settings) {
        long total_proc_time = 0;

        if (frame.type != FrameThresholdType.GREYSCALE) {
            // We asked for a GREYSCALE frame, but didn't get one -- best we can do is give up
            return new CVPipelineResult(frame.sequenceID, 0, 0, List.of(), frame);
        }

//        var pr = blurPipe.run(frame.processedImage.getMat());
//        total_proc_time += pr.nanosElapsed;

        double blurAmount = blurDetectionPipe.run(frame.processedImage.getMat()).output;
        System.out.println(blurAmount);

        Imgproc.putText(frame.processedImage.getMat(), "BLUR: " + blurAmount, new Point(10,50), Imgproc.FONT_HERSHEY_TRIPLEX, 1, FONT_COLOR);

        var fps = calculateFPSPipe.run(null).output;

        return new CVPipelineResult(frame.sequenceID, total_proc_time, fps, List.of(), frame);
    }

    @Override
    public void release() {
        super.release();
    }
}
