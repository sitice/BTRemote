package com.example.btremote.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.media.AudioAttributes
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.PowerManager
import android.os.Vibrator
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Display
import com.example.btremote.BuildConfig.DEBUG
import java.lang.reflect.Field
import java.lang.reflect.Method


class AppUtils {
    /*
     * 获取MEID
     * 注：调用前需要获取READ_PHONE_STATE权限
     * */
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getMEID(context: Context): String {
        var meid = ""
        val mTelephonyMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (null != mTelephonyMgr) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                meid = mTelephonyMgr.meid
                Log.i(TAG, "Android版本大于o-26-优化后的获取---meid:$meid")
            } else {
                meid = mTelephonyMgr.deviceId
            }
        }
        Log.i(TAG, "优化后的获取---meid:$meid")
        return meid
    }

    /**
     * 获取IMEI
     * 注：调用前需要获取READ_PHONE_STATE权限
     *
     * @param context Context
     * @param index   取第几个imei(0,1)
     * @return
     */
    @SuppressLint("MissingPermission")
    fun getIMEI(context: Context, index: Int): String? {
        var imei: String? = ""
        val mTelephonyMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (null != mTelephonyMgr) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei = mTelephonyMgr.getImei(index)
                Log.i(TAG, "Android版本大于o-26-优化后的获取---imei-:$imei")
            } else {
                try {
                    imei = getDoubleImei(mTelephonyMgr, "getDeviceIdGemini", index)
                } catch (e: Exception) {
                    try {
                        imei = getDoubleImei(mTelephonyMgr, "getDeviceId", index)
                    } catch (e1: Exception) {
                        e1.printStackTrace()
                    }
                    Log.e(TAG, "get device id fail: $e")
                }
            }
        }
        Log.i(TAG, "优化后的获取---imei：$imei")
        return imei
    }

    /**
     * 获取双卡手机的imei
     */
    @Throws(Exception::class)
    private fun getDoubleImei(
        telephony: TelephonyManager,
        predictedMethodName: String,
        slotID: Int
    ): String? {
        var inumeric: String? = null
        val telephonyClass = Class.forName(telephony.javaClass.name)
        val parameter = arrayOfNulls<Class<*>?>(1)
        parameter[0] = Int::class.javaPrimitiveType
        val getSimID: Method = telephonyClass.getMethod(predictedMethodName, *parameter)
        val obParameter = arrayOfNulls<Any>(1)
        obParameter[0] = slotID
        val ob_phone: Any = getSimID.invoke(telephony, obParameter)
        if (ob_phone != null) {
            inumeric = ob_phone.toString()
        }
        return inumeric
    }//手机品牌//        TelephonyManager manager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String mtype = android.os.Build.MODEL;
    /**
     * 获取品牌
     */
    val phoneBrand: String
        get() =//        TelephonyManager manager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //        String mtype = android.os.Build.MODEL;
            Build.BRAND//手机型号

    /**
     * 获取型号
     */
    val phoneMODEL: String
        get() = Build.MODEL

