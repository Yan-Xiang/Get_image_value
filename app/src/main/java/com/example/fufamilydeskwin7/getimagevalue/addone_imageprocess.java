package com.example.fufamilydeskwin7.getimagevalue;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by fufamilyDeskWin7 on 2016/2/15.
 */
public class addone_imageprocess {

    public static Mat sobel_gray(Mat img) {
        Mat tmp = new Mat();
        Imgproc.cvtColor(img, tmp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(tmp, tmp, new Size(3, 3), 5, 5);
        Imgproc.Sobel(tmp, tmp, CvType.CV_8U, 0, 1);
        Core.convertScaleAbs(tmp, tmp, 10, 0);
        Mat onelayer = new Mat();
        Core.inRange(tmp, new Scalar(200), new Scalar(255), onelayer);
//        Imgproc.erode(onelayer, onelayer, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5)));
        tmp.copyTo(tmp, onelayer);

//        img.release();
//        tmp.release();
        return tmp;
    }
//侵蝕
    public static Mat erode(Mat img) {
        Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(4, 2)), new Point(-1, -1), 3);

        Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 1)), new Point(-1, -1), 1);
//        Mat back=new Mat();

        return img;
    }

    //    膨脹
    public static Mat dilate(Mat img) {
        Imgproc.dilate(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(4, 2)), new Point(-1, -1), 3);
        return img;
    }
}
