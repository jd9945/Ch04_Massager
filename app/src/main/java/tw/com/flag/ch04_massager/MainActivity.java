package tw.com.flag.ch04_massager;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends AppCompatActivity
        implements View.OnTouchListener {
    private BluetoothAdapter mBluetoothAdapter;
    public Handler mHandler;
    private BluetoothGatt mBluetoothGatt ;
    public static final String  UUID_SERVER_KEY="0000ffe0-0000-1000-8000-00805f9b34fb";
    private final static String UUID_KEY_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private static  Boolean  try_connect=false;
    Timer  timer;
  //  static final String db_name = "DeviceDB";//
  //  static final String tb_name = "DeviceTB";
   // SQLiteDatabase db;
    String   aa;
    Cursor cur;
    private TextView tvCounter;
    private long count = 0;
    private boolean run = false;
    public  DevicePar   DeveceA=new DevicePar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txv = (TextView) findViewById(R.id.txv);
        txv.setOnTouchListener(this);   // 登录触控监听对象


        final BluetoothManager bluetoothManager;
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //final TextView tt = (TextView) findViewById(R.id.rxData);
        aa = new String("ABCFS");

        tvCounter = (TextView) findViewById(R.id.rxData);
        tvCounter.setText(aa);
        run=true;

        StudentRepo repo = new StudentRepo(this);
        student studentA = new student();
        studentA = repo.getStudentById(0);

        studentA.value=35;
        studentA.email="888888";
        studentA.name="password";
        studentA.student_ID=1;
        repo.update(studentA);

        studentA.name="";
        studentA = repo.getStudentById(0);
        int   size;
        size = repo.getRecounts();
        studentA = repo.getStudentById(1);
        DeveceA.setTryConnect(false);
        mHandler = new Handler();

        timer = new Timer(true);
        timer.schedule(task,3000, 3000);
    }

    TimerTask task = new TimerTask(){
        public void run() {
            if(DeveceA.isTryConnect()==true)
            {
                Looper looper = Looper.getMainLooper();
                MyHandler mHandler = new MyHandler(looper);//构造一个handler使之可与looper通信
                mHandler.removeMessages(0);
                String msgStr ="接收数据:";
                Message m = mHandler.obtainMessage(1, 1, 1, msgStr);//构造要传递的消息
                m.what=33;
                mHandler.sendMessage(m);//发送消息:系统会自动调用handleMessage方法来处理消息

            }
        }
    };


/*接收其他线程的消息并进行处理 */

    private class MyHandler extends Handler{
        public MyHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {//处理消息

            if(msg.obj.toString().equals("ok")) {
                tvCounter.setText("成功连接");
                try_connect = false;
            }
            else if(msg.obj.toString().equals("fail"))
            {
                tvCounter.setText("断开连接");
                try_connect = true;
                DeveceA.setTryConnect(true);
              //  this.sendEmptyMessageAtTime()
            }
            else if(msg.what==2)
            {
                tvCounter.setText(msg.obj.toString());
            }
            else if((msg.what==33)&&(DeveceA.isConnect()==false))
            {
                tvCounter.setText("尝试连接");
                connect(mBluetoothAdapter.getRemoteDevice("88:4A:EA:7E:9F:18"));

            } else if (msg.what == 32) {

                tvCounter.setText("555");
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(mBluetoothAdapter!=null)
           mBluetoothAdapter.stopLeScan(mScanCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  if(mBluetoothGatt!=null)mBluetoothGatt.close();
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        Vibrator vb =
                (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(e.getAction() == MotionEvent.ACTION_DOWN) { // 按住屏幕中间的文字
            vb.vibrate(5000); // 震动 5 秒
        }
        else if(e.getAction() == MotionEvent.ACTION_UP) { // 放开手指
            vb.cancel(); // 停止震动
        }
        return true;
    }
    //************************************
    //jd945
    //*************************************
    public  void  openBleDevice(View v) {
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.enable();
        }

    }
    //************************************
    //jd945
    //*************************************
    public void   closeBleDevice(View v)
    {
        if(mBluetoothAdapter!=null){
            mBluetoothAdapter.disable();
        }

    }
    public void  scanDevice(View v)
    {
      // Private  Handler mHandler

        if(mBluetoothAdapter!=null){

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mScanCallback);
                }
            },10000);
            mBluetoothAdapter.startLeScan(mScanCallback);
        }
    }

    public  void  dialogPop(View v)
    {
        ConfirmDialog dialog = new ConfirmDialog(MainActivity.this);
        //设置它的ContentView
        dialog.setContentView(R.layout.dialog);

        dialog.show();

    }

    private BluetoothAdapter.LeScanCallback mScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device,int rssi,byte[] scanRecord){
            runOnUiThread(new Runnable() {
                Button btn;
                @Override
                public void run() {
                    Log.d("ble",device.getName()+"_"+ device.getAddress());
//                    btn = (Button) findViewById(R.id.scanDevice);
//                    btn.setText(device.getName());
//                    TextView txv = (TextView) findViewById(R.id.txv);
//                    txv.setText(device.getAddress());

                    if(device.getName().equalsIgnoreCase("IIS-HOME")) {
                        mBluetoothAdapter.cancelDiscovery();
                        mBluetoothAdapter.stopLeScan(mScanCallback);
                        connect(mBluetoothAdapter.getRemoteDevice("88:4A:EA:7E:9F:18"));
                    }

                }
            });
        }
    };


    public   void    ConnectDeviceFun(View v){
        //connect(mBluetoothAdapter.getRemoteDevice("88:4A:EA:7E:9F:18"));
        sendSetting();
        readBatrery();
    }



   // 连接/断开设备
    /**
     * 连接设备
     */
    public void connect(BluetoothDevice device) {
        if (device != null) {
          //  gatt.close()
           // if(mBluetoothGatt!=null)mBluetoothGatt.close();
            mBluetoothGatt = device.connectGatt(this, false, gattCallback);
        }
    }

