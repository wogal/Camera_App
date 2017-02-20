package com.egs.wogal.camera_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import wogals_bungle.AdapterDrops;

public class Activity_Main extends AppCompatActivity implements View.OnClickListener {
    private int mTestError;  // used to fool the compile
    private Button but_camera;
    private Button but_login;
    private RecyclerView mRecycler;
    private Realm mRealm;
    private static String logtag = "CameraAppS";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;
    private static final String TAG = Activity_Main.class.getSimpleName();
    private GoogleApiClient client;
    private TextureView mTextureView;
    private Size mPreviewSize;
    private String mCameraId;
    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private ImageView mPhotoCaptureImageView;
    private String mImageFileLocation = "";
    private TextureView.SurfaceTextureListener mSurfaceTextureListener =
            new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    setupCamera(width, height);
                    // display log
                    Log.e(logtag, "Hi Wogal  onSurfaceTextureAvailable Width " + width + " height " + height);
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                }
            };


    private void setupCamera(int width, int height) {
        Log.e(logtag, "Hi Wogal  setupCamera Width " + width + " height " + height);
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                mPreviewSize = getPreferredPreviewSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private Size getPreferredPreviewSize(Size[] mapSizes, int width, int height) {
        List<Size> collectorSizes = new ArrayList<>();
        for (Size option : mapSizes) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    collectorSizes.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    collectorSizes.add(option);
                }
            }
        }
        if (collectorSizes.size() > 0) {

            return Collections.min(collectorSizes, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum((lhs.getWidth() * lhs.getHeight()) - (rhs.getWidth() * rhs.getHeight()));
                }
            });
        }
        return mapSizes[0];
    }

    protected void onCreate(Bundle savedInstanceState) {
        // good house keeping ( and to fool the compiler )
        mTestError = -1;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity__main);
        mRecycler = (RecyclerView) findViewById(R.id.rv_drops);
        mRecycler.setAdapter(new AdapterDrops(this));
        LinearLayoutManager mamager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mamager);
        // assign vars from xml id's
        mTextureView = (TextureView) findViewById(R.id.textureView);
        but_camera = (Button) findViewById(R.id.button_camrea);
        but_login = (Button) findViewById(R.id.button_login);
        but_camera.setOnClickListener(this);
        but_login.setOnClickListener(this);
        mPhotoCaptureImageView = (ImageView) findViewById(R.id.capturePhotoImage);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    //region overides on pause , start , resume
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(logtag, "Hi Wogal onPause ");
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        Log.e(logtag, "Hi Wogal onStop ");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Log.e(logtag, "Hi Wogal onStart ");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mTextureView.isAvailable()) {

        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
        Log.e(logtag, "Hi Wogal onResume ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(logtag, "Hi Wogal onDestroy ");
    }
    //endregion

    @Override
    public void onClick(View v) {

        int id = v.getId();
        String str = "";
        switch (id) {
            case R.id.button_camrea:
                str = "But -> Camera";
                takePhoto(v);
                break;
            case R.id.button_login:
                str = "But -> Login";
                AddData();
                break;
            case R.id.button_testCode: {
                wogalsTestCode(v);
                break;
            }
            default:
                str = "not found";
        }
        Toast.makeText(Activity_Main.this, "__" + str, Toast.LENGTH_LONG).show();
    }

    private void wogalsTestCode(View v) {
    }

    private void takePhoto(View v) {
        //   if (mTestError == -1)
        //      return;
        Log.e(logtag, " Take Pic ******  ( 1 )  ");
//        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//        try {
//            for (String cameraId : cameraManager.getCameraIdList()) {
//                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
//                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
//                    continue;
//                }
//                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//            }
//        } catch (Exception ex1) {
//            Log.e(logtag, "Error ->" + ex1.getMessage());
//        }
        File photoFile = null;
        Intent callCameraApplicationIntent = new Intent();
        try {
            callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            photoFile = createImageFile();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
//        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpg");
//        imageUri = Uri.fromFile(photo);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, TAKE_PICTURE);
    }

    private File createImageFile() throws IOException {
        //   String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String timeStamp = "Wogals_Pic";
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File strorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", strorageDirectory);
        mImageFileLocation = image.getAbsolutePath();
        return (image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(logtag, " **** Activity Action " + data.getAction());
        if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            try {
//                Bundle extras = data.getExtras();
//                Bitmap photoCapturedBitmap = (Bitmap) extras.get("data");
//                mPhotoCaptureImageView.setImageBitmap(photoCapturedBitmap);
                Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
                mPhotoCaptureImageView.setImageBitmap(photoCapturedBitmap);
            } catch (Exception ex1) {
                Log.e(logtag, "Bitmap -- Error ->" + ex1.getMessage());
            }
        }
    }


    //   @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if (resultCode == Activity.RESULT_OK) {
//            Uri selectedImage = imageUri;
//            getContentResolver().notifyChange(selectedImage, null);
//            ImageView imageView = (ImageView) findViewById(R.id.image_camera);
//            ContentResolver cr = getContentResolver();
//            Bitmap bitmap;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
//                imageView.setImageBitmap(bitmap);
//                Toast.makeText(Activity_Main.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
//            } catch (Exception ex1) {
//                Log.e(logtag, ex1.toString());
//            }
//        }
// }


    private void AddData() {
        Log.e(logtag, "Hi Wogal");


    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Activity_Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}






