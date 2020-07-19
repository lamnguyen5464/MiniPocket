package com.example.myapplication.wolit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.databinding.ActivityNewTransferBinding;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.viewmodels.NewTransferViewModel;
public class NewTransferActivity extends AppCompatActivity {
    Switch switchSelect;
    Button btBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityNewTransferBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_new_transfer);
        switchSelect = findViewById(R.id.switch_select);
        btBack = findViewById(R.id.button_back);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        switchSelect.setTypeface(ResourcesCompat.getFont(this, R.font.chalkboard));
        binding.setNewTransfer(new NewTransferViewModel(this, getSupportFragmentManager()));

        (findViewById(R.id.btGetDateOnce)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((Button)findViewById(R.id.btGetDateOnce)).setText(getResources().getString(R.string.EMPTY_DATE));
                DateType.getRecentDate().reset();
                return true;
            }
        });
    }
    @BindingAdapter({"cursorPosition"})
    public static void setCursorPosition(EditText editText, String value){
        editText.setSelection(value.length());
    }
}
