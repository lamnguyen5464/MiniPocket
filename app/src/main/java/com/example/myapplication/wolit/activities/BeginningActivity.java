package com.example.myapplication.wolit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.wolit.model.MyMoney;
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

        inputMoney.setText(MyMoney.getSharedValue(this).myMoney+"");


        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMoney tmp = MyMoney.getSharedValue(BeginningActivity.this);
                tmp.updateMoney(Double.parseDouble(inputMoney.getText().toString()), tmp.currencyType, BeginningActivity.this);
                startActivity(new Intent(BeginningActivity.this, MainActivity.class));
            }
        });

    }
}
