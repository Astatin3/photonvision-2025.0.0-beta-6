package org.photonvision.vision.pipeline;

import org.photonvision.vision.frame.Frame;
import org.photonvision.vision.frame.FrameThresholdType;
import org.photonvision.vision.pipeline.result.CVPipelineResult;

import java.util.List;

public class CustomTestPipeline extends CVPipeline<CVPipelineResult, CustomTestPipelineSettings> {
    private static final FrameThresholdType PROCESSING_TYPE = FrameThresholdType.GREYSCALE;

    public CustomTestPipeline(FrameThresholdType thresholdType) {
        super(thresholdType);
    }

    public CustomTestPipeline(CustomTestPipelineSettings settings) {
        super(PROCESSING_TYPE);
        this.settings = settings;
    }

    @Override
    protected void setPipeParamsImpl() {

    }

    @Override
    protected CVPipelineResult process(Frame frame, CustomTestPipelineSettings settings) {
        if (frame.type != FrameThresholdType.GREYSCALE) {
            // We asked for a GREYSCALE frame, but didn't get one -- best we can do is give up
            return new CVPipelineResult(frame.sequenceID, 0, 0, List.of(), frame);
        }
            return new CVPipelineResult(frame.sequenceID, 50, 10, List.of(), frame);
    }
}
