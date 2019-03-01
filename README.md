# CoolGestureLock
## 使用

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
	        implementation 'com.github.lvrayman:CoolGestureLock:1.0.1'
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

```kotlin
		gestureLock.onFinishListener = object : GestureLock.OnGestureFinishListener {
            override fun onFinish(result: String) {
            }

            override fun onError() {
            }

        }
```

当返回结果不正确时，需要手动调用`setError()`

``` kotlin
gestureLock.setError()
```

返回结果由默认提供的加密工具进行md5加密，也可以使用自己的加密算法，只需要继承自IEncryptUtil，并设置即可

```kotlin
class MyEncryptUtil : IEncryptUtil {
    override fun encrypt(result: String): String {
        TODO("not implemented") 
    }
}

……

gestureLock.setEncryptUtil(MyEncryptUtil())
```

可在xml代码中通过属性rowCount设置每行点数，默认为3

```xml
    <com.rayman.coolgesturelock.GestureLock
        android:id="@+id/gesture_lock"
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:rowCount="4" />
```

也可在代码中动态进行设置

```kotlin
gestureLock.setRowCount(4)
```