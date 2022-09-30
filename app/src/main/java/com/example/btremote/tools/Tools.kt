package com.example.btremote.tools

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.view.View.*
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.*
import java.io.*
import java.nio.charset.Charset


/**
 * LogUtil 日志打印工具类
 * @Author cy
 * @Date 2021-3-16
 * @Version 3.0.0
 */
internal object LogUtil {
    var status = false

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

@SuppressLint("QueryPermissionsNeeded")
fun openMail(context: Context, email:String) {
    val uri: Uri = Uri.parse("mailto:$email")
    val packageInfo: List<ResolveInfo> =
        context.packageManager.queryIntentActivities(Intent(Intent.ACTION_SENDTO, uri), 0)
    val tempPkgNameList: MutableList<String> = ArrayList()
    val emailIntents: MutableList<Intent> = ArrayList()
    for (info in packageInfo) {
        val pkgName = info.activityInfo.packageName
        if (!tempPkgNameList.contains(pkgName)) {
            tempPkgNameList.add(pkgName)
            val intent: Intent? = context.packageManager.getLaunchIntentForPackage(pkgName)
            if (intent != null) {
                emailIntents.add(intent)
            }
        }
    }
    if (emailIntents.isNotEmpty()) {
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra(Intent.EXTRA_CC, email) // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分") // 主题
        intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分") // 正文
        val chooserIntent =
            Intent.createChooser(intent, "请选择邮箱")
        if (chooserIntent != null) {
            context.startActivity(chooserIntent)
        } else {
            ToastUtil.toast(context,"当前系统中没有邮箱应用")
        }
    } else {
        ToastUtil.toast(context,"当前系统中没有邮箱应用")
    }
}

fun addQQ(context: Context,qq:String)
{
    try {
        val url = "mqqwpa://im/chat?chat_type=wpa&uin=$qq"
        //uin是发送过去的qq号码
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (e:Exception) {
        ToastUtil.toast(context,"请先安装qq")
    }
}



fun readAssetsFile(context: Context, fileName: String?): String? {
    try {
        val `is` = context.assets.open(fileName!!)
        val fileLength = `is`.available()
        val buffer = ByteArray(fileLength)
        val readLength = `is`.read(buffer)
        `is`.close()
        return String(buffer, Charset.forName("utf-8"))
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}
