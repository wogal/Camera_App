package com.egs.wogal.camera_app;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.egs.wogal.camera_app.For_Sale_Object.Serialize_DeSerilize_Object;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class check_strorage_activity extends AppCompatActivity implements View.OnClickListener {
    private Button mBut_write;
    private Button mBut_read;
    private Button mBut_todo_1;
    private Button mBut_todo_2;
    private TextView txtviewStatus;
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_strorage_activity);
        // init code
        mBut_write = (Button) findViewById(R.id.button_dsu_write);
        mBut_read = (Button) findViewById(R.id.button_dsu_read);
        mBut_todo_1 = (Button) findViewById(R.id.button_dsu_todo_1);
        mBut_todo_2 = (Button) findViewById(R.id.button_dsu_todo_2);
        txtviewStatus = (TextView) findViewById(R.id.txtView_status);

        mBut_write.setOnClickListener(this);
        mBut_read.setOnClickListener(this);
        mBut_todo_1.setOnClickListener(this);
        mBut_todo_2.setOnClickListener(this);
        // put in data path
        File Root = Environment.getExternalStorageDirectory();
        File Dir = new File(Root.getAbsolutePath() + "/WogalsFolder");
        String str = Dir.getAbsolutePath();
        txtviewStatus.setText(str);

    }

    @Override
    public void onClick(View v) {
        String str;
        switch (v.getId()) {
            case R.id.button_dsu_write: {
                try {
                    str = "But " + v.getId();
                    txtviewStatus.setText(str);
                    writeExternalStorage(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.button_dsu_read: {
                str = "But " + v.getId();
                txtviewStatus.setText(str);
                readExternalStoarge(v);
                break;
            }
            case R.id.button_dsu_todo_1: { // save car object
                saveCarObj();
                str = "But " + v.getId();
                txtviewStatus.setText(str);
                //     saveCarObj();
                break;
            }
            case R.id.button_dsu_todo_2: {
                getCarObj();
                str = "But " + v.getId();
                txtviewStatus.setText(str);
                break;
            }
        }
    }

    private void getCarObj() {
        List<Car> carList = null;
        String filePath;
        String fileName;
        String fileAbstotalPath = "";
        filePath = getResources().getText(R.string.F_S_O_FileFolder).toString();
        fileName = getResources().getText(R.string.F_S_O_FileName).toString();
        fileAbstotalPath = Storage_Helper_Class.MakeAbsoulteFromPathAndFile(filePath, fileName);
        carList = Serialize_DeSerilize_Object.desrialize(fileAbstotalPath);
    }


    private void saveCarObj() {
        List<Car> myCars = new ArrayList<Car>();
        myCars.add(new Car("Ford", 1942, R.drawable.help, " Work"));
        myCars.add(new Car("toy", 1930, R.drawable.heart, "Need Work"));
        myCars.add(new Car("chev", 1940, R.drawable.fish, "Need Work"));
        myCars.add(new Car("wogasl", 1970, R.drawable.lightning, "Need Work"));
        myCars.add(new Car("nissain", 1977, R.drawable.star, "Need Work"));
        myCars.add(new Car("earle", 1910, R.drawable.bug, "Need Work"));
        myCars.add(new Car("bus", 1923, R.drawable.up, "Need Work"));
        myCars.add(new Car("British", 1999, R.drawable.lightning, "Need Work"));
        String filePath;
        String fileName;
        String fileAbstotalPath = "";
        filePath = getResources().getText(R.string.F_S_O_FileFolder).toString();
        fileName = getResources().getText(R.string.F_S_O_FileName).toString();
        fileAbstotalPath = Storage_Helper_Class.MakeAbsoulteFromPathAndFile(filePath, fileName);

        Serialize_DeSerilize_Object.serialize(myCars, fileAbstotalPath);
        txtviewStatus.setText(fileAbstotalPath);
    }

    private void readExternalStoarge(View v) {
    }

    private void writeExternalStorage(View v) throws IOException {
        String state;
        state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsolutePath() + "/Wogals_TesT");
            if (!Dir.exists()) {
                Dir.mkdir();
            }
            File file = new File(Dir, "Wogals_NEW_File.txt");
            String message = " Well Hello Wogal";
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(message.getBytes());
            fileOutputStream.close();
        } else {

            //  }
            //  }


        }
    }
}






















