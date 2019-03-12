package io.git.zjoker.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import io.git.zjoker.zcache.ZCache;

public class MultiplyCachePathActivity extends AppCompatActivity {
    private EditText contentED1;
    private EditText contentED2;
    private EditText contentED3;
    private TextView savedTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_cache);

        contentED1 = findViewById(R.id.ed_content1);
        contentED2 = findViewById(R.id.ed_content2);
        contentED3 = findViewById(R.id.ed_content3);

        savedTV = findViewById(R.id.tv_saved);

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
    }

    private void save() {
        String content1 = contentED1.getText().toString();
        String content2 = contentED2.getText().toString();
        String content3 = contentED3.getText().toString();

        ZCache.disk(this).putString(Utils.MD5("Test_Multiply"), content1);
        ZCache.disk(this, "http").putString(Utils.MD5("Test_Multiply"), content2);
        ZCache.disk(this, new File(Utils.getDiskCacheDir(this), "test")).putString(Utils.MD5("Test_Multiply"), content3);
    }

    private void get() {
        String content1 = ZCache.disk(this).getString(Utils.MD5("Test_Multiply"));
        String content2 = ZCache.disk(this, "http").getString(Utils.MD5("Test_Multiply"));
        String content3 = ZCache.disk(this, new File(Utils.getDiskCacheDir(this), "test")).getString(Utils.MD5("Test_Multiply"));
        String allContent = String.format("key=Test_Multiply\n缓存路径1：%s\n缓存路径2：%s\n缓存路径3：%s", content1, content2, content3);
        savedTV.setText(allContent);
    }
}
