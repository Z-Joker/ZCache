package io.git.zjoker.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import io.git.zjoker.zcache.ZCache;
import io.git.zjoker.zcache.helper.ICacheHelper;

public class CacheLevelActivity extends AppCompatActivity {
    private EditText contentED;
    private EditText durationED;
    private TextView savedTV;
    private Spinner cacheLevelSp;
    private ICacheHelper cacheHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_level);
        contentED = findViewById(R.id.ed_content);
        durationED = findViewById(R.id.ed_duration);
        savedTV = findViewById(R.id.tv_saved);
        cacheLevelSp = findViewById(R.id.sp_cache_level);


        cacheLevelSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cacheLevel = (String) cacheLevelSp.getAdapter().getItem(position);
                if (TextUtils.equals(cacheLevel, "内存缓存")) {
                    cacheHelper = ZCache.memory(CacheLevelActivity.this);
                } else if (TextUtils.equals(cacheLevel, "本地缓存")) {
                    cacheHelper = ZCache.disk(CacheLevelActivity.this);
                } else {
                    cacheHelper = ZCache.twoLevel(CacheLevelActivity.this);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get();
            }
        });
        cacheHelper = ZCache.memory(this);
    }

    private void save() {
        String content = contentED.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入要缓存的内容", Toast.LENGTH_LONG).show();
            return;
        }

        String durationStr = durationED.getText().toString();
        int duration = !TextUtils.isEmpty(durationStr) ? Integer.parseInt(durationStr) : -1;
        cacheHelper.putString(Utils.MD5("Test_Cache_Level"), content, duration);
    }

    private void get() {
        String content = cacheHelper.getString(Utils.MD5("Test_Cache_Level"));
        savedTV.setText(content);
    }
}