//    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
//
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                Log.d("ble", "设备已连接");
//                //链接成功发现服务
//                gatt.discoverServices();
//            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                Log.d("ble", "设备已断开");
//            }
//        }
//    };


    private BluetoothGattCallback gattCallback=  new BluetoothGattCallback() {

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt,
                                                BluetoothGattCharacteristic characteristic) {

                super.onCharacteristicChanged(gatt, characteristic);
                //收到设备notify值 （设备上报值）
                Log.d("ble", "设备上报值");
                //if(characteristic.getUuid()==UUID.fromString(UUID_KEY_DATA))
                if (UUID.fromString(UUID_KEY_DATA).equals(characteristic.getUuid())) {

                     aa=Utils.bytesToHexString(characteristic.getValue());
                    DeveceA.setRxBooth(characteristic.getValue());
                    DeveceA.setConnect(true);

                  //  txv.setText(Utils.bytesToHexString(characteristic.getValue()).substring(1,3));
                    Log.d("ble",Utils.bytesToHexString(characteristic.getValue()));
                    Looper looper = Looper.getMainLooper();
                    //MyHandler mHandler =new MyHandler(looper);//构造一个handler使之可与looper通信
                    MyHandler mHandler =new MyHandler(looper);//构造一个handler使之可与looper通信
                    mHandler.removeMessages(0);
                    String msgStr ="接收数据:"+Utils.bytesToHexString(characteristic.getValue());
                    Message m = mHandler.obtainMessage(1, 1, 1, msgStr);//构造要传递的消息
                    m.what=2;
                    mHandler.sendMessage(m);//发送消息:系统会自动调用handleMessage方法来处理消息
                }

        }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt,
                                             BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                //读取到值
                Log.d("ble", "读取到值");

            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt,
                                              BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    //write成功（发送值成功）
                    Looper looper = Looper.getMainLooper();
                    //MyHandler mHandler =new MyHandler(looper);//构造一个handler使之可与looper通信
                    MyHandler mHandler =new MyHandler(looper);//构造一个handler使之可与looper通信
                    mHandler.removeMessages(0);
                    String msgStr ="发送成功";
                    Message m = mHandler.obtainMessage(1, 1, 1, msgStr);//构造要传递的消息
                    m.what=2;
                    mHandler.sendMessage(m);//发送消息:系统会自动调用handleMessage方法来处理消息
                    Log.d("ble", "发送值成功");
                }
            }

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                //    表示相应的连接或断开操作是否完成，而不是指连接状态
                    if (newState == BluetoothGatt.STATE_CONNECTED) {
                        // 连接成功
                        Log.d("ble", "设备已连接");
                        // 进行服务发现，50ms
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        gatt.discoverServices();//寻找寻找服务


                    } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                        // 断开连接
                        Log.d("ble", "设备已断开");
                    }
                }
                else if(status==8)
                {
                    DeveceA.setConnect(false);
                    Looper looper = Looper.getMainLooper();
                    //MyHandler mHandler =new MyHandler(looper);//构造一个handler使之可与looper通信
                    MyHandler mHandler =new MyHandler(looper);//构造一个handler使之可与looper通信
                    mHandler.removeMessages(0);
                    String msgStr ="fail";
                    Message m = mHandler.obtainMessage(1, 1, 1, msgStr);//构造要传递的消息
                    m.what=0x122;
                    mHandler.sendMessage(m);//发送消息:系统会自动调用handleMessage方法来处理消息
                  //  if(mBluetoothGatt!=null)mBluetoothGatt.close();
                }
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt,
                                         BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorRead(gatt, descriptor, status);
                Log.d("ble", "读取到值");

            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt,
                                          BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);

            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    //获取到RSSI，  RSSI 正常情况下 是 一个 负值，如 -33 ； 这个值的绝对值越小，代表设备离手机越近
                    //通过mBluetoothGatt.readRemoteRssi();来获取
                }
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                super.onReliableWriteCompleted(gatt, status);
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    //寻找到服务
                    Log.d("ble", "寻找到服务");
                    BluetoothGattCharacteristic characteristic = mBluetoothGatt.getService(UUID.fromString(UUID_SERVER_KEY)).getCharacteristic(UUID.fromString(UUID_KEY_DATA));
                    setCharacteristicNotification(characteristic, true);
                    sendSetting();
                    DeveceA.setConnect(true);
                    Looper looper = Looper.getMainLooper();
                    //MyHandler mHandler =new MyHandler(looper);//构造一个handler使之可与looper通信
                    MyHandler mHandler =new MyHandler(looper);//构造一个handler使之可与looper通信
                    mHandler.removeMessages(0);
                    String msgStr ="ok";
                    Message m = mHandler.obtainMessage(1, 1, 1, msgStr);//构造要传递的消息
                    m.what=0x122;
                    mHandler.sendMessage(m);//发送消息:系统会自动调用handleMessage方法来处理消息


                    //   setCharacteristicNotification(,1);
                }
            }
        };



    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
           // Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));//CLIENT_CHARACTERISTIC_CONFIG
      //  BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(UUID_SERVER_KEY));//CLIENT_CHARACTERISTIC_CONFIG

        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);

    }


    void   sendSetting(){
       // BluetoothGattService sendService=mBluetoothGatt.getService(UUID.fromString("00001805-0000-1000-8000-00805f9b34fb"));//此处的00001805...是举例，实际开发需要询问硬件那边
        BluetoothGattService sendService=mBluetoothGatt.getService(UUID.fromString(UUID_SERVER_KEY));//此处的00001805...是举例，实际开发需要询问硬件那边
        if(sendService!=null){
           // BluetoothGattCharacteristic sendCharacteristic=sendService.getCharacteristic(UUID.fromString("00002a08-0000-1000-8000-00805f9b34fb"));//此处的00002a08...是举例，实际开发需要询问硬件那边
            BluetoothGattCharacteristic sendCharacteristic=sendService.getCharacteristic(UUID.fromString(UUID_KEY_DATA));//此处的00002a08...是举例，实际开发需要询问硬件那边

            if(sendCharacteristic!=null){
                sendCharacteristic.setValue(new byte[] { 0x01,0x20,0x03,0X04,0X05,0X06,0X07,0X08,0X09,0X10  });//随便举个数据
                mBluetoothGatt.writeCharacteristic(sendCharacteristic);//写命令到设备，
            }
        }
    }

    /***读操作***/
    void   readBatrery(){
        //如上面所说，想要和一个学生通信，先知道他的班级（ServiceUUID）和学号（CharacUUID）
        BluetoothGattService batteryService=mBluetoothGatt.getService(UUID.fromString(UUID_SERVER_KEY)); //此处的0000180f...是举例，实际开发需要询问硬件那边
        if(batteryService!=null){
            BluetoothGattCharacteristic batteryCharacteristic=batteryService.getCharacteristic(UUID.fromString(UUID_KEY_DATA));//此处的00002a19...是举例，实际开发需要询问硬件那边
            if(batteryCharacteristic!=null){
                mBluetoothGatt.readCharacteristic(batteryCharacteristic); //读取电量, 这是读取batteryCharacteristic值的方法，读取其他的值也是如此，只是它们的ServiceUUID 和CharacUUID不一样
            }
        }
    }




}
