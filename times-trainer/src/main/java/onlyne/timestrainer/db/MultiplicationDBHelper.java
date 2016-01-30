package onlyne.timestrainer.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MultiplicationDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_MULTIPLICATIONS = "multiplications";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_MULTIPLICAND = "multiplicand";
    public static final String COLUMN_MULTIPLIER = "multiplier";
    public static final String COLUMN_ANSWER = "answer";
    public static final String COLUMN_TIME_TAKEN_IN_MILLIS = "time_taken_in_millis";
    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_USERNAME,
            COLUMN_MULTIPLICAND,
            COLUMN_MULTIPLIER,
            COLUMN_ANSWER,
            COLUMN_TIME_TAKEN_IN_MILLIS
    };

    private static final String DATABASE_NAME = "onlyne.multiplications.db";
    private static final int DATABASE_VERSION = 3;

    private static final String CREATE_TABLE_MULTIPLICATIONS_QUERY = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL)",
            TABLE_MULTIPLICATIONS,
            COLUMN_ID,
            COLUMN_USERNAME,
            COLUMN_MULTIPLICAND,
            COLUMN_MULTIPLIER,
            COLUMN_ANSWER,
            COLUMN_TIME_TAKEN_IN_MILLIS
            );
    public static final String DELETE_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_MULTIPLICATIONS);

    public MultiplicationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.w(MultiplicationDBHelper.class.getName(), "Creating database");
        Log.d(this.getClass().getName(), String.format("Using this sql: %s", CREATE_TABLE_MULTIPLICATIONS_QUERY));
        try {
            database.execSQL(CREATE_TABLE_MULTIPLICATIONS_QUERY);
            Log.i(this.getClass().getName(), String.format("Created table %s", TABLE_MULTIPLICATIONS));
        } catch (SQLException sqle) {
            Log.e(this.getClass().getName(), String.format("Failed to create table %s", TABLE_MULTIPLICATIONS));
        }
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
