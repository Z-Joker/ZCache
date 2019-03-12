package io.git.zjoker.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.charset.Charset;

import io.git.zjoker.zcache.ZCache;
import io.git.zjoker.zcache.converter.IByteConverter;

public class CustomConverterActivity extends AppCompatActivity {
    private EditText contentED;
    private TextView savedTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_cache);

        contentED = findViewById(R.id.ed_content);
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

        ZCache.memory(this).put(Utils.MD5("Test_String"), content, new CustomStringByteConverter());
    }

    private void get() {
        String content = ZCache.memory(this).get(Utils.MD5("Test_String"), new CustomStringByteConverter());
        savedTV.setText(content);
    }

    private void clear() {
        ZCache.memory(this).remove(Utils.MD5("Test_String"));
    }

    private static class CustomStringByteConverter implements IByteConverter<String> {
        @Override
        public byte[] obj2Bytes(String obj) {
            return obj.getBytes(Charset.forName("UTF-8"));
        }

        @Override
        public String bytes2Obj(byte[] bytes) {
            return new String(bytes, Charset.forName("UTF-8"));
        }
    }
}
