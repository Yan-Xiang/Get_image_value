package com.example.fufamilydeskwin7.getimagevalue;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by fufamilyDeskWin7 on 2016/2/15.
 */
public class addone_imageprocess {

    public static Mat sobel(Mat img) {
        Mat tmp = new Mat();
        Imgproc.cvtColor(img, tmp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(tmp, tmp, new Size(3, 3), 3, 3);
        Imgproc.Sobel(tmp, tmp, CvType.CV_8U, 0, 1);
        Core.convertScaleAbs(tmp, tmp, 10, 0);
        Mat onelayer = new Mat();
        Core.inRange(tmp,new Scalar(127),new Scalar(255),onelayer);
        tmp.copyTo(tmp,onelayer);
        Imgproc.cvtColor(onelayer, img, Imgproc.COLOR_GRAY2BGRA);
        tmp.release();
        return img;
    }

}
