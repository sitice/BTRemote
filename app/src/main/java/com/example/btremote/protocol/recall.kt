package com.example.btremote.protocol

//震动
var attitudeReceiver: (i: Short) -> Unit = {}

//车辆数据
var chariotDataReceiver: (x: Float, y: Float, battery: Float, angle: Float) -> Unit =
    { _: Float, _: Float, _: Float, _: Float -> }

//芯片id
var idReceiver: (id: Short) -> Unit = {}

//轮子类型
var wheelReceiver: (i: Short) -> Unit = {}

//解锁状态
var lockStateReceiver: (i: Boolean) -> Unit = {}

//版本
var versionReceiver: (i: Short) -> Unit = {}

//电量
var voltageReceiver: (i: Short) -> Unit = {}

//路径添加成功
var addSuccessReceiver: (i: Short) -> Unit = {}

//错误提示
var errorReceiver: (i: Short) -> Unit = {}

//行驶完毕
var finishedDrivingReceiver: (i: Short) -> Unit = {}

//加速度
var accDataReceiver: (accX: Short, accY: Short, accZ: Short) -> Unit =
    { _: Short, _: Short, _: Short -> }

//欧拉角yaw
var eulerPointReceiver: (yaw: Short) -> Unit = {}

//陀螺仪数据
var gyroDataReceiver: (gyroX: Short, gyroY: Short, gyroZ: Short) -> Unit =
    { _: Short, _: Short, _: Short -> }

//正在运行的路径
var nowRunWayReceiver: (way: Short) -> Unit = {}

//就绪状态
var readyStateReceiver: () -> Unit = {}

var PIDDataReceiver: (pData:Int,iData:Int,dData:Int) -> Unit = {pData, iData, dData ->  }

//Flash数据
@ExperimentalUnsignedTypes
var flashDataReceiver: (i:UByteArray)->Unit = {}

@ExperimentalUnsignedTypes
var agreementReceiverMsg: (data: UByteArray, length: Int) -> Unit = { _: UByteArray, _: Int -> }

@ExperimentalUnsignedTypes
fun onAgreementMsgReceiver(mAgreementReceiver: (data: UByteArray, length: Int) -> Unit) {
    agreementReceiverMsg = mAgreementReceiver
}

@ExperimentalUnsignedTypes
var agreementReceiverWave: (data: UByteArray, length: Int) -> Unit = { _: UByteArray, _: Int -> }

@ExperimentalUnsignedTypes
fun onAgreementWaveReceiver(mAgreementReceiver: (data: UByteArray, length: Int) -> Unit) {
    agreementReceiverWave = mAgreementReceiver
}


fun onAttitudeReceiver(mAttitudeReceiver: (i: Short) -> Unit) {
    attitudeReceiver = mAttitudeReceiver
}

fun onChariotDataReceiver(mDataReceiver: (x: Float, y: Float, battery: Float, angle: Float) -> Unit) {
    chariotDataReceiver = mDataReceiver
}

fun onIDReceiver(mIdReceiver: (id: Short) -> Unit) {
    idReceiver = mIdReceiver
}

fun onAddSuccessReceiver(mSuccessReceiver: (i: Short) -> Unit) {
    addSuccessReceiver = mSuccessReceiver
}

fun onWheelReceiver(mWheelReceiver: (i: Short) -> Unit) {
    wheelReceiver = mWheelReceiver
}

fun onLockStateReceiver(mLockStateReceiver: (i: Boolean) -> Unit) {
    lockStateReceiver = mLockStateReceiver
}

fun onGyroDataReceiver(mGyroDataReceiver: (gyroX: Short, gyroY: Short, gyroZ: Short) -> Unit) {
    gyroDataReceiver = mGyroDataReceiver
}

fun onVersionReceiver(mVersionReceiver: (i: Short) -> Unit) {
    versionReceiver = mVersionReceiver
}

fun onVoltageReceiver(mVoltageReceiver: (i: Short) -> Unit) {
    voltageReceiver = mVoltageReceiver
}

fun onErrorReceiver(mErrorReceiver: (i: Short) -> Unit) {
    errorReceiver = mErrorReceiver
}

fun onFinishedDrivingReceiver(mFinishedDrivingReceiver: (i: Short) -> Unit) {
    finishedDrivingReceiver = mFinishedDrivingReceiver
}

fun onAccDataReceiver(mAccDataReceiver: (accX: Short, accY: Short, accZ: Short) -> Unit) {
    accDataReceiver = mAccDataReceiver
}

fun onEulerPointReceiver(mEulerPointReceiver: (yaw: Short) -> Unit) {
    eulerPointReceiver = mEulerPointReceiver
}

fun onNowRunWayReceiver(mNowRunWayReceiver: (i: Short) -> Unit) {
    nowRunWayReceiver = mNowRunWayReceiver
}

fun onReadyStateReceiver(mReadyStateReceiver: () -> Unit) {
    readyStateReceiver = mReadyStateReceiver
}

@ExperimentalUnsignedTypes
fun onFlashDataReceiver(mFlashDataReceiver:(i:UByteArray)->Unit){
    flashDataReceiver = mFlashDataReceiver
}

fun PIDDataReceiverReCall(mPIDDataReceiver:(p:Int,i:Int,d:Int)->Unit){
    PIDDataReceiver = mPIDDataReceiver
}