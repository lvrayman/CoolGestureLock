# CoolGestureLock
Usage:

在你工程的build.gradle中添加

```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

然后在你module的build.gradle中引用

```groovy
dependencies {
	        implementation 'com.github.lvrayman:CoolGestureLock:1.0.0'
}
```

在你的布局文件中使用

```xml
<com.rayman.coolgesturelock.GestureLock
        android:id="@+id/gesture_lock"
        android:layout_width="300dp"
        android:layout_height="300dp" />
```

在代码中进行结果监听