package com.example.fufamilydeskwin7.getimagevalue;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBDataNAME;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBG11_C80100point;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBG7_C80100point;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBHSV_Spoint;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBTABLE_NAME;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBcompleteset;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBfloor;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBpeople;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBpillar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "OCVSample::Activity";
    private Button clearbtn, doimage;
    private ToggleButton completeset, floor, people, pillar;
    private ImageView orgimage, outimage, orgsubimage, outimage2, rowimg;
    private Bitmap orgbm, bitmap;
    private int cannycount1;
    private TextView countT;
    private Mat orgimageMat;
    private Mat halforg;
    private Uri uri;
    int newnumber=0;
    Intent it;
    Cursor cursor;

    private DBHelper dbhelper = null;
    SQLiteDatabase db;
    private String Dataname;
    private int HSV_Spoint_value, G7_C80100point_value, G11_C80100point_value;
    private Boolean completeseton ,flooron ,peopleon ,pillaron;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbhelper = new DBHelper(this);
        dbhelper.close();


//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //設定螢幕直向顯示
        orgimage = (ImageView) findViewById(R.id.orgimage);
        orgsubimage = (ImageView) findViewById(R.id.orgsubimage);
        outimage = (ImageView) findViewById(R.id.outputimage);
        outimage2 = (ImageView) findViewById(R.id.outputimage2);
        rowimg = (ImageView) findViewById(R.id.rowimg);
        countT = (TextView) findViewById(R.id.valuetxt);
//        clearbtn = (Button) findViewById(R.id.clear);
        doimage = (Button) findViewById(R.id.starbtn);
        Button buttonImage = (Button) findViewById(R.id.inputimagebtn);
       completeset = (ToggleButton) findViewById(R.id.completeset);
       floor = (ToggleButton) findViewById(R.id.floor);
       people = (ToggleButton) findViewById(R.id.people);
       pillar = (ToggleButton) findViewById(R.id.pillar);





//        clearbtn.setOnClickListener(this);
        doimage.setOnClickListener(this);
        completeset.setOnCheckedChangeListener(this);
        floor.setOnCheckedChangeListener(this);
        people.setOnCheckedChangeListener(this);
        pillar.setOnCheckedChangeListener(this);

//        doimage.setEnabled(false);
        buttonImage.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
/*
String filePath = vSDCard.getCanonicalPath() + File.separator + "地面照片" + File.separator + vlowerFileName;
 Intent intent = new Intent( Intent.ACTION_VIEW );
 File f1 = new File(filePath);
 if(f1.exists()){
  if( vlowerFileName.endsWith("jpg") || vlowerFileName.endsWith("png")){
      // 影像
      intent.setDataAndType(Uri.fromFile(f1), "image/*");}}*/
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(i, 0);

                Log.i(TAG, "Intent: call Result");
     Log.i(TAG, "Intent: set Type");
            }
        });
        Log.i(TAG, "onCreate: leave onCreate");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "Intent: star Result");

        //當使用者按下確定後
        if (resultCode == RESULT_OK) {

            countT.setText("");
            outimage.setImageBitmap(null);
            orgsubimage.setImageBitmap(null);
            completeset.setChecked(false);
            floor.setChecked(false);
            people.setChecked(false);
            pillar.setChecked(false);

            Log.i(TAG, "Intent: requestCode " + String.valueOf(requestCode));
            Log.i(TAG, "Intent: resultCode "+String.valueOf(resultCode));
            Log.i(TAG, "Intent: data "+String.valueOf(data));

            Log.i(TAG, "Intent: Result get Data URI");
            //取得圖檔的路徑位置
            uri = data.getData();

            Log.i(TAG, "Intent: write log");
            //寫log
            Log.e("uri", uri.toString());
            Dataname=uri.getLastPathSegment();


            Log.i(TAG, "Intent: ID-" + uri.getLastPathSegment());
//            Log.i(TAG, "Intent: ID-"+ DocumentsContract.getDocumentId(uri));
            //抽象資料的接口
            ContentResolver cr = this.getContentResolver();
            try {
                Log.i(TAG, "load org image ");
                //由抽象資料接口轉換圖檔路徑為Bitmap
                orgbm = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //減少解析度
                Log.i(TAG, "hsv star");
                Mat sizeMat = new Mat();
                Utils.bitmapToMat(orgbm, sizeMat);
                Imgproc.resize(sizeMat, sizeMat, new Size(320, 240));
                Log.i(TAG, "change bitmap");
                orgbm.recycle();
                System.gc();
                orgbm = Bitmap.createBitmap(320, 240, Bitmap.Config.ARGB_8888);
//                bitmap = Bitmap.createBitmap(320, 240, Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(sizeMat, orgbm);
//                Utils.matToBitmap(sizeMat, bitmap);
                Log.i(TAG, "size finish!!");

                //取得圖片控制項ImageView
                Log.i(TAG, "set org image");
                // 將Bitmap設定到ImageView
                orgimage.setImageBitmap(orgbm);
                Log.i(TAG, "org image OK");

                orgimageMat = sizeMat;
//                makeMat = sizeMat;
//                orgimageMat.copyTo(makeMat);

                Log.i(TAG, "orgbm size: "+orgbm.getHeight()+"x"+orgbm.getWidth());

                Log.i(TAG, "halforg: submat");
                halforg = orgimageMat.submat(0, orgimageMat.height(), 0, orgimageMat.width() / 3);
                bitmap = Bitmap.createBitmap(halforg.width(), halforg.height(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(halforg, bitmap);
                orgsubimage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        Log.i(TAG, "super.onActivityResult: star");
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "super.onActivityResult: OK!");
    }


    //OpenCV类库加载并初始化成功后的回调函数，在此我们不进行任何操作
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            Log.i(TAG, "onManagerConnected star ");
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public void gotoTABLE(View v) {
        it = new Intent(this, DataTABLE.class);
        startActivityForResult(it, newnumber);
    }
   /* private Cursor getCursor(){

        db = dbhelper.getReadableDatabase();

        String[] columns = {_ID, DBDataNAME, DBHSV_Spoint, DBG7_C80100point, DBG11_C80100point};
        cursor = db.query(DBTABLE_NAME, columns, null, null, null, null, null);
        startManagingCursor(cursor);

        return  cursor;
    }
    private void showSQL(){
        Log.i(TAG, "showSQL: star");
        cursor = getCursor();
        StringBuilder resultData = new StringBuilder();
        resultData.append(cursor.getCount()).append("\n");
//        cursor.moveToFirst();
        while(cursor.moveToNext()){

            Log.i(TAG, "showSQL: star getint()");
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int DBHSV_Spoint = cursor.getInt(2);
            int DBG7_C80100point = cursor.getInt(3);
            int DBG11_C80100point = cursor.getInt(4);

            Log.i(TAG, "showSQL: star text");
            resultData.append(id).append(": ").append("Data位置 : ").append(name).append(", ");
            resultData.append("\n").append("\t\t");
            resultData.append("DBHSV_Spoint : ").append(DBHSV_Spoint);
            resultData.append("DBG7_C80100point : ").append(DBG7_C80100point);
            resultData.append("DBG11_C80100point : ").append(DBG11_C80100point);

            Log.i(TAG, "showSQL: text ok");
//            resultData.append(cursor.getInt(13)).append(", ");
            resultData.append("\n");
        }
        Log.i(TAG, "showSQL: set text");
        countT.setText(resultData);
        cursor.moveToFirst();
        cursor.close();
        //db.close();
        Log.i(TAG, "showSQL: finish");
    }*/
    private void add(){//新增資料

        Log.i(TAG, "add star");
        db = dbhelper.getWritableDatabase();
        Log.i(TAG, "call db");
        ContentValues values = new ContentValues();
        Log.i(TAG, "new ContentValues()");

        values.put(DBDataNAME, Dataname);
        Log.i(TAG, "put dataname");
        values.put(DBHSV_Spoint, HSV_Spoint_value);
        values.put(DBG7_C80100point, G7_C80100point_value);
        values.put(DBG11_C80100point, G11_C80100point_value);
        values.put(DBcompleteset,completeseton);
        values.put(DBfloor,flooron);
        values.put(DBpeople,peopleon);
        values.put(DBpillar,pillaron);

        Log.i(TAG, "all put OK");
        db.insert(DBTABLE_NAME, null, values);

    }
    private void dbclose() {
        Log.i(TAG, "dbclose()");
        db.close();
    }

    /** Call on every application resume **/
    @Override
    protected void onResume()
    {
        Log.i(TAG, "Called onResume");
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, mLoaderCallback);

        Log.i(TAG, " onResume OK");
    }
    @Override
    protected void onStop() {
        super.onStop();
        //dbclose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.i(TAG, "onCreateOptionsMenu set");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        StringBuilder valuetext = new StringBuilder();

//        makeMat = new Mat();
//        makeMat = orgimageMat;
        Mat dst = new Mat();
//        Mat halforg = new Mat();                                                //原圖的子集合 不能修改

        if (v==doimage) {
            valuetext.append("hsv_h,hsv_s,hsv_v,rgbcuthsv_s,G7_C80100, G11_C80100\nhsv_h_canny, sobelrgb, erodergb, dilatergb, tile_sobelXYrgb, tilergb\n\n");
            valuetext.append("Data ID : ").append(Dataname).append("\n");
            //HSV---------------------------------------------------------------------------
            Log.i(TAG, "HSV: star");
            Mat hsv = new Mat();
            Mat hsv_s = new Mat();
            Mat mask_s = new Mat(halforg.size(), CvType.CV_8UC1);
            Log.i(TAG, "HSV: RGB2HSV");
            Imgproc.cvtColor(halforg, hsv, Imgproc.COLOR_RGB2HSV);
//            Core.split(makeMat, 1);
            Log.i(TAG, "HSV: take one channel");
            //HSV_H---------------------------------------------------------------------------
            Mat hsv_h = new Mat();
            Core.extractChannel(hsv, hsv_h, 0);
            Imgproc.cvtColor(hsv_h, hsv_h, Imgproc.COLOR_GRAY2RGBA);
            //HSV_S---------------------------------------------------------------------------
            //多層矩陣轉換成幾個單一矩陣
            Core.extractChannel(hsv, hsv_s, 1);
            Log.i(TAG, "HSV: do mask");
            Core.inRange(hsv_s, new Scalar(100), new Scalar(256), mask_s);//76~255
            Log.i(TAG, "HSV: copy to");
            mask_s = addone_imageprocess.hsv_s_erode_dilate(mask_s);
            hsv_s.copyTo(hsv_s, mask_s); //將原圖片經由遮罩過濾後，得到結果dst
            Imgproc.cvtColor(hsv_s, hsv_s, Imgproc.COLOR_GRAY2RGBA);
            Log.i(TAG, "HSV: finish!");
            //output Mat is hsv_s
            //HSV_V---------------------------------------------------------------------------
            Mat hsv_v = new Mat();
            Core.extractChannel(hsv, hsv_v, 2);
            Imgproc.cvtColor(hsv_v, hsv_v, Imgproc.COLOR_GRAY2RGBA);


            //RGB cut HSV_S--------------------------------------------------------------------
            Log.i(TAG, "RGB cut HSV_s: star");
            Mat rgbcuthsv_s = new Mat();
            Log.i(TAG, "RGB cut HSV_s: copy to");
            halforg.copyTo(rgbcuthsv_s, mask_s);
            Log.i(TAG, "RGB cut HSV_s: finish");
            Scalar maskvalue= Core.sumElems(mask_s);

//            valuetext.append("mask count: " + String.valueOf(maskvalue));
//            valuetext.append("\nmask count: " + maskvalue.toString());
            HSV_Spoint_value = Core.countNonZero(mask_s);
            valuetext.append("mask count: " + String.valueOf(HSV_Spoint_value));
            //output Mat is rgbcuthsv_s

            //Gauss and Canny  ==================================================================
            //Gauss3 Canny(80,100)
            Mat G7_C80100 = new Mat();
            Log.i(TAG, "Gauss: star");
            Imgproc.GaussianBlur(halforg, G7_C80100, new Size(5, 5), 3, 3);

            Log.i(TAG, "Canny: star");
            Imgproc.Canny(G7_C80100, G7_C80100, 80, 100);
//            Log.i(TAG, "Canny: do canny count");
//            cannycount1 = Core.countNonZero(makeMat);
//            countT.setText("canny count" + String.valueOf(cannycount1));
            G7_C80100point_value = Core.countNonZero(G7_C80100);
            valuetext.append("\nG7_C80100 count: " + String.valueOf( G7_C80100point_value));
            Imgproc.cvtColor(G7_C80100, G7_C80100, Imgproc.COLOR_GRAY2RGBA);
            Log.i(TAG, "Canny: canny finish");
            //Scalar G7_C80100value= Core.sumElems(G7_C80100);

            //output Mat is G7_C80100

            //Gauss5 Canny(80,100)
            Mat G11_C80100 = new Mat();
            Log.i(TAG, "Gauss: star");
            Imgproc.GaussianBlur(halforg, G11_C80100, new Size(11, 11), 3, 3);

            Log.i(TAG, "Canny: star");
            Imgproc.Canny(G11_C80100, G11_C80100, 80, 100);
//            Log.i(TAG, "Canny: do canny count");
            G11_C80100point_value = Core.countNonZero(G11_C80100);
            valuetext.append("\nG11_C80100 count: " + String.valueOf( G11_C80100point_value));
            Imgproc.cvtColor(G11_C80100, G11_C80100, Imgproc.COLOR_GRAY2RGBA);
            Log.i(TAG, "Canny: canny finish");

            //output Mat is G11_C80100


            //============================================================
            Mat hsv_h_canny = new Mat();
            Imgproc.GaussianBlur(hsv_h, hsv_h_canny, new Size(3, 3), 3, 3);
            Imgproc.Canny(hsv_h_canny, hsv_h_canny, 80, 100);
            Imgproc.cvtColor(hsv_h_canny, hsv_h_canny, Imgproc.COLOR_GRAY2BGRA);

            Mat sobel_Y = new Mat();
            sobel_Y = addone_imageprocess.sobel_outputgray_Y(halforg);
            Mat sobel_Y_rgb = new Mat();
            Imgproc.cvtColor(sobel_Y, sobel_Y_rgb, Imgproc.COLOR_GRAY2BGRA);
            Mat erode_Y = new Mat();
            erode_Y = addone_imageprocess.erode_Y(sobel_Y);
            Mat erode_Y_rgb = new Mat();
            Imgproc.cvtColor(erode_Y, erode_Y_rgb, Imgproc.COLOR_GRAY2BGRA);
            Mat dilate_Y = new Mat();
            dilate_Y = addone_imageprocess.dilate_Y(erode_Y);
            Mat dilate_Y_rgb = new Mat();
            Imgproc.cvtColor(dilate_Y, dilate_Y_rgb, Imgproc.COLOR_GRAY2BGRA);

            Mat sobel_X = addone_imageprocess.sobel_outputgray_X(halforg);
            Mat sobel_X_rgb = new Mat();
            Imgproc.cvtColor(sobel_X, sobel_X_rgb, Imgproc.COLOR_GRAY2BGRA);
            Mat erode_X = addone_imageprocess.erode_X(sobel_X);
            Mat erode_X_rgb = new Mat();
            Imgproc.cvtColor(erode_X, erode_X_rgb, Imgproc.COLOR_GRAY2BGRA);
            Mat dilate_X = addone_imageprocess.dilate_X(erode_X);
            Mat dilate_X_rgb = new Mat();
            Imgproc.cvtColor(dilate_X, dilate_X_rgb, Imgproc.COLOR_GRAY2BGRA);


            Mat tile_sobelXY = new Mat();
            Mat tile = new Mat();
            Imgproc.cvtColor(halforg, tile_sobelXY, Imgproc.COLOR_RGB2GRAY);
            Mat grayrgb = new Mat();
            Mat graymask = new Mat();
//            Core.inRange(tile_sobelXY,new Scalar(245), new Scalar(255), graymask);
            Imgproc.cvtColor(tile_sobelXY, grayrgb, Imgproc.COLOR_GRAY2BGRA);

            tile_sobelXY = addone_imageprocess.sobel_outputgray_XY_noGaussianBlur(tile_sobelXY);
            Mat tile_sobelXYrgb = new Mat();
            Imgproc.cvtColor(tile_sobelXY, tile_sobelXYrgb, Imgproc.COLOR_GRAY2BGRA);

            tile = addone_imageprocess.Tile_dilate(tile_sobelXY);
            Mat tile_dilatergb = new Mat();
            Imgproc.cvtColor(tile, tile_dilatergb, Imgproc.COLOR_GRAY2BGRA);
            tile = addone_imageprocess.Tile_erode(tile);
            Mat tile_erodergb = new Mat();
            Imgproc.cvtColor(tile, tile_erodergb, Imgproc.COLOR_GRAY2BGRA);
            tile = addone_imageprocess.Tile_dilate2(tile);
            tile = addone_imageprocess.Tile_erode2(tile);
            Mat tile_2rgb = new Mat();
            Imgproc.cvtColor(tile, tile_2rgb, Imgproc.COLOR_GRAY2BGRA);


            Mat bodyhsv_rgb = addone_imageprocess.body_hsv(halforg);
            Mat bodyrgb_rgb = addone_imageprocess.body_rgb(halforg);

            Mat arow = addone_imageprocess.getcol(dilate_Y, 50);
            Mat arow_rgb = new Mat();
            Imgproc.cvtColor(arow,arow_rgb, Imgproc.COLOR_GRAY2BGRA);


            Mat Y_imgrgb = new Mat();
            Y_imgrgb = addone_imageprocess.sobel_outputgray_Y_complet(halforg);
            Imgproc.cvtColor(Y_imgrgb, Y_imgrgb, Imgproc.COLOR_GRAY2BGRA);
            Mat X_imgrgb = new Mat();
            X_imgrgb = addone_imageprocess.sobel_outputgray_X_complet(halforg);
            Imgproc.cvtColor(X_imgrgb, X_imgrgb, Imgproc.COLOR_GRAY2BGRA);
            //============================================================

            Log.i(TAG, "hconcat: star new list");
            List<Mat> src = Arrays.asList(hsv_h, hsv_s, hsv_v, rgbcuthsv_s, G7_C80100, G11_C80100);
            Log.i(TAG, "hconcat: do hconcat");
            Core.hconcat(src, dst);
            Log.i(TAG, "hconcat: finish!");
            countT.setText(valuetext);

            List<Mat> src2 = Arrays.asList(tile_dilatergb, tile_2rgb ,  dilate_Y_rgb,  dilate_X_rgb, bodyhsv_rgb, Y_imgrgb, X_imgrgb);//tile_sobelXYrgb, tile_dilatergb, tile_erodergb, tile_2rgb);
            Mat dst2 = new Mat();
            Core.hconcat(src2, dst2);

//            dbhelper = new DBHelper(this);
            add();
            Log.i(TAG, "add() OK!");
//            showSQL();
            Log.i(TAG, "show OK!");
//            dbclose();

//            dbhelper.close();
            dbclose();


            bitmap = Bitmap.createBitmap(dst.width(), dst.height(), Bitmap.Config.ARGB_8888);
            Log.i(TAG, "Finish: bitmap set OK");
            Utils.matToBitmap(dst, bitmap);
            Log.i(TAG, "Finish: set image");
            outimage.setImageBitmap(bitmap);

            bitmap = Bitmap.createBitmap(dst2.width(), dst2.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(dst2, bitmap);
            outimage2.setImageBitmap(bitmap);

//            Imgproc.resize(arow_rgb, arow_rgb, new Size(10, 240));
//            bitmap = Bitmap.createBitmap(arow_rgb.width(), arow_rgb.height(), Bitmap.Config.ARGB_8888);
//            Utils.matToBitmap(arow_rgb, bitmap);
//            rowimg.setImageBitmap(bitmap);

            Mat getline = addone_imageprocess.HoughLines(halforg);
            bitmap = Bitmap.createBitmap(getline.width(), getline.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(getline, bitmap);
            rowimg.setImageBitmap(bitmap);
        }

//        if (v == clearbtn) {
//            Log.i(TAG, "Clear: star");
//            orgimageMat.copyTo(makeMat);
//            Log.i(TAG, "Clear: makeMat=org");
//
//        } else if (v == doimage) {
//            Log.i(TAG, "Gray: star");
//            //将彩色图像数据转换为灰度图像数据并存储到grayMat中
//            Log.i(TAG, "Gray: do doimage");
//            Imgproc.cvtColor(makeMat, makeMat, Imgproc.COLOR_RGB2GRAY);
//            Log.i(TAG, "Gray: doimage finish");
//
//        } else if (v == canny) {
//            Log.i(TAG, "Canny: star");
//            //将彩色图像数据转换为灰度图像数据并存储到grayMat中
//            Log.i(TAG, "Canny: do canny");
////            Imgproc.cvtColor(orgimageMat, makeMat, Imgproc.COLOR_RGB2GRAY);
//            Imgproc.Canny(makeMat, makeMat, 80, 100);
//            Log.i(TAG, "Canny: do canny count");
//            cannycount1 = Core.countNonZero(makeMat);
//            countT.setText("canny count" + String.valueOf(cannycount1));
//            Log.i(TAG, "Canny: canny finish");
//        } else if (v == gauss) {
//            Log.i(TAG, "Gauss: star");
//            Imgproc.GaussianBlur(makeMat, makeMat, new Size(3, 3), 3, 3);
//            Log.i(TAG, "Gauss: finish!");
//        } else if (v == hsv) {
//            Log.i(TAG, "HSV: star");
////            StringBuilder resultData = new StringBuilder();
//            Mat hsv_s = new Mat();
//            Mat mask_s = new Mat(320, 240, CvType.CV_8UC1);
//            Log.i(TAG, "HSV: RGB2HSV");
//            Imgproc.cvtColor(orgimageMat, makeMat, Imgproc.COLOR_RGB2HSV);
////            Core.split(makeMat, 1);
//            Log.i(TAG, "HSV: take one channel");
//            //多層矩陣轉換成幾個單一矩陣
//            Core.extractChannel(makeMat, hsv_s, 1);
//            Log.i(TAG, "HSV: do mask");
//            Core.inRange(hsv_s, new Scalar(150), new Scalar(255), mask_s);
//            //二值化：h值介於50~70 & s值介於100~255 & v值介於120~255
//            Log.i(TAG, "HSV: copy to");
//
//            hsv_s.copyTo(makeMat, mask_s); //將原圖片經由遮罩過濾後，得到結果dst
//
////            Core.subtract(makeMat,,makeMat);
//
//
////            Log.i(TAG, "size finish!!");
////            resultData.append("size count : ").append("\t org Width : ").append( String.valueOf(bitmap.getWidth())).append("\t org Height : ").append(String.valueOf(bitmap.getHeight()));
////            resultData.append("\t\t").append("\t out Width : ").append(String.valueOf(outbm.getWidth())).append("\t out Height : ").append(String.valueOf(outbm.getHeight()));
////                    countT.setText(resultData);
//            Log.i(TAG, "HSV: finish!");
//        }
////        else {
////            outimage.setImageBitmap(null);
////        }


    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.completeset:
                if (isChecked) {
                    // The toggle is enabled
                    completeseton=true;
//                    doimage.setEnabled(true);
                } else {
                    // The toggle is disabled
                    completeseton = false;
//                    doimage.setEnabled(false);
                }
                break;
            case R.id.floor:
                if (isChecked) {
                    flooron=true;
                } else {
                    flooron = false;
                }
                break;
            case R.id.people:
                if (isChecked) {
                    peopleon=true;
                } else {
                    peopleon = false;
                }
                break;
            case R.id.pillar:
                if (isChecked) {
                    pillaron=true;
                } else {
                    pillaron = false;
                }
                break;
        }
    }

}