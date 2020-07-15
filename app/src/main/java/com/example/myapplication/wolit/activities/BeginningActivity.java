package com.example.myapplication.wolit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.wolit.model.CurrentStatus;
import com.example.myapplication.wolit.R;

public class BeginningActivity extends Activity {
    EditText inputMoney;
    Button btConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);

        inputMoney = findViewById(R.id.inputMoney);
        btConfirm = findViewById(R.id.btConfirm);

        inputMoney.setText(CurrentStatus.initSharedValue(this).getMyMoney()+"");


        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentStatus tmp = CurrentStatus.initSharedValue(BeginningActivity.this);
                tmp.updateMoney(Double.parseDouble(inputMoney.getText().toString()), tmp.getCurrencyType());
                startActivity(new Intent(BeginningActivity.this, MainActivity.class));
            }
        });

    }
}
