package com.example.btremote.tools

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.view.View.*
import android.view.Window
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.btremote.app.App
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.*


/**
 * LogUtil 日志打印工具类
 * @Author cy
 * @Date 2021-3-16
 * @Version 3.0.0
 */
internal object LogUtil {
    var status = true

    fun log(title: String, content: String = "") {
        if (status) {
            if (content != "")
                Log.i("SerialPort", "$title  -->  $content")
            else
                Log.i("SerialPort", title)
        }
    }
}

/**
 * ToastUtil 提示信息工具类
 * @Author cy
 * @Date 2021-7-21
 * @Version 4.0.0
 */
internal object ToastUtil {
    fun toast(context: Context, content: String) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }
}


object SaveDataToLocalFile {
    fun save(context: Context, file: String, data: String) {
        var writer: BufferedWriter? = null
        try {
            val out = context.openFileOutput(file, Context.MODE_PRIVATE)
            writer = BufferedWriter(OutputStreamWriter(out))
            writer.write(data)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun load(context: Context, file: String): String? {
        var reader: BufferedReader? = null
        val content = StringBuilder()
        try {
            val input: FileInputStream
            try {
                input = context.openFileInput(file)
            } catch (e: FileNotFoundException) {
                return null
            }
            reader = BufferedReader(InputStreamReader(input))
            var line: String? = ""
            while (reader.readLine().also { line = it } != null) {
                content.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return content.toString()
    }
}

/**
 * 获取当前APK的版本号和版本名
 */
object APKVersionInfoUtils {
    /**
     * 获取当前apk的版本号
     *
     * @param mContext
     * @return
     */
    fun getVersionCode(mContext: Context): Int {
        var versionCode = 0
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode =
                mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    /**
     * 获取当前apk的版本名
     *
     * @param context 上下文
     * @return
     */
    fun getVersionName(context: Context): String {
        var versionName = ""
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionName
            versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }
}

object WindowManager {
    lateinit var window: Window
    fun setStatusBar(visible: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (visible) {
                controller?.show(WindowInsetsCompat.Type.statusBars())
            } else {
                controller?.hide(WindowInsetsCompat.Type.statusBars())
            }
        } else {
            if (visible) {
                window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                window.statusBarColor = 0xffffff
            } else {
                window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

    }

    fun setNavigationBar(visible: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (visible) {
                controller?.show(WindowInsetsCompat.Type.navigationBars())
            } else {
                controller?.hide(WindowInsetsCompat.Type.navigationBars())
            }
        } else {
            val decorView: View = window.decorView
            if (!visible) {
                val option = SYSTEM_UI_FLAG_HIDE_NAVIGATION
                decorView.systemUiVisibility = option
            } else {
                val option = SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                decorView.systemUiVisibility = option
            }
        }
    }

    fun setFullScreen(boolean: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (!boolean) {
                controller?.show(WindowInsetsCompat.Type.systemBars())
            } else {
                controller?.hide(WindowInsetsCompat.Type.systemBars())
            }
        } else {
            if (!boolean) {
                val decorView: View = window.decorView
                decorView.systemUiVisibility = (
                        SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                        )
            } else {
                val decorView: View = window.decorView
                decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or SYSTEM_UI_FLAG_FULLSCREEN
                        or SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
        }
    }

}


object EasyDataStore {
    val Context.dataStore1: DataStore<Preferences> by preferencesDataStore(name = "ViewPos")

    // DataStore变量
    var dataStore = App.appContext.dataStore1

    /**
     * 存数据
     */
    suspend fun <T> putAsyncData(key: String, value: T) {
        when (value) {
            is Int -> putIntData(key, value)
            is Long -> putLongData(key, value)
            is String -> putStringData(key, value)
            is Boolean -> putBooleanData(key, value)
            is Float -> putFloatData(key, value)
            is Double -> putDoubleData(key, value)
            else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
        }

    }

    /**
     * 存数据
     */
    fun <T> putSyncData(key: String, value: T) {
        runBlocking {
            when (value) {
                is Int -> putIntData(key, value)
                is Long -> putLongData(key, value)
                is String -> putStringData(key, value)
                is Boolean -> putBooleanData(key, value)
                is Float -> putFloatData(key, value)
                is Double -> putDoubleData(key, value)
                else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
            }
        }
    }

    /**
     * 取数据
     */
    fun <T> getSyncData(key: String, defaultValue: T): T {
        var data: T? = null
        runBlocking {
            data = when (defaultValue) {
                is Int -> getIntData(key, defaultValue) as T
                is Long -> getLongData(key, defaultValue) as T
                is String -> getStringData(key, defaultValue) as T
                is Boolean -> getBooleanData(key, defaultValue) as T
                is Float -> getFloatData(key, defaultValue) as T
                is Double -> getDoubleData(key, defaultValue) as T
                else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
            }
        }
        return data ?: defaultValue
    }

    /**
     * 取数据
     */
    suspend fun <T> getAsyncData(key: String, defaultValue: T): T {
        val data = when (defaultValue) {
            is Int -> getIntData(key, defaultValue)
            is Long -> getLongData(key, defaultValue)
            is String -> getStringData(key, defaultValue)
            is Boolean -> getBooleanData(key, defaultValue)
            is Float -> getFloatData(key, defaultValue)
            is Double -> getDoubleData(key, defaultValue)
            else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
        }
        return data as T
    }

    /**
     * 存放Int数据
     */
    private suspend fun putIntData(key: String, value: Int) = dataStore.edit {
        it[intPreferencesKey(key)] = value
    }

    /**
     * 存放Long数据
     */
    private suspend fun putLongData(key: String, value: Long) = dataStore.edit {
        it[longPreferencesKey(key)] = value
    }

    /**
     * 存放String数据
     */
    private suspend fun putStringData(key: String, value: String) = dataStore.edit {
        it[stringPreferencesKey(key)] = value
    }

    /**
     * 存放Boolean数据
     */
    private suspend fun putBooleanData(key: String, value: Boolean) = dataStore.edit {
        it[booleanPreferencesKey(key)] = value
    }

    /**
     * 存放Float数据
     */
    private suspend fun putFloatData(key: String, value: Float) = dataStore.edit {
        it[floatPreferencesKey(key)] = value
    }

    /**
     * 存放Double数据
     */
    private suspend fun putDoubleData(key: String, value: Double) = dataStore.edit {
        it[doublePreferencesKey(key)] = value
    }

    /**
     * 取出Int数据
     */
    private suspend fun getIntData(key: String, default: Int = 0): Int = dataStore.data.map {
        it[intPreferencesKey(key)] ?: default
    }.first()

    /**
     * 取出Long数据
     */
    private suspend fun getLongData(key: String, default: Long = 0): Long = dataStore.data.map {
        it[longPreferencesKey(key)] ?: default
    }.first()


    /**
     * 取出String数据
     */
    private suspend fun getStringData(key: String, default: String? = null): String =
        dataStore.data.map {
            it[stringPreferencesKey(key)] ?: default
        }.first()!!

    /**
     * 取出Boolean数据
     */
    private suspend fun getBooleanData(key: String, default: Boolean = false): Boolean =
        dataStore.data.map {
            it[booleanPreferencesKey(key)] ?: default
        }.first()

    /**
     * 取出Float数据
     */
    private suspend fun getFloatData(key: String, default: Float = 0.0f): Float =
        dataStore.data.map {
            it[floatPreferencesKey(key)] ?: default
        }.first()

    /**
     * 取出Double数据
     */
    private suspend fun getDoubleData(key: String, default: Double = 0.00): Double =
        dataStore.data.map {
            it[doublePreferencesKey(key)] ?: default
        }.first()

}

