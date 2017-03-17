package com.egs.wogal.camera_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class Wogals_Test_Activity extends AppCompatActivity implements View.OnClickListener {
    private Button mButton;
    private List<Car> myCars = new ArrayList<Car>();


    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wogals__test_activity);
        mButton = (Button) findViewById(R.id.button_text_activity_exit);
        mButton.setOnClickListener(this);
        populateCarList();
        populateListView();
        registerClickCallback();
    }

    private void populateCarList() {
        myCars.add(new Car("Ford", 1942, R.drawable.help, " Work"));
        myCars.add(new Car("toy", 1930, R.drawable.heart, "Need Work"));
        myCars.add(new Car("chev", 1940, R.drawable.fish, "Need Work"));
        myCars.add(new Car("wogasl", 1970, R.drawable.lightning, "Need Work"));
        myCars.add(new Car("nissain", 1977, R.drawable.star, "Need Work"));
        myCars.add(new Car("earle", 1910, R.drawable.bug, "Need Work"));
        myCars.add(new Car("bus", 1923, R.drawable.up, "Need Work"));
        myCars.add(new Car("British", 1999, R.drawable.lightning, "Need Work"));

    }

    private void populateListView() {
        ArrayAdapter<Car> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.carsListView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Car> {
        public MyListAdapter() {
            super(
                    Wogals_Test_Activity.this, // this java object
                    R.layout.item_view,  // points to xml object in list view
                    myCars      //  Cars Array list
            );
        }

        // works to populate Listview with in context of screen
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }
            // find car object to work with
            Car currentCar = myCars.get(position);

            // fill the view
            // jpg image from file  into bitmap
            String fullPath = getResources().getString(R.string.test_image_path);
            int Height = 0;
            int Width = 0;


            Bitmap bImage = null;
            try {
                bImage = BitmapFactory.decodeFile(fullPath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Height = bImage.getHeight();
            Width = bImage.getWidth();
            Bitmap bM;

            ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);

            Height = 480;
            Width = 640;


            bM = getResizedBitmap(bImage, Width, Height);


            Height = bM.getHeight();
            Width = bM.getWidth();


            imageView.setImageBitmap(bM);


            //  imageView.setImageResource(currentCar.getIconID());
            // Make:
            TextView makeText = (TextView) itemView.findViewById(R.id.item_txtMake);
            makeText.setText(currentCar.getMake());
            // Year:
            TextView yearText = (TextView) itemView.findViewById(R.id.item_txtYear);
            yearText.setText("" + currentCar.getYear());
            // Condition:
            TextView condionText = (TextView) itemView.findViewById(R.id.item_txtCondition);
            condionText.setText(currentCar.getCondition());
            return itemView;
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.carsListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Car currentCar = myCars.get(position);
                String message = "Index " + position + " make  " + currentCar.getMake();
                TextView txtView = (TextView) findViewById(R.id.bot_text);
                txtView.setText(message);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_text_activity_exit: {
                finish();
                break;
            }
        }
    }

}
