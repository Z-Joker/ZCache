# ZCache
ZCache是一个**为Android制定**的 **高度可扩展**的 开源缓存框架。

## 1. 特点
- 1 ：存储对象可定制，可以定制存储任何能转换成byte数组东西如String, Bitmap和POJO对象等。
- 2 ：存储方式可定制，可以选择不同的方式进行定制，如内存，文件等。
- 3 ：基于**LRU算法**的缓存，适用于移动设备这种配置有限的设备，自动删除使用频率小的缓存。
- 4 ：可以配置全局的缓存路径，大小，也可以局部单独配置相对或绝对路径。
- 5 ：可以设置缓存超时时间，缓存超时自动失效，并被删除。
- 6 ：支持多存储路径多缓存实例，不同存储路径使用不同缓存实例互不干扰。

## 2. 可缓存对象
一切能转换成byte数组的东西：String、POJO对象、Bitmap、byte数组和Intent等。  
已提供String, 、POJO对象、Bitmap和JSONObject的存取实现。

## 3.添加依赖
```
//Add it in your root build.gradle at the end of repositories:
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

//Add the dependency
dependencies {
	        implementation 'com.github.Z-Joker:ZCache:v2.0.0'
	}
```

## 4. 基本使用

- 1：获取ZCache
```
    ZCache.memory(this);//获取内存缓存，路径=全局缓存根路径+全局缓存子文件、大小=全局内存缓存大小。
    ZCache.disk(this,"http",5 * 1024 * 1024);//获取本地缓存，路径=全局缓存根路径+http 对应实例缓存大小=5M。
    ZCache.twoLevel(MainActivity.this, new File(absolutePath));//获取内存+本地组成的2级缓存,路径为绝对路径。
```
- 2：存储
```
    ZCache.memory(this).putString("key","value");
    ZCache.memory(this).putString("key","value"，30*1000);//缓存30秒钟，超过30秒再去获取会返回null并且清空过期缓存。
```
- 3：获取
```
    ZCache.memory(this).getString("key","value");
```
## 5. 高级使用
### 全局的配置

```
  ZCacheConfig
            .instance()
            .setMaxDiskCacheSize(5 * 1024 * 1024) //内存缓存实例的大小,默认内存的1/8.
            .setMaxMemoryCacheSize(5 * 1024 * 1024)//本地缓存实例的大小,默认10M
            .setCacheRootDir(getCacheDir().getAbsolutePath())//缓存根路径，默认Context.getCacheDir()。
            .setCacheDir("http");//缓存子文件夹，默认zcache。
```
### 自定义可缓存对象
- 1：自定义转换器，实现IByteConverter接口，如String对象
```
  public class StringByteConverter implements IByteConverter<String> {
    @Override
    public byte[] obj2Bytes(String obj) {//String -> byte[]
        return obj.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public String bytes2Obj(byte[] bytes) {//byte[] -> String
        return new String(bytes, Charset.forName("UTF-8"));
    }
  }
    
  ZCache.memory(this).put("key","value", )
```
- 2：使用自定义转换器，2种方式
    
```
  //第1种：注册为全局的转换器
  ZCacheConfig
            .instance()
            .registerConverter(String.class, new StringByteConverter());
    
  ZCache.memory(this).put("key","value")//会自动从全局转换器中寻找对应类型的转换器

  //第2种：局部使用
  ZCache.memory(this).put("key","value",new StringByteConverter());
```


### 自定义存储方式
```
  //实现ICache接口，选择自己的存储方式。
  public interface ICache {
   
    <T> void put(String key, T obj, long duration, IByteConverter<T> converter);//缓存

    <T> void putWithDeadLine(String key, T obj, long deadLine, IByteConverter<T> converter);//缓存

    <T> T get(String key, IByteConverter<T> converter);//获取

    boolean isExpired(String key);//是否过期

    void remove(String key);//删除某个缓存
    
    void removeAll();//删除所有缓存

    boolean contains(String key);//是否存某个缓存

    long getDeadLine(String key);//获取过期时间
  }
```

更多示例请见Demo。

## 6. Change Log
### v(2.0.0)
- 基于ProtoStuff实现对POJO对象序列化/反序列化，替代Serializable。

## 7. Thanks
[ASimpleCache](https://github.com/yangfuhai/ASimpleCache)  
[TCache](https://github.com/borneywpf/TCache)

## 8. License
```
 Copyright 2019 Mr_Joker (zsimplest@gmail.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```
