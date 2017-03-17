package com.egs.wogal.camera_app;

import android.content.Context;
import android.content.ContextWrapper;
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
import android.util.Log;
import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Activity_Main extends AppCompatActivity implements View.OnClickListener {
    private Storage_Helper_Class mWogals_Test_Class;
    private int mTestError;  // used to fool the compile
    private Button but_camera;
    private Button but_login;
    private Button but_test_code;
    private Button but_test_stroage;
    private Button but_start_forSaleObj;

    private static String logtag = "CameraAppS";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;
    private static final String TAG = Activity_Main.class.getSimpleName();
    private GoogleApiClient client;
    private TextureView mTextureView;
    private TextView mTextView;
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
        // force static screen orientation
        //      this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LinearLayoutManager mamager = new LinearLayoutManager(this);
      // assign vars from xml id's
        but_camera = (Button) findViewById(R.id.button_camrea);
        but_login = (Button) findViewById(R.id.button_login);
        but_test_code = (Button) findViewById(R.id.button_fso_listView);
        but_test_stroage = (Button) findViewById(R.id.button_test_storage);
        but_start_forSaleObj = (Button) findViewById(R.id.but_start_forSaleObj);
        but_start_forSaleObj.setOnClickListener(this);
        but_test_stroage.setOnClickListener(this);
        but_camera.setOnClickListener(this);
        but_login.setOnClickListener(this);
        but_test_code.setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.textview_top_header);
        //  client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        mWogals_Test_Class = new Storage_Helper_Class(this);
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        String str = "";
        switch (id) {
            case R.id.button_fso_listView: {
                F_S_O_ListView(v);
                break;
            }
            case R.id.button_camrea:
                str = "But -> Camera";
                takePhoto(v);
                break;
            case R.id.button_login:
                str = "But -> Login";
                test_Login();
                break;
            case R.id.button_test_storage: {
                TestStorage(v);
                break;
            }
            case R.id.but_start_forSaleObj: {
                startForSaleObj(v);
                break;
            }
            default:
                str = "not found";
        }
    }

    private void startForSaleObj(View v) {
        Intent intent = new Intent(this, start_for_sale_obj.class);
        startActivity(intent);
    }

    private void TestStorage(View v) {
        Intent intent = new Intent(this, check_strorage_activity.class);
        startActivity(intent);

        String str = "";
        ContextWrapper c = new ContextWrapper(this);
        //  c.getFilesDir().getPath()
        Storage_Helper_Class sh = new Storage_Helper_Class(this);
        str = "Hi Device Storage Options";
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    private void F_S_O_ListView(View v) {
        // start new page ( intent )
        Intent intent = new Intent(this, Wogals_Test_Activity.class);
        startActivity(intent);

        String str = "";
        ContextWrapper c = new ContextWrapper(this);
        //  c.getFilesDir().getPath()
        Storage_Helper_Class sh = new Storage_Helper_Class(this);
        str = sh.SgetExternalStorageDirectory();
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }


    private void takePhoto(View v) {
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }

    private File createImageFile() throws IOException {
        String imageFileName = "Wogals_IMAGE_0";
        File strorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //    File Tmp_image = File.createTempFile(imageFileName, ".jpg", strorageDirectory);
        String AbsFilePath = strorageDirectory + "/" + imageFileName + ".jpg";
        File image = new File(AbsFilePath);
        mImageFileLocation = image.getAbsolutePath();
        return (image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "Wogal Heck ", Toast.LENGTH_LONG).show();
        if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
            mPhotoCaptureImageView.setImageBitmap(photoCapturedBitmap);
        }
    }

    private void test_Login() {
        Log.e(logtag, "test_Login");
    }


}






