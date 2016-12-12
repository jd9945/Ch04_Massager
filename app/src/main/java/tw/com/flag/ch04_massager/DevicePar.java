package tw.com.flag.ch04_massager;

import android.support.v7.app.AppCompatActivity;


/**
 * Created by Administrator on 2016/12/8.
 */

public class DevicePar extends AppCompatActivity {
     static final String db_name = "DeviceDB";//
     static final String tb_name = "DeviceTB";
     private   boolean   connect;   /*是否连接成功，app开始时进行连接 */
     private   boolean   tryConnect; /*断开连接了， 需尝试连连 */
     private   byte[] RxBooth = new byte[20];
     private   byte[] RxData =  new byte[100];
     private   String  pwd=new String("888888");

    public boolean isConnect() {
        return connect;
    }

    public void setConnect(boolean connect) {
        this.connect = connect;
    }

    public boolean isTryConnect() {
        return tryConnect;
    }

    public void setTryConnect(boolean tryConnect) {
        this.tryConnect = tryConnect;
    }

    public byte[] getRxBooth() {
        return RxBooth;
    }

    public void setRxBooth(byte[] rxBooth) {
        RxBooth = rxBooth;
    }

    public byte[] getRxData() {
        return RxData;
    }

    public void setRxData(byte[] rxData) {
        RxData = rxData;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}
