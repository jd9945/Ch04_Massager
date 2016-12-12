package tw.com.flag.ch04_massager;

/**
 * Created by Administrator on 2016/12/8.
 */
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;


public class DbHelp extends SQLiteOpenHelper {
        //数据库版本号
        private static final int DATABASE_VERSION=4;
        //数据库名称
        private static final String DATABASE_NAME="jd9945.db";
        public DbHelp(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //创建数据表
            String CREATE_TABLE_STUDENT="CREATE TABLE IF NOT EXISTS "+ student.TABLE+"("
            //String CREATE_TABLE_STUDENT="CREATE TABLE "+ student.TABLE+"("
                    +student.KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    +student.KEY_name+" TEXT, "
                    +student.KEY_value+" INTEGER, "
                    +student.KEY_email+" TEXT)";
            db.execSQL(CREATE_TABLE_STUDENT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //如果旧表存在，删除，所以数据将会消失
            db.execSQL("DROP TABLE IF EXISTS "+ student.TABLE);
            //再次创建表
            onCreate(db);
        }

}
