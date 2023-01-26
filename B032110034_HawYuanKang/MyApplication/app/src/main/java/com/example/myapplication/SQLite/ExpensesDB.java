package com.example.myapplication.SQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Student;
import com.example.myapplication.User;

import java.util.ArrayList;
import java.util.List;

public class ExpensesDB extends SQLiteOpenHelper {
    public static final String dbName="dbMyExpense";
    public static final String tblNameExpense="expenses";
    public static final String StuID="expense_ID";
    public static final String StuFullName="expense_name";
    public static final String StuEmail="expense_email";
    public static final String StuDateBirth="expense_dob";
    public static final String StuState="expense_state";
    public static final String StuGender="expense_gender";

    public static final String strCrtTblExpenses = "CREATE TABLE "+ tblNameExpense + " ("+ StuID + " TEXT PRIMARY KEY, " + StuFullName +" TEXT, "+ StuEmail +" TEXT, " + StuDateBirth +" TEXT, " + StuState +" TEXT, "+ StuGender +" TEXT)";
    public static final String strDropTblExpenses = "DROP TABLE IF EXISTS "+ tblNameExpense;

    public ExpensesDB(Context context)
    {
        super(context,dbName,null,1);
    }
    

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(strCrtTblExpenses);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(strDropTblExpenses);
        onCreate(sqLiteDatabase);
    }

    public float fnInsertExpense(Student student)
    {
        float retResult=0;
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(StuID,student.getStrStudNo());
        values.put(StuFullName,student.getStrFullname());
        values.put(StuEmail,student.getStrEmail());
        values.put(StuDateBirth,student.getStrBirthdate());
        values.put(StuState,student.getStrState());
        values.put(StuGender,student.getStrGender());

        retResult=sqLiteDatabase.insert(tblNameExpense,null,values);
        return retResult;
    }

    @SuppressLint("Range")
    public Student fnGetExpenses(int intStuID)
    {
        Student student=new Student();
        String strSelQry="SELECT * FROM "+tblNameExpense+" WHERE "+StuID+" = " + intStuID;
        Cursor cursor = this.getReadableDatabase().rawQuery(strSelQry,null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        student.setStrStudNo(cursor.getString(cursor.getColumnIndex(StuID)));
        student.setStrFullname(cursor.getString(cursor.getColumnIndex(StuFullName)));
        student.setStrBirthdate(cursor.getString(cursor.getColumnIndex(StuDateBirth)));
        student.setStrState(cursor.getString(cursor.getColumnIndex(StuState)));
        student.setStrGender(cursor.getString(cursor.getColumnIndex(StuGender)));
        student.setStrEmail(cursor.getString(cursor.getColumnIndex(StuEmail)));

        return student;
    }

    @SuppressLint("Range")
    public List<Student> fnGetAllExpenses()
    {
        List<Student> studentList = new ArrayList<Student>();
        String strSelAll = "Select * from "+ tblNameExpense;
        Cursor cursor = this.getReadableDatabase().rawQuery(strSelAll,null);
        if (cursor.moveToFirst()){
            do{
                Student student = new Student();
                student.setStrStudNo(cursor.getString(cursor.getColumnIndex(StuID)));
                student.setStrFullname(cursor.getString(cursor.getColumnIndex(StuFullName)));
                student.setStrEmail(cursor.getString(cursor.getColumnIndex(StuEmail)));
                student.setStrBirthdate(cursor.getString(cursor.getColumnIndex(StuDateBirth)));
                student.setStrGender(cursor.getString(cursor.getColumnIndex(StuGender)));
                student.setStrState(cursor.getString(cursor.getColumnIndex(StuState)));
                studentList.add(student);
            }while (cursor.moveToNext());
        }return studentList;
    }

    public int fnUpdateExpenses(Student student)
    {
        int retResult=0;
        ContentValues values=new ContentValues();
        values.put(StuID,student.getStrStudNo());
        values.put(StuFullName,student.getStrFullname());
        values.put(StuEmail,student.getStrEmail());
        values.put(StuDateBirth,student.getStrBirthdate());
        values.put(StuState,student.getStrState());
        values.put(StuGender,student.getStrGender());

        String []argg={String.valueOf(student.getStrStudNo())};
        this.getWritableDatabase().update(tblNameExpense,values,StuID+" = ?",argg);
        return retResult;
    }
}
