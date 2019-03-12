package io.git.zjoker.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_string_cache:
                startActivity(new Intent(this, StringCacheActivity.class));
                break;
            case R.id.btn_bitmap_cache:
                startActivity(new Intent(this, BitmapCacheActivity.class));
                break;
            case R.id.btn_ser_cache:
                startActivity(new Intent(this, SerializableCacheActivity.class));
                break;
            case R.id.btn_cache_level:
                startActivity(new Intent(this, CacheLevelActivity.class));
                break;
            case R.id.btn_multiply_cache_path:
                startActivity(new Intent(this, MultiplyCachePathActivity.class));
                break;
            case R.id.btn_custom_converter:
                startActivity(new Intent(this, CustomConverterActivity.class));
                break;
        }
    }

    public static class Test implements Serializable {
        String name;

        public Test(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Test{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
