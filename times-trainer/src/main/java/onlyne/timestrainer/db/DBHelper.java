package onlyne.timestrainer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_MULTIPLICATIONS = "multiplications";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MULTIPLICAND = "multiplicand";
    public static final String COLUMN_MULTIPLIER = "multiplier";
    public static final String COLUMN_ANSWER = "answer";

    private static final String DATABASE_NAME = "multiplications.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_MULTIPLICATIONS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_MULTIPLICAND + "integer, "
            + COLUMN_MULTIPLIER + "integer, "
            + COLUMN_ANSWER + "integer);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MULTIPLICATIONS);
        onCreate(db);
    }
}
