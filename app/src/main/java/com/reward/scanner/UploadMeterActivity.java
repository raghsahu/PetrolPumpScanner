package com.reward.scanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.reward.scanner.api_client.Api_Call;
import com.reward.scanner.api_client.Base_Url;
import com.reward.scanner.api_client.RxApiClient;
import com.reward.scanner.model.FindCustomerModel;
import com.reward.scanner.model.SuccessModel;

import java.io.ByteArrayOutputStream;
import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava2.HttpException;

public class UploadMeterActivity extends AppCompatActivity {
    ImageView iv_back, iv_capture;
    String QrData, upload_folder_name, qrcode_id, customer_id,Et_desc=" ";
    TextView tv_submit, tv_customer_name,tv_total_point;
    EditText et_amount;
    private static final int CAMERA_REQUEST = 1888;
    private File finalFile;
    MultipartBody.Part body;
    RadioGroup radioSexGroup;
    RadioButton radioSexButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_meter);

        iv_back = findViewById(R.id.iv_back);
        iv_capture = findViewById(R.id.iv_capture);
        tv_submit = findViewById(R.id.tv_submit);
        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_total_point = findViewById(R.id.tv_total_point);
        et_amount = findViewById(R.id.et_amount);
        //et_desc = findViewById(R.id.et_desc);
        radioSexGroup=(RadioGroup)findViewById(R.id.radioGroup);

        if (getIntent() != null) {
            QrData = getIntent().getStringExtra("Qr_data");

            if (Connectivity.isConnected(UploadMeterActivity.this)) {
                FindCustomerData(QrData);
            } else {
                Toast.makeText(UploadMeterActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
            }

        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });


        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //**************************************
                int selectedId=radioSexGroup.getCheckedRadioButtonId();
                radioSexButton=(RadioButton)findViewById(selectedId);
                Et_desc = radioSexButton.getText().toString();
                String Et_amount = et_amount.getText().toString();

                if (!QrData.isEmpty()) {
                    if (!Et_amount.isEmpty() ) {
                        if (Connectivity.isConnected(UploadMeterActivity.this)) {
                            UploadMeter(Et_amount, Et_desc);
                        } else {
                            Toast.makeText(UploadMeterActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(UploadMeterActivity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(UploadMeterActivity.this, "Qr code not found, Please scan again", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @SuppressLint("CheckResult")
    private void UploadMeter(String et_amount, String et_desc) {
        final ProgressDialog progressDialog = new ProgressDialog(UploadMeterActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        if (finalFile != null) {
            RequestBody fileBody;

            fileBody = RequestBody.create(MediaType.parse("image/*"), finalFile);
            body = MultipartBody.Part.createFormData("meter_reading_picture", finalFile.getName(), fileBody);
        }

        RequestBody EtAmount = RequestBody.create(MediaType.parse("text/plain"), et_amount);
        RequestBody EtDesc = RequestBody.create(MediaType.parse("text/plain"),et_desc );
        RequestBody CustomerId = RequestBody.create(MediaType.parse("text/plain"), customer_id);
        RequestBody QrCodeInfo = RequestBody.create(MediaType.parse("text/plain"), QrData);
        RequestBody FolderName = RequestBody.create(MediaType.parse("text/plain"), upload_folder_name);

        apiInterface.UploadMeter(EtAmount, EtDesc, CustomerId, QrCodeInfo, FolderName, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SuccessModel>() {
                    @Override
                    public void onNext(SuccessModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMessage());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getSuccess().equals(1)) {
                               // Toast.makeText(UploadMeterActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(UploadMeterActivity.this, ThanksActivity.class);
                                intent.putExtra("Message",response.getMessage());
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(UploadMeterActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        Log.e("mr_product_error", e.toString());
                        Toast.makeText(UploadMeterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        if (e instanceof HttpException) {
                            int code = ((HttpException) e).code();
                            switch (code) {
                                case 403:
                                    break;
                                case 404:
                                    //Toast.makeText(EmailSignupActivity.this, R.string.email_already_use, Toast.LENGTH_SHORT).show();
                                    break;
                                case 409:
                                    break;
                                default:
                                    // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            if (TextUtils.isEmpty(e.getMessage())) {
                                // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(EmailSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void FindCustomerData(String qrData) {
        final ProgressDialog progressDialog = new ProgressDialog(UploadMeterActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.FindCustomer(qrData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<FindCustomerModel>() {
                    @Override
                    public void onNext(FindCustomerModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMessage());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getSuccess().equals(1)) {
                                //  Log.e("result_my_test", "" + response.getDeliveryagentInfo().getDeliveryagentId());
                                tv_customer_name.setText("Customer name- " + response.getCustomerInfo().getCustomerName());
                                tv_total_point.setText("Total points- " + response.getCustomerInfo().getTotalPoints());
                                qrcode_id = response.getCustomerInfo().getQrcodeId();
                                upload_folder_name = response.getCustomerInfo().getUploadFolderName();
                                customer_id = response.getCustomerInfo().getCustomerId();

                            } else {
                                OpenErrorDialog(response.getMessage());
                                //Toast.makeText(UploadMeterActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        Log.e("mr_product_error", e.toString());
                        Toast.makeText(UploadMeterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        if (e instanceof HttpException) {
                            int code = ((HttpException) e).code();
                            switch (code) {
                                case 403:
                                    break;
                                case 404:
                                    //Toast.makeText(EmailSignupActivity.this, R.string.email_already_use, Toast.LENGTH_SHORT).show();
                                    break;
                                case 409:
                                    break;
                                default:
                                    // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            if (TextUtils.isEmpty(e.getMessage())) {
                                // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(EmailSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });
    }

    private void OpenErrorDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UploadMeterActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage(Html.fromHtml(message));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent intent = new Intent(UploadMeterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.putExtra("Qr_data",result.getText());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            iv_capture.setImageBitmap(photo);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            finalFile = new File(getRealPathFromURI(tempUri));

           // System.out.println(mImageCaptureUri);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


}