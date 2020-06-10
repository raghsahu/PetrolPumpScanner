package com.reward.scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    TextView tv_add_customer,tv_scan;
    ImageView iv_scan;
    private IntentIntegrator qrScan;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //intializing scan object
        qrScan = new IntentIntegrator(MainActivity.this);

        tv_add_customer = findViewById(R.id.tv_add_customer);
        tv_scan = findViewById(R.id.tv_scan);
        iv_scan = findViewById(R.id.iv_scan);

        tv_add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCustomerActivity.class);
                startActivity(intent);
            }
        });

        tv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //******scan QR**
                if (!checkPermission()) {
                    requestPermission();

                } else {
                    scanQR();
                    // Snackbar.make(v, "Permission already granted.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        iv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //******scan QR**
                if (!checkPermission()) {
                    requestPermission();

                } else {
                    scanQR();
                    // Snackbar.make(v, "Permission already granted.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void scanQR() {
        //initiating the qr code scan
        qrScan.initiateScan();
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(MainActivity.this, CAMERA);
        int result2 = ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_EXTERNAL_STORAGE, CAMERA,}, PERMISSION_REQUEST_CODE);

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
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //*************QR scan result************
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                //if qrcode has nothing in it
                if (result.getContents() == null) {
                    Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
                } else {
                    //if qr contains data
                   // try {
                        Log.e("scan_data", "" + result.getContents());
                        //converting the data to json
                        //JSONObject obj = new JSONObject(result.getContents());
                       // Log.e("scan_data", "" + obj);

                        //setting values to textviews
                        //textViewName.setText(obj.getString("name"));
                        // textViewAddress.setText(obj.getString("address"));
                  //  } catch (JSONException e) {
                   //     e.printStackTrace();
                        //if control comes here
                        //that means the encoded format not matches
                        //in this case you can display whatever data is available on the qrcode
                        //to a toast
                        Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                   // }
                    Intent intent = new Intent(MainActivity.this, UploadMeterActivity.class);
                   // intent.putExtra("Qr_data",result.getContents());
                    startActivity(intent);

                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

        }
    }
}