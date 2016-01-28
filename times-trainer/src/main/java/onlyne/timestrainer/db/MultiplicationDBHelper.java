package onlyne.timestrainer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MultiplicationDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_MULTIPLICATIONS = "multiplications";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MULTIPLICAND = "multiplicand";
    public static final String COLUMN_MULTIPLIER = "multiplier";
    public static final String COLUMN_ANSWER = "answer";

    private static final String DATABASE_NAME = "onlyne.multiplications.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String CREATE_TABLE = String.format(
            "create table %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL);",
            TABLE_MULTIPLICATIONS,
            COLUMN_ID,
            COLUMN_MULTIPLICAND,
            COLUMN_MULTIPLIER,
            COLUMN_ANSWER
            );
    public static final String DELETE_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_MULTIPLICATIONS);

    public MultiplicationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.w(MultiplicationDBHelper.class.getName(), "Creating database");
        database.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(MultiplicationDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL(DELETE_TABLE);
        onCreate(database);
    }
}
