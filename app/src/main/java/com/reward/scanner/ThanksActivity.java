package com.reward.scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reward.scanner.api_client.Api_Call;
import com.reward.scanner.api_client.Base_Url;
import com.reward.scanner.api_client.RxApiClient;
import com.reward.scanner.model.SuccessModel;
import com.reward.scanner.model.Terms_Condition_Model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

public class ThanksActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_add,tv_thanks;
    String Message;
    RecyclerView rv_terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);

        iv_back=findViewById(R.id.iv_back);
        tv_add=findViewById(R.id.tv_add);
        tv_thanks=findViewById(R.id.tv_thanks);
        rv_terms=findViewById(R.id.rv_terms);

        if (getIntent()!=null){
            Message=getIntent().getStringExtra("Message");
            tv_thanks.setText(Message);
        }

        if (Connectivity.isConnected(ThanksActivity.this)) {
            GetT_C();
        } else {
            Toast.makeText(ThanksActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void GetT_C() {
        final ProgressDialog progressDialog = new ProgressDialog(ThanksActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.Get_TermsCond()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Terms_Condition_Model>() {
                    @Override
                    public void onNext(Terms_Condition_Model response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMessage());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getSuccess().equals(1)) {
                                //  Log.e("result_my_test", "" + response.getDeliveryagentInfo().getDeliveryagentId());
                                TermsAdapter  newsAdapter = new TermsAdapter(response.getTerms(), ThanksActivity.this);
                                rv_terms.setLayoutManager(new LinearLayoutManager(ThanksActivity.this, RecyclerView.VERTICAL, false));
                                rv_terms.setItemAnimator(new DefaultItemAnimator());
                                rv_terms.setAdapter(newsAdapter);
                               // newsAdapter.notifyDataSetChanged();

                            } else {

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
                              //  Toast.makeText(ThanksActivity.this, "network failure", Toast.LENGTH_SHORT).show();
                            } else {
                              //  Toast.makeText(ThanksActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        //super.onBackPressed();

        Intent intent =new Intent(ThanksActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}