package com.hybunion.yirongma.payment.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.autonavi.bigwasp.sdk.BWBaseActivity;
import com.hybunion.yirongma.R;

public class GaodeGuideActivity extends BWBaseActivity {

    ImageView imageVeiw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaode_guide);
        imageVeiw = findViewById(R.id.imageVeiw);
        imageVeiw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GaodeGuideActivity.this.startView(null);
                }catch (Exception e){
                    GaodeGuideActivity.this.startView(null);
                }
            }
        });
    }
}
