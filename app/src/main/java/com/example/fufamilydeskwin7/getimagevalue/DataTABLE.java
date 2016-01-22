package com.example.fufamilydeskwin7.getimagevalue;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import static android.provider.BaseColumns._ID;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBDataNAME;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBG11_C80100point;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBG7_C80100point;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBHSV_Spoint;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBTABLE_NAME;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBcompleteset;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBfloor;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBpeople;
import static com.example.fufamilydeskwin7.getimagevalue.DBHelper.DBpillar;

public class DataTABLE extends AppCompatActivity {
    private static final String TAG = "OCVSample::Activity";
    private DBHelper dbhelper = null;
    SQLiteDatabase db;
    private String Dataname;
    private int HSV_Spoint_value, G7_C80100point_value, G11_C80100point_value;
    private TextView tabletext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_table);


        tabletext = (TextView) findViewById(R.id.tabletxt);

        dbhelper = new DBHelper(this);
        dbhelper.close();

        showSQL();

    }
    private Cursor getCursor(){

        db = dbhelper.getReadableDatabase();

        String[] columns = {_ID, DBDataNAME, DBHSV_Spoint, DBG7_C80100point, DBG11_C80100point, DBcompleteset, DBfloor, DBpeople, DBpillar};
        Cursor cursor = db.query(DBTABLE_NAME, columns, null, null, null, null, null);
        startManagingCursor(cursor);

        return  cursor;
    }
    private void showSQL(){

        Cursor cursor = getCursor();
        StringBuilder resultData = new StringBuilder();
        resultData.append("Table總數 : ").append(cursor.getCount()).append("\n");
        resultData.append("\t\t").append("HSV_S").append("\t").append("G7_C80100").append("\t").append("G11_C80100");
        resultData.append("\t\t").append("set").append("\t").append("floor").append("\t").append("people").append("\t").append("pillar").append("\t");
        resultData.append("\n\n");
        while(cursor.moveToNext()){

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int HSV_Spoint = cursor.getInt(2);
            int G7_C80100point = cursor.getInt(3);
            int G11_C80100point = cursor.getInt(4);
            int completeset = cursor.getInt(5);
            int floor = cursor.getInt(6);
            int people = cursor.getInt(7);
            int pillar = cursor.getInt(8);

            resultData.append(id).append(": ").append("Data ID : ").append(name).append(", ");
            resultData.append("\n");
            resultData.append("\t\t").append(String.format("%05d", HSV_Spoint));
            resultData.append("\t\t\t").append(String.format("%05d", G7_C80100point));
            resultData.append("\t\t\t").append(String.format("%05d", G11_C80100point));
            resultData.append("\t\t\t").append(String.format("%02d", completeset));
            resultData.append("\t\t").append(String.format("%02d", floor));
            resultData.append("\t\t\t").append(String.format("%02d", people));
            resultData.append("\t\t").append(String.format("%02d", pillar));


//            resultData.append(cursor.getInt(13)).append(", ");
            resultData.append("\n");
        }

        tabletext.setText(resultData);
        cursor.close();
    }

    public  void closeactivity() {
        Log.i(TAG, "db.close()");
        db.close();
        finish();
        Log.i(TAG, "leave OK");
    }
    public void leavetable (View v) {
        closeactivity();
    }
    @Override
    protected void onStop() {
        super.onStop();
        closeactivity();
    }
    @Override
    protected void onPause() {
        super.onPause();
        closeactivity();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeactivity();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_table, menu);
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
}
