package io.git.zjoker.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.git.zjoker.zcache.ZCache;

public class StringCacheActivity extends AppCompatActivity {
    private EditText contentED;
    private EditText durationED;
    private TextView savedTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_cache);

        contentED = findViewById(R.id.ed_content);
        durationED = findViewById(R.id.ed_duration);
        savedTV = findViewById(R.id.tv_saved);

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get();
            }
        });
    }

    private void save() {
        String content = contentED.getText().toString();

        String durationStr = durationED.getText().toString();
        int duration = !TextUtils.isEmpty(durationStr) ? Integer.parseInt(durationStr) : -1;
        ZCache.memory(this).putString(Utils.MD5("Test_String"), content, duration);
    }

    private void get() {
        String content = ZCache.memory(this).getString(Utils.MD5("Test_String"));
        savedTV.setText(content);
    }

    private void clear() {
        ZCache.memory(this).remove(Utils.MD5("Test_String"));
    }
}
