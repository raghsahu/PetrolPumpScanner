package com.reward.scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.reward.scanner.api_client.Api_Call;
import com.reward.scanner.api_client.Base_Url;
import com.reward.scanner.api_client.RxApiClient;
import com.reward.scanner.model.SuccessModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddcustomerQrScan extends AppCompatActivity {

    ImageView iv_back;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private DecoratedBarcodeView barcodeView;

    private boolean isFlashOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcustomer_qr_scan);

        isFlashOn = false;
        iv_back=findViewById(R.id.iv_back);
        barcodeView = findViewById(R.id.barcode_scanner);
        Button btnFlash = findViewById(R.id.btn_flash);

        if (!hasFlash()) {
            btnFlash.setVisibility(View.GONE);
        }
        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFlashlight();
            }
        });
        //******scan QR**
        if (!checkPermission()) {
            requestPermission();

        } else {
            scanQR();
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight() {
        if (isFlashOn) {
            isFlashOn = false;
            barcodeView.setTorchOff();
        } else {
            isFlashOn = true;
            barcodeView.setTorchOn();
        }
    }

    private void scanQR() {

        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.CODE_39); // Set barcode type
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory());
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            Log.e("scan_result", result.getText()); // QR/Barcode result
            Toast.makeText(AddcustomerQrScan.this, result.getText(), Toast.LENGTH_LONG).show();

            Intent intent = new Intent(AddcustomerQrScan.this, AddCustomerActivity.class);
            intent.putExtra("Qr_data",result.getText());
            startActivity(intent);
            finish();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(AddcustomerQrScan.this, READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(AddcustomerQrScan.this, CAMERA);
        int result2 = ContextCompat.checkSelfPermission(AddcustomerQrScan.this, WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(AddcustomerQrScan.this, new String[]{READ_EXTERNAL_STORAGE, CAMERA,}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean galleryAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (galleryAccepted && cameraAccepted) {

                        // Snackbar.make(view, "Permission Granted, Now you can access gallery and camera.", Snackbar.LENGTH_LONG).show();
                        scanQR();

                    } else {

                        Toast.makeText(this, "Permission Denied, You cannot access gallery and camera.", Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }

                    break;
                }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddcustomerQrScan.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}