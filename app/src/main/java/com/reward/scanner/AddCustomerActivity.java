package com.reward.scanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reward.scanner.api_client.Api_Call;
import com.reward.scanner.api_client.Base_Url;
import com.reward.scanner.api_client.RxApiClient;
import com.reward.scanner.databinding.ActivityAddCustomerBinding;
import com.reward.scanner.model.SuccessModel;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

public class AddCustomerActivity extends AppCompatActivity implements TextWatcher {
    ActivityAddCustomerBinding binding;
    ImageView iv_back;
    TextView tv_add;
    EditText EtQrData, EtDOB;
    String QrData, Qr_id;
    EditText et_customer_name, et_reward_card_no, et_state_name, et_dist_code, et_vehical_code, et_veh_no, et_email;
    int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_add_customer);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_customer);

        iv_back = findViewById(R.id.iv_back);
        tv_add = findViewById(R.id.tv_add_customer);
        EtQrData = findViewById(R.id.et_qr_data);
        EtDOB = findViewById(R.id.et_dob);
        et_email = findViewById(R.id.et_email);

        et_state_name = findViewById(R.id.et_state_name);
        et_dist_code = findViewById(R.id.et_dist_code);
        et_vehical_code = findViewById(R.id.et_vehical_code);
        et_veh_no = findViewById(R.id.et_veh_no);

        et_customer_name = findViewById(R.id.et_customer_name);
        //  et_mobile=findViewById(R.id.et_mobile);
        // et_vehical_no=findViewById(R.id.et_vehical_no);
        et_reward_card_no = findViewById(R.id.et_reward_card_no);

        binding.etMob1.addTextChangedListener(this);
        binding.etMob2.addTextChangedListener(this);
        binding.etMob3.addTextChangedListener(this);
        binding.etMob4.addTextChangedListener(this);
        binding.etMob5.addTextChangedListener(this);
        binding.etMob6.addTextChangedListener(this);
        binding.etMob7.addTextChangedListener(this);
        binding.etMob8.addTextChangedListener(this);
        binding.etMob9.addTextChangedListener(this);
        binding.etMob10.addTextChangedListener(this);


        if (getIntent() != null) {
            QrData = getIntent().getStringExtra("Qr_data");
            EtQrData.setText(QrData);

            if (Connectivity.isConnected(AddCustomerActivity.this)) {
                UploadQR(QrData);
            } else {
                Toast.makeText(AddCustomerActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
            }
        }


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        EtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Et_StateName = et_state_name.getText().toString();
                String Et_DistCode = et_dist_code.getText().toString();
                String Et_Vehi_Code = et_vehical_code.getText().toString();
                String Et_Vehi_No = et_veh_no.getText().toString();

                String Customer_vehical = Et_StateName + Et_DistCode + Et_Vehi_Code + Et_Vehi_No;

                String Et_Mob1 = binding.etMob1.getText().toString();
                String Et_Mob2 = binding.etMob2.getText().toString();
                String Et_Mob3 = binding.etMob3.getText().toString();
                String Et_Mob4 = binding.etMob4.getText().toString();
                String Et_Mob5 = binding.etMob5.getText().toString();
                String Et_Mob6 = binding.etMob6.getText().toString();
                String Et_Mob7 = binding.etMob7.getText().toString();
                String Et_Mob8 = binding.etMob8.getText().toString();
                String Et_Mob9 = binding.etMob9.getText().toString();
                String Et_Mob10 = binding.etMob10.getText().toString();

                String Customer_mobile = Et_Mob1+Et_Mob2+Et_Mob3+Et_Mob4+Et_Mob5+Et_Mob6+Et_Mob7+Et_Mob8+Et_Mob9+Et_Mob10;

                String Et_Email = et_email.getText().toString();
                String Customer_name = et_customer_name.getText().toString();
                String Customer_reward_card = et_reward_card_no.getText().toString();
                String Customer_dob = EtDOB.getText().toString();

                if (!Customer_name.isEmpty() && !Customer_mobile.isEmpty() &&
                        !Customer_vehical.isEmpty() && !Customer_reward_card.isEmpty() && !Customer_dob.isEmpty() && !Et_Email.isEmpty()) {
                    if (QrData != null && !QrData.isEmpty()) {
                        if (Customer_mobile.length() == 10) {
                            if (android.util.Patterns.EMAIL_ADDRESS.matcher(Et_Email).matches()){
                                if (Customer_vehical.length() != 10) {
                                    Toast.makeText(AddCustomerActivity.this, "Invalid vehical number", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (Customer_reward_card.length()!=10){
                                        Toast.makeText(AddCustomerActivity.this, "Invalid XTRAREWARDS Card No.", Toast.LENGTH_SHORT).show();
                                    }else {
                                        if (Connectivity.isConnected(AddCustomerActivity.this)) {
                                            AddCustomer(Customer_name, Customer_mobile, Customer_vehical, Customer_reward_card, Customer_dob, Et_Email);
                                        } else {
                                            Toast.makeText(AddCustomerActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                            }else {
                                Toast.makeText(AddCustomerActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(AddCustomerActivity.this, "Please enter valid mobile no.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(AddCustomerActivity.this, "Qr Data id not found, Please scan QR again", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AddCustomerActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void DateDialog() {
        Calendar mcalendar = Calendar.getInstance();

        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (dayOfMonth < 10) {
                    EtDOB.setText("0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                } else {
                    EtDOB.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                }
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(AddCustomerActivity.this, listener, year, month, day);
        dpDialog.show();

    }

    @SuppressLint("CheckResult")
    private void UploadQR(String qr_text) {
        final ProgressDialog progressDialog = new ProgressDialog(AddCustomerActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.SaveQrCode(qr_text)
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
                                //  Log.e("result_my_test", "" + response.getDeliveryagentInfo().getDeliveryagentId());
                                Qr_id = String.valueOf(response.getQrcode_id());

                            } else {
                                OpenErrorDialog(response.getMessage());
                                // Toast.makeText(AddCustomerActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AddCustomerActivity.this, "network failure", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddCustomerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddCustomerActivity.this);
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

    @SuppressLint("CheckResult")
    private void AddCustomer(String customer_name, String customer_mobile, String customer_vehical, String customer_reward_card,
                             String customer_dob, String et_Email) {
        final ProgressDialog progressDialog = new ProgressDialog(AddCustomerActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.AddNewCustomer(customer_name, customer_mobile, customer_vehical, customer_reward_card, QrData, customer_dob, et_Email)
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
                                //  Log.e("result_my_test", "" + response.getDeliveryagentInfo().getDeliveryagentId());
                                // Toast.makeText(AddCustomerActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddCustomerActivity.this, ThanksActivity.class);
                                intent.putExtra("Message", response.getMessage());
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AddCustomerActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        Intent intent = new Intent(AddCustomerActivity.this, AddcustomerQrScan.class);
        // intent.putExtra("Qr_data",result.getText());
        startActivity(intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        {
            if (editable.length() == 1) {
                if (binding.etMob1.length() == 1) {
                    binding.etMob2.requestFocus();
                }

                if (binding.etMob2.length() == 1) {
                    binding.etMob3.requestFocus();
                }
                if (binding.etMob3.length() == 1) {
                    binding.etMob4.requestFocus();
                }
                if (binding.etMob4.length() == 1) {
                    binding.etMob5.requestFocus();
                }
                if (binding.etMob5.length() == 1) {
                    binding.etMob6.requestFocus();
                }
                if (binding.etMob6.length() == 1) {
                    binding.etMob7.requestFocus();
                }
                if (binding.etMob7.length() == 1) {
                    binding.etMob8.requestFocus();
                }
                if (binding.etMob8.length() == 1) {
                    binding.etMob9.requestFocus();
                }
                if (binding.etMob9.length() == 1) {
                    binding.etMob10.requestFocus();
                }

            } else if (editable.length() == 0) {
                if (binding.etMob10.length() == 0) {
                    binding.etMob9.requestFocus();
                }
                if (binding.etMob9.length() == 0) {
                    binding.etMob8.requestFocus();
                }
                if (binding.etMob8.length() == 0) {
                    binding.etMob7.requestFocus();
                }

                if (binding.etMob7.length() == 0) {
                    binding.etMob6.requestFocus();
                }
                if (binding.etMob6.length() == 0) {
                    binding.etMob5.requestFocus();
                }
                if (binding.etMob5.length() == 0) {
                    binding.etMob4.requestFocus();
                }
                if (binding.etMob4.length() == 0) {
                    binding.etMob3.requestFocus();
                }
                if (binding.etMob3.length() == 0) {
                    binding.etMob2.requestFocus();
                }
                if (binding.etMob2.length() == 0) {
                    binding.etMob1.requestFocus();
                }


            }
        }
    }
}