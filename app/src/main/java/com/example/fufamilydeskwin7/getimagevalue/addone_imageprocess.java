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
    public static Mat sobel_outputgray_Y_complet(Mat img) {
        Mat tmp = new Mat();
        Imgproc.cvtColor(img, tmp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(tmp, tmp, new Size(5, 5), 3, 3);
        Imgproc.Sobel(tmp, tmp, CvType.CV_8U, 0, 1);
        Core.convertScaleAbs(tmp, tmp, 10, 0);
        Mat onelayer = new Mat();
        Core.inRange(tmp, new Scalar(200), new Scalar(255), onelayer);
        tmp.copyTo(tmp, onelayer);

        Imgproc.erode(tmp, tmp, Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(4, 2)), new Point(-1, -1), 3);
        Imgproc.erode(tmp, tmp, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 1)), new Point(-1, -1), 1);
        Core.inRange(tmp, new Scalar(230), new Scalar(255), onelayer);
        tmp.copyTo(tmp, onelayer);

        Imgproc.dilate(tmp, tmp, Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(5, 2)), new Point(-1, -1), 2);
        Core.inRange(tmp, new Scalar(240), new Scalar(255), onelayer);
        tmp.copyTo(tmp, onelayer);

        return onelayer;
    }
    public static Mat sobel_outputgray_X_complet(Mat img) {
        Mat tmp = new Mat();
        Imgproc.cvtColor(img, tmp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(tmp, tmp, new Size(5, 5), 3, 3);
        Imgproc.Sobel(tmp, tmp, CvType.CV_8U, 1, 0);
        Core.convertScaleAbs(tmp, tmp, 10, 0);
        Mat onelayer = new Mat();
        Core.inRange(tmp, new Scalar(200), new Scalar(255), onelayer);
        tmp.copyTo(tmp, onelayer);

        Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(2, 4)), new Point(-1, -1), 3);
        Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 2)), new Point(-1, -1), 1);
        Core.inRange(tmp, new Scalar(230), new Scalar(255), onelayer);
        tmp.copyTo(tmp, onelayer);

        Imgproc.dilate(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(2, 5)), new Point(-1, -1), 2);
        Core.inRange(tmp, new Scalar(240), new Scalar(255), onelayer);
        tmp.copyTo(tmp, onelayer);

        return onelayer;
    }
    public static Mat sobel_outputgray_Y(Mat img) {
        Mat tmp = new Mat();
        Imgproc.cvtColor(img, tmp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(tmp, tmp, new Size(3, 3), 5, 5);
        Imgproc.Sobel(tmp, tmp, CvType.CV_8U, 0, 1);
        Core.convertScaleAbs(tmp, tmp, 10, 0);
        Mat onelayer = new Mat();
        Core.inRange(tmp, new Scalar(200), new Scalar(255), onelayer);
//        Imgproc.erode_Y(onelayer, onelayer, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5)));
        tmp.copyTo(tmp, onelayer);

//        img.release();
//        tmp.release();
        return tmp;
    }
    public static Mat sobel_outputgray_X(Mat img) {
        Mat tmp = new Mat();
        Imgproc.cvtColor(img, tmp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(tmp, tmp, new Size(3, 3), 5, 5);
        Imgproc.Sobel(tmp, tmp, CvType.CV_8U, 1, 0);
        Core.convertScaleAbs(tmp, tmp, 10, 0);
        Mat onelayer = new Mat();
        Core.inRange(tmp, new Scalar(200), new Scalar(255), onelayer);
//        Imgproc.erode_Y(onelayer, onelayer, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5)));
        tmp.copyTo(tmp, onelayer);

//        img.release();
//        tmp.release();
        return tmp;
    }


    public static Mat sobel_outputgray_XY_noGaussianBlur(Mat img) {
        Mat tmp = new Mat();
//        Imgproc.cvtColor(img, tmp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(tmp, tmp, new Size(3, 3), 5, 5);
        Imgproc.Sobel(img, tmp, CvType.CV_8U, 1, 0);

        Core.convertScaleAbs(tmp, tmp, 10, 0);
        Mat onelayer = new Mat();
        Core.inRange(tmp, new Scalar(240), new Scalar(255), onelayer);
//        Imgproc.erode_Y(onelayer, onelayer, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5)));
        tmp.copyTo(tmp, onelayer);
//        Core.convertScaleAbs(tmp, tmp, 2, 0);
//        img.release();
//        tmp.release();
        return onelayer;
    }
    //Y向 侵蝕
    public static Mat erode_Y(Mat img) {
        Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(4, 2)), new Point(-1, -1), 3);

        Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 1)), new Point(-1, -1), 1);
//        Mat back=new Mat();

        return img;
    }

    //Y向 膨脹
    public static Mat dilate_Y(Mat img) {
        Imgproc.dilate(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(5, 2)), new Point(-1, -1), 2);
        return img;
    }
    //X向 侵蝕
    public static Mat erode_X(Mat img) {
        Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(2, 4)), new Point(-1, -1), 3);

        Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 2)), new Point(-1, -1), 1);
//        Mat back=new Mat();

        return img;
    }
    //X向 膨脹
    public static Mat dilate_X(Mat img) {
        Imgproc.dilate(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(2, 5)), new Point(-1, -1), 2);
        return img;
    }

    public static Mat Tile_dilate(Mat img) {
//        Imgproc.erode_Y(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)), new Point(-1, -1), 3);
        Imgproc.dilate(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3)), new Point(-1, -1), 3);
        Mat onelayer = new Mat();
        Core.inRange(img, new Scalar(250), new Scalar(255), onelayer);
        img.copyTo(img, onelayer);
        return img;
    }
    public static Mat Tile_erode(Mat img) {
        Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10, 10)), new Point(-1, -1), 1);
        Mat onelayer = new Mat();
        Core.inRange(img, new Scalar(253), new Scalar(255), onelayer);
        img.copyTo(img, onelayer);
        return img;
    }
    public static Mat Tile_dilate2(Mat img) {
//        Imgproc.erode_Y(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)), new Point(-1, -1), 3);
        Imgproc.dilate(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(7, 7)), new Point(-1, -1), 3);
        Mat onelayer = new Mat();
        Core.inRange(img, new Scalar(250), new Scalar(255), onelayer);
        img.copyTo(img, onelayer);
        return img;
    }
    public static Mat Tile_erode2(Mat img) {
        Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(20, 20)), new Point(-1, -1), 1);
        Mat onelayer = new Mat();
        Core.inRange(img, new Scalar(253), new Scalar(255), onelayer);
        img.copyTo(img, onelayer);
        return img;
    }

    public static Mat hsv_s_erode_dilate(Mat img) {
        Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(4, 4)), new Point(-1, -1), 1);
        Imgproc.dilate(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(4, 4)), new Point(-1, -1), 1);
        return img;
    }

    public static Mat body_hsv(Mat img) {
        Mat hsv = new Mat();
        Imgproc.cvtColor(img, hsv, Imgproc.COLOR_RGB2HSV);
        Mat hsv_h = new Mat();
        Mat hsv_s = new Mat();
        Core.extractChannel(hsv, hsv_h, 0);
        Core.extractChannel(hsv, hsv_s, 1);

        Mat hsv_h_mask = new Mat();
        Mat hsv_s_mask = new Mat();
        Core.inRange(hsv_h, new Scalar(7), new Scalar(17), hsv_h_mask);
        Core.inRange(hsv_s, new Scalar(40), new Scalar(128), hsv_s_mask);
        Imgproc.erode(hsv_h_mask, hsv_h_mask, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)), new Point(-1, -1), 1);
        Imgproc.erode(hsv_h_mask, hsv_h_mask, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(2, 2)), new Point(-1, -1), 1);
        Imgproc.dilate(hsv_h_mask, hsv_h_mask, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)), new Point(-1, -1), 1);
        Imgproc.dilate(hsv_h_mask, hsv_h_mask, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(2, 2)), new Point(-1, -1), 1);

        Imgproc.erode(hsv_s_mask, hsv_s_mask, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)), new Point(-1, -1), 1);
        Imgproc.erode(hsv_s_mask, hsv_s_mask, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(2, 2)), new Point(-1, -1), 1);
        Imgproc.dilate(hsv_s_mask, hsv_s_mask, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)), new Point(-1, -1), 1);
        Imgproc.dilate(hsv_s_mask, hsv_s_mask, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(2, 2)), new Point(-1, -1), 1);


        Mat bodyrgb = new Mat();
        img.copyTo(bodyrgb, hsv_h_mask);
//        bodyrgb.copyTo(bodyrgb, hsv_s_mask);

//        img.copyTo(bodyrgb, hsv_s_mask);

        return bodyrgb;
    }

    public static Mat body_rgb(Mat img) {
//        Mat hsv = new Mat();
//        Imgproc.cvtColor(img, hsv, Imgproc.COLOR_RGB2HSV);
//        Mat rgb_r = new Mat();
//        Mat rgb_g = new Mat();
//        Core.extractChannel(img, hsv_h, 0);
//        Core.extractChannel(img, hsv_s, 1);

        Mat rgb_mask_1 = new Mat();
        Mat rgb_mask_2 = new Mat();
        Core.inRange(img, new Scalar(0, 30, 30), new Scalar(40, 170, 256), rgb_mask_1);
        Core.inRange(img, new Scalar(156, 30, 30), new Scalar(180, 170, 256), rgb_mask_2);

        Mat bodyrgb = new Mat();
        img.copyTo(bodyrgb, rgb_mask_1);
        bodyrgb.copyTo(bodyrgb, rgb_mask_2);

        return bodyrgb;
    }

    public static Mat getcol(Mat img, int number) {
        Mat outputcol = img.col(number);

        return outputcol;
    }

}
