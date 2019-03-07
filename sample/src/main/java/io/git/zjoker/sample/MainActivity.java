package io.git.zjoker.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONObject;

import java.io.Serializable;

import io.git.zjoker.zcache.ZCache;

public class MainActivity extends AppCompatActivity {
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
//                try {
//                BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(MainActivity.this,R.mipmap.ic_launcher);
//                    jsonObject.put("lalala", "lalala"+i);
                    ZCache.twoLevel("http").putSerializable("lalala",new Test("lalala"+i));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                i++;
            }
        });

        findViewById(R.id.get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    String str = new String(ZCache.twoLevel("http").getBytes("lalala"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Bitmap bitmap = ZCache.twoLevel("http").getBitmap("lalala");
//                ((ImageView)findViewById(R.id.imageView)).setImageBitmap(bitmap);
//               String string = ZCache.twoLevel("http").getString("lalala");
                Test test = (Test) ZCache.twoLevel("http").getSerializable("lalala");
//                Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        });

//        ZCache.disk("http").putString("lalala", "lalala");
//        ZCache.twoLevel("http").putString("lalala", "lalala");
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
