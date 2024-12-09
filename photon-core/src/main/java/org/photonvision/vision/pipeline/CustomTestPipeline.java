package org.photonvision.vision.pipeline;

import org.photonvision.vision.frame.Frame;
import org.photonvision.vision.frame.FrameThresholdType;
import org.photonvision.vision.pipe.CVPipe;
import org.photonvision.vision.pipe.impl.BlurPipe;
import org.photonvision.vision.pipeline.result.CVPipelineResult;

import java.util.List;

public class CustomTestPipeline extends CVPipeline<CVPipelineResult, CustomTestPipelineSettings> {
    private static final FrameThresholdType PROCESSING_TYPE = FrameThresholdType.GREYSCALE;

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

    @Override
    protected CVPipelineResult process(Frame frame, CustomTestPipelineSettings settings) {
        if (frame.type != FrameThresholdType.GREYSCALE) {
            // We asked for a GREYSCALE frame, but didn't get one -- best we can do is give up
            return new CVPipelineResult(frame.sequenceID, 0, 0, List.of(), frame);
        }

        var pr = blurPipe.run(frame.processedImage.getMat());



        return new CVPipelineResult(frame.sequenceID, pr.nanosElapsed, 10, List.of(), frame);
    }

//    @Override
//    public void release() {
//        super.release();
//    }
}
