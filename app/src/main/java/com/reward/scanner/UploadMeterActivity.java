package com.reward.scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UploadMeterActivity extends AppCompatActivity {
    ImageView iv_back;
    private String QrData;
    TextView tv_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_meter);

        iv_back=findViewById(R.id.iv_back);
        tv_submit=findViewById(R.id.tv_submit);

        if (getIntent()!=null){
           // QrData=getIntent().getStringExtra("Qr_data");

        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(UploadMeterActivity.this,ThanksActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}