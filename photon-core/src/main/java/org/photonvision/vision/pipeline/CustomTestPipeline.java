package org.photonvision.vision.pipeline;

import edu.wpi.first.math.geometry.Transform3d;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.features2d.*;
import org.opencv.imgproc.Imgproc;
import org.photonvision.common.util.ColorHelper;
import org.photonvision.common.util.math.MathUtils;
import org.photonvision.vision.frame.Frame;
import org.photonvision.vision.frame.FrameThresholdType;
import org.photonvision.vision.pipe.CVPipe;
import org.photonvision.vision.pipe.impl.BlurDetectionPipe;
import org.photonvision.vision.pipe.impl.BlurPipe;
import org.photonvision.vision.pipe.impl.CalculateFPSPipe;
import org.photonvision.vision.pipeline.result.CVPipelineResult;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.calib3d.Calib3d.findEssentialMat;
import static org.opencv.calib3d.Calib3d.recoverPose;
import static org.opencv.core.CvType.CV_16U;
import static org.opencv.features2d.Features2d.drawKeypoints;
import static org.opencv.video.Video.calcOpticalFlowPyrLK;

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
//        if(frameStaticProperties.cameraCalibration != null) {
//            visualOdometry = new VisualOdometry(frameStaticProperties.cameraCalibration.getCameraIntrinsicsMat());
//        }
//        blurDetectionPipe.setParams(new BlurDetectionPipe.BlurDetectionParams(100));
    }


    private static final Scalar FONT_COLOR = ColorHelper.colorToScalar(Color.RED);

    AKAZE detector = AKAZE.create();

    MatOfKeyPoint prev_points;

    @Override
    protected CVPipelineResult process(Frame frame, CustomTestPipelineSettings settings) {
        long total_proc_time = 0;

        String status = "ERROR";

        if (frame.type != FrameThresholdType.GREYSCALE) {
            // We asked for a GREYSCALE frame, but didn't get one -- best we can do is give up
            Imgproc.putText(frame.processedImage.getMat(), "Not Greyscale", new Point(10,50), Imgproc.FONT_HERSHEY_TRIPLEX, 1, FONT_COLOR);
            return new CVPipelineResult(frame.sequenceID, 0, 0, List.of(), frame);
        }

        if(frameStaticProperties.cameraCalibration == null) {
            // The camera must be calibrated
            Imgproc.putText(frame.processedImage.getMat(), "Not Calibrated", new Point(10,50), Imgproc.FONT_HERSHEY_TRIPLEX, 1, FONT_COLOR);
            return new CVPipelineResult(frame.sequenceID, 0, 0, List.of(), frame);
        }

        Mat frame_mat = frame.processedImage.getMat();

        MatOfKeyPoint points = new MatOfKeyPoint();
        detector.detect(frame_mat, points);
        points.convertTo(points, CV_16U);

        if(prev_points != null && points.depth() > 0 && prev_points.depth() > 0){
//            System.out.println("Calc!");
            Mat cam_mat = frameStaticProperties.cameraCalibration.getCameraIntrinsicsMat();
            Mat E, R = new Mat(), t = new Mat(), mask = new Mat();

            int npoints = points.checkVector(2);

            System.out.println("npoints >= 0" + (npoints >= 0));
            System.out.println("points2.checkVector(2) == npoints" + (prev_points.checkVector(2) == npoints));
            System.out.println("points1.type() == points2.type()" + (points.type() == prev_points.type()));
            E = findEssentialMat(prev_points, points, cam_mat, Calib3d.RANSAC);


//            recoverPose(E, points, prev_points, cam_mat, R, t, mask);
//
//
//
//
//            System.out.println(t.width() + ", " + t.height());
////            System.out.println(pos.get(0,3)[0] + ", " + pos.get(1,3)[0] + ", " + pos.get(2,3)[0]);
        }
        prev_points = points;



        drawKeypoints(frame_mat, points, frame_mat);

//        Transform3d pos =


        var fps = calculateFPSPipe.run(null).output;

        return new CVPipelineResult(frame.sequenceID, total_proc_time, fps, List.of(), frame);
    }

    public void featureTracking(Mat img_1, Mat img_2, MatOfPoint2f points1, MatOfPoint2f points2, MatOfByte status) {

  // this function automatically gets rid of points for which tracking fails

  MatOfFloat err = new MatOfFloat();
  Size window_size = new Size(21, 21);
  TermCriteria term_criteria = new TermCriteria(TermCriteria.COUNT + TermCriteria.EPS, 30, 0.01);

  calcOpticalFlowPyrLK(img_1, img_2, points1, points2, status, err, window_size, 3, term_criteria, 0, 0.001);

  // getting rid of points for which the KLT tracking failed or those who have gone outside the frame
//  int index_correction = 0;
//  for (int i = 0; i < status.depth(); i++) {
//    Point pt = points2.toArray()[i - index_correction];
//    if ((status.toArray()[i] == 0) || (pt.x < 0) || (pt.y < 0))
//    {
//      if ((pt.x < 0) || (pt.y < 0))
//      {
//        status.toArray()[i] = 0;
//      }
//      points1
//      points1.erase(points1.begin() + (i - index_correction));
//      points2.erase(points2.begin() + (i - index_correction));
//      index_correction++;
//    }
//  }
}

    void featureDetection(Mat img_1, MatOfKeyPoint points1)  {
      // uses FAST for feature detections
//      MatOfKeyPoint keypoints_1;
//      int fast_threshold = 20;
//      boolean non_max_suppression = true;
      detector.detect(img_1, points1);
    }


    @Override
    public void release() {
        super.release();
    }
}
