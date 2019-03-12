package io.git.zjoker.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

import io.git.zjoker.zcache.ZCache;

public class SerializableCacheActivity extends AppCompatActivity {
    private EditText userIdED;
    private EditText userNameED;
    private EditText userAgeED;
    private EditText durationED;
    private TextView savedTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serializable_cache);

        userIdED = findViewById(R.id.ed_user_id);
        userNameED = findViewById(R.id.ed_user_name);
        userAgeED = findViewById(R.id.ed_age);
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
        String userId = userIdED.getText().toString();
        String userName = userNameED.getText().toString();
        String ageStr = userAgeED.getText().toString();
        int age = !TextUtils.isEmpty(ageStr) ? Integer.parseInt(ageStr) : 0;

        String durationStr = durationED.getText().toString();
        int duration = !TextUtils.isEmpty(durationStr) ? Integer.parseInt(durationStr) : -1;
        ZCache.memory(this).putSerializable(Utils.MD5("Test_Serializable"), new User(userId, userName, age), duration);
    }

    private void get() {
        User user = (User) ZCache.memory(this).getSerializable(Utils.MD5("Test_Serializable"));
        savedTV.setText(String.valueOf(user));
    }

    private void clear() {
        ZCache.memory(this).remove(Utils.MD5("Test_Serializable"));
    }

    public static class User implements Serializable {
        public String userId;
        public String userName;
        public int age;

        public User(String userId, String userName, int age) {
            this.userId = userId;
            this.userName = userName;
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "userId='" + userId + '\'' +
                    ", userName='" + userName + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
