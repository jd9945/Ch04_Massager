package tw.com.flag.ch04_massager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/8.
 */

public class StudentRepo {
    private DbHelp dbHelper;

    public StudentRepo(Context context){
        dbHelper=new DbHelp(context);
    }

    public int insert(student student){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(student.KEY_value,student.value);
        values.put(student.KEY_email,student.email);
        values.put(student.KEY_name,student.name);
        //
        long student_Id=db.insert(student.TABLE,null,values);
        db.close();
        return (int)student_Id;
    }
   public  int   getRecounts(){
       //获取有几条记录
       Cursor cur;
       SQLiteDatabase db=dbHelper.getWritableDatabase();
       cur=db.rawQuery("SELECT * FROM "+ student.TABLE, null);  // 查询数据
       return   cur.getCount();
   }
    public void delete(int student_Id){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(student.TABLE,student.KEY_ID+"=?", new String[]{String.valueOf(student_Id)});
        db.close();
    }
    public void update(student student){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(student.KEY_value,student.value);
        values.put(student.KEY_email,student.email);
        values.put(student.KEY_name,student.name);

        db.update(student.TABLE,values,student.KEY_ID+"=?",new String[] { String.valueOf(student.student_ID) });
        db.close();

    }

    public ArrayList<HashMap<String, String>> getStudentList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                student.KEY_ID+","+
                student.KEY_name+","+
                student.KEY_email+","+
                student.KEY_value+" FROM "+student.TABLE;
        ArrayList<HashMap<String,String>> studentList=new ArrayList<HashMap<String, String>>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                HashMap<String,String> student1=new HashMap<String,String>();
                student1.put("id",cursor.getString(cursor.getColumnIndex(student.KEY_ID)));
                student1.put("name",cursor.getString(cursor.getColumnIndex(student.KEY_name)));
                studentList.add(student1);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return studentList;
    }

    public student getStudentById(int Id){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                student.KEY_ID + "," +
                student.KEY_name + "," +
                student.KEY_email + "," +
                student.KEY_value +
                " FROM " + student.TABLE
                + " WHERE " +
                student.KEY_ID + "=?";
        int iCount=0;
        student student=new student();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(Id)});
        if(cursor.moveToFirst()){
            do{
                student.student_ID =cursor.getInt(cursor.getColumnIndex(student.KEY_ID));
                student.name =cursor.getString(cursor.getColumnIndex(student.KEY_name));
                student.email  =cursor.getString(cursor.getColumnIndex(student.KEY_email));
                student.value =cursor.getInt(cursor.getColumnIndex(student.KEY_value));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return student;
    }

}
