package io.git.zjoker.sample;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import io.git.zjoker.zcache.ZCache;

public class BitmapCacheActivity extends AppCompatActivity {
    private ImageView contentImg;
    private EditText durationED;
    private ImageView savedImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_cache);
        contentImg = findViewById(R.id.img_content);
        durationED = findViewById(R.id.ed_duration);
        savedImg = findViewById(R.id.img_saved);

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
        Bitmap bitmap = ((BitmapDrawable) contentImg.getDrawable()).getBitmap();

        String durationStr = durationED.getText().toString();
        int duration = !TextUtils.isEmpty(durationStr) ? Integer.parseInt(durationStr) : -1;
        ZCache.memory(this).putBitmap(Utils.MD5("Test_Bitmap"), bitmap, duration);
    }

    private void get() {
        Bitmap bitmap = ZCache.memory(this).getBitmap(Utils.MD5("Test_Bitmap"));
        savedImg.setImageBitmap(bitmap);
    }

    private void clear() {
        ZCache.memory(this).remove(Utils.MD5("Test_Bitmap"));
    }
}
