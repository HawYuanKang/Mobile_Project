package ftmk.bits.myapplication.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.ContentView;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ftmk.bits.myapplication.Student;
import ftmk.bits.myapplication.User;

public class SQLiteStudent extends SQLiteOpenHelper {

    public static final String dbName = "Student";
    public static final String tblName = "attendance";
    public static final String colStID = "student_id";
    public static final String colStName = "student_name";
    public static final String colStEmail = "student_email";
    public static final String colStDob = "student_dob";
    public static final String colStGender = "student_gender";
    public static final String colStState = "student_state";
    public static final String sqlCreate = "CREATE TABLE "+ tblName + " ("+ colStID + " TEXT PRIMARY KEY, " + colStName +" TEXT, "+ colStEmail +" TEXT, " + colStDob +" TEXT," + colStGender +" TEXT,"+ colStState +" TEXT)";
    public static final String sqlDrop = "DROP TABLE IF EXISTS "+ tblName;

    public SQLiteStudent(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(sqlDrop);
        onCreate(sqLiteDatabase);
    }

    public float fnInsertStudent(Student student){

        float retResult = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(colStID, student.getStrStudNo());
        values.put(colStName, student.getStrFullname());
        values.put(colStEmail, student.getStrEmail());
        values.put(colStDob, student.getStrBirthdate());
        values.put(colStGender, student.getStrGender());
        values.put(colStState, student.getStrState());

        retResult = db.insert(tblName, null, values);
        return retResult;
    }

    @SuppressLint("Range")
    public Student fnGetStudent(int intStId)
    {
        Student student = new Student();
        String strSelQry = "Select * from "+ tblName + " where "+ colStID+" = " + intStId;
        Cursor cursor = this.getReadableDatabase().rawQuery(strSelQry,null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        student.setStrStudNo(cursor.getString(cursor.getColumnIndex(colStID)));
        student.setStrFullname(cursor.getString(cursor.getColumnIndex(colStName)));
        student.setStrEmail(cursor.getString(cursor.getColumnIndex(colStEmail)));
        student.setStrBirthdate(cursor.getString(cursor.getColumnIndex(colStDob)));
        student.setStrGender(cursor.getString(cursor.getColumnIndex(colStGender)));
        student.setStrState(cursor.getString(cursor.getColumnIndex(colStID)));
        return student;
    }

    @SuppressLint("Range")
    public List<Student> fnGelAllStudent(){
        List<Student> listStr = new ArrayList<Student>();
        String strSelAll = "Select * from "+ tblName;
        Cursor cursor = this.getReadableDatabase().rawQuery(strSelAll,null);
        if (cursor.moveToFirst()){
            do{
                Student student = new Student();
                student.setStrStudNo(cursor.getString(cursor.getColumnIndex(colStID)));
                student.setStrFullname(cursor.getString(cursor.getColumnIndex(colStName)));
                student.setStrEmail(cursor.getString(cursor.getColumnIndex(colStEmail)));
                student.setStrBirthdate(cursor.getString(cursor.getColumnIndex(colStDob)));
                student.setStrGender(cursor.getString(cursor.getColumnIndex(colStGender)));
                student.setStrState(cursor.getString(cursor.getColumnIndex(colStID)));
                listStr.add(student);
            }while (cursor.moveToNext());
        }return listStr;
    }

    public int fnUpdateStudent(Student student){
        int retResult = 0;
        ContentValues values = new ContentValues();
        values.put(colStID, student.getStrStudNo());
        values.put(colStName, student.getStrFullname());
        values.put(colStEmail, student.getStrEmail());
        values.put(colStDob, student.getStrBirthdate());
        values.put(colStGender, student.getStrGender());
        values.put(colStState, student.getStrState());

        String [] argg = {String.valueOf(student.getStrStudNo())};
        this.getWritableDatabase().update(tblName,values,colStID+ " = ?",argg);
        return retResult;
    }
}