//    /**
//     * 获取手机分辨率
//     *
//     * @param context
//     * @return
//     */
//    fun getResolution(context: Context): String {
//        // 方法1 Android获得屏幕的宽和高
//        val windowManager: WindowManager = (context as Activity).windowManager
//        val display: Display = windowManager.getDefaultDisplay()
//        val screenWidth: Int = display.getWidth()
//        val screenHeight: Int = display.getHeight()
//        Log.w(TAG, "分辨率：$screenWidth*$screenHeight")
//        return "$screenWidth*$screenHeight"
//    }

    /**
     * 获取运营商
     *
     * @param context
     * @return
     */
    fun getNetOperator(context: Context): String {
        val manager = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val iNumeric = manager.simOperator
        var netOperator = ""
        if (iNumeric.length > 0) {
            netOperator = if (iNumeric == "46000" || iNumeric == "46002") {
                // 中国移动
                "中国移动"
            } else if (iNumeric == "46003") {
                // 中国电信
                "中国电信"
            } else if (iNumeric == "46001") {
                // 中国联通
                "中国联通"
            } else {
                //未知
                "未知"
            }
        }
        Log.w(TAG, "运营商：$netOperator")
        return netOperator
    }

    /**
     * 获取联网方式
     */
    fun getNetMode(context: Context): String {
        var strNetworkType = "未知"
        //        TelephonyManager manager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        manager.getNetworkType();
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            val netMode = networkInfo.type
            if (netMode == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI"
                //wifi
            } else if (netMode == ConnectivityManager.TYPE_MOBILE) {
                val networkType = networkInfo.subtype
                strNetworkType =
                    when (networkType) {
                        TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> "2G"
                        TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"
                        TelephonyManager.NETWORK_TYPE_LTE -> "4G"
                        else -> {
                            val _strSubTypeName = networkInfo.subtypeName
                            // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                            if (_strSubTypeName.equals(
                                    "TD-SCDMA",
                                    ignoreCase = true
                                ) || _strSubTypeName.equals(
                                    "WCDMA",
                                    ignoreCase = true
                                ) || _strSubTypeName.equals("CDMA2000", ignoreCase = true)
                            ) {
                                "3G"
                            } else {
                                _strSubTypeName
                            }
                        }
                    }
            }
        }
        Log.w(TAG, "联网方式:$strNetworkType")
        return strNetworkType
    }

    /**
     * 获取操作系统
     *
     * @return
     */
    val oS: String
        get() {
            Log.w(TAG, "操作系统:" + "Android" + Build.VERSION.RELEASE)
            return "Android" + Build.VERSION.RELEASE
        }

    /**
     * 获取wifi当前ip地址
     *
     * @param context
     * @return
     */
    fun getLocalIpAddress(context: Context): String {
        return try {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val i = wifiInfo.ipAddress
            int2ip(i)
        } catch (ex: Exception) {
            """ 请保证是WIFI,或者请重新打开网络!
${ex.message}"""
        }
    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    fun int2ip(ipInt: Int): String {
        val sb = StringBuilder()
        sb.append(ipInt and 0xFF).append(".")
        sb.append(ipInt shr 8 and 0xFF).append(".")
        sb.append(ipInt shr 16 and 0xFF).append(".")
        sb.append(ipInt shr 24 and 0xFF)
        return sb.toString()
    }

    /**
     * 获取蓝牙MAC地址
     *
     * @return
     */
    val btAddressByReflection: String?
        get() {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            var field: Field? = null
            try {
                field = BluetoothAdapter::class.java.getDeclaredField("mService")
                field.isAccessible = true
                val bluetoothManagerService: Any = field.get(bluetoothAdapter) ?: return null
                val method: Method? = bluetoothManagerService.javaClass.getMethod("getAddress")
                if (method != null) {
                    val obj: Any = method.invoke(bluetoothManagerService)
                    if (obj != null) {
                        return obj.toString()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

    /**
     * 获取android序列号SN
     * 8675 8604 3504 498
     *
     * @return id或者空串
     */
    @get:Synchronized
    val serialNumber: String
        get() {
            var serialNumber: String? = null
            try {
                val clazz = Class.forName("android.os.SystemProperties")
                val method_get: Method? =
                    clazz.getMethod("get", String::class.java, String::class.java)
                if (method_get != null) {
                    serialNumber = method_get.invoke(clazz, "ro.serialno", "") as String?
                }
            } catch (e: Exception) {
                if (DEBUG) {
                    e.printStackTrace()
                }
            }
            return serialNumber ?: ""
        }

    /*
    * 获取手机号(基于sim卡是否有写入，未写入则返回空)
    * 注：调用前需要获取READ_PHONE_STATE权限！！！
    * @return 手机号
    *
    *  //截取+86
           if (phone.startsWith("+86")) {
               phone = phone.substring(3, phone.length());
           }
    *
    *
    * */
    @SuppressLint("MissingPermission")
    fun getPhone(context: Context): String {
        var tel = ""
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tel = tm.line1Number
            return tel
        } catch (e: Exception) {
            tel = ""
        }
        return tel
    }

    /**
     * 震动
     *
     * @param context
     * @param vibrationPattern 第二参数表示从哪里开始循环，比如这里的0表示这个数组在第一次循环完之后会从下标0开始循环到最后，这里的如果是-1表示不循环。
     */
    fun vibrator(context: Context, vibrationPattern: LongArray?) {
        //获取系统的Vibrator服务
        val vibrator = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(vibrationPattern, -1)
    }

    /**
     * 震动 适配6.0以上
     *
     * @param context
     * @param pattern 第二参数表示从哪里开始循环，比如这里的0表示这个数组在第一次循环完之后会从下标0开始循环到最后，这里的如果是-1表示不循环。
     */
    fun vibratorForLollipop(context: Context, pattern: LongArray?) {
        val mVibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        var audioAttributes: AudioAttributes? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM) //key
                .build()
            mVibrator.vibrate(pattern, -1, audioAttributes)
        } else {
            mVibrator.vibrate(pattern, -1)
        }
    }

    companion object {
        private const val TAG = "mlink"
        private val `object` = Any()
        private var appUtils: AppUtils? = null
        val instance: AppUtils?
            get() {
                if (appUtils == null) {
                    synchronized(`object`) {
                        if (appUtils == null) {
                            appUtils = AppUtils()
                        }
                    }
                }
                return appUtils
            }

        /**
         * 唤醒屏幕
         *
         * @param context
         */
        @SuppressLint("InvalidWakeLockTag")
        fun wakeUpAndUnlock(context: Context) {
            //屏锁管理器
            val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            val kl = km.newKeyguardLock("unLock")
            //解锁
            kl.disableKeyguard()
            //获取电源管理器对象
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            val wl = pm.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP or
                        PowerManager.SCREEN_DIM_WAKE_LOCK, "bright"
            )
            //点亮屏幕
            wl.acquire()
            //释放
            wl.release()
        }
    }
}