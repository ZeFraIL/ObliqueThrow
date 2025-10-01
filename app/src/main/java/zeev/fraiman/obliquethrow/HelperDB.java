package zeev.fraiman.obliquethrow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HelperDB extends SQLiteOpenHelper {

    public static final String DB_FILE="all_info.db";

    //USERS
    public static final String USERS_TABLE="Users";
    public static final String USER_NAME="Name";
    public static final String USER_MAIL="Email";
    public static final String USER_PASSWORD="Password";

    //LINKS
    public static final String LINKS_TABLE="Links";
    public static final String LINK_NAME="lName";
    public static final String LINK_ID="lID";
    public static final String LINK_TYPE="lType";

    //EXERCISES
    public static final String EXERCISES_TABLE="Exercises";
    public static final String EXERCISE_V0="eV0";
    public static final String EXERCISE_ANGLE="eAngle";
    public static final String EXERCISE_HEIGHT="eH0";
    public static final String EXERCISE_HEIGHT_MAX="eHMax";
    public static final String EXERCISE_LENGTH_MAX="eLMax";
    public static final String EXERCISE_TOTAL_TIME="eTTotal";



    public HelperDB(@Nullable Context context) {
        super(context, DB_FILE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String st="CREATE TABLE IF NOT EXISTS ";
        st+=USERS_TABLE+" ( ";
        st+=USER_NAME+" TEXT, ";
        st+=USER_MAIL+" TEXT, ";
        st+=USER_PASSWORD+" TEXT); ";
        db.execSQL(st);

        st = "CREATE TABLE IF NOT EXISTS " + LINKS_TABLE;
        st += " ( " + LINK_NAME + " TEXT, " + LINK_ID + " TEXT, ";
        st += LINK_TYPE + " TEXT);";
        db.execSQL(st);

        st = "CREATE TABLE IF NOT EXISTS " + EXERCISES_TABLE;
        st += " ( " + EXERCISE_V0 + " REAL, " + EXERCISE_ANGLE + " REAL, ";
        st += EXERCISE_HEIGHT + " REAL, " + EXERCISE_HEIGHT_MAX + " REAL, ";
        st += EXERCISE_LENGTH_MAX + " REAL, " + EXERCISE_TOTAL_TIME + " REAL);";
        db.execSQL(st);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}