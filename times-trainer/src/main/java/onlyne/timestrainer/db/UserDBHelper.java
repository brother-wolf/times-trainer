package onlyne.timestrainer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "name";

    private static final String DATABASE_NAME = "onlyne.multiplications.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_USER_TABLE_QUERY = String.format(
            "create table %s (%s integer primary key autoincrement, %s TEXT NOT NULL);",
            TABLE_USERS,
            COLUMN_ID,
            COLUMN_USERNAME);
    public static final String DATABASE_DELETE = String.format("DROP TABLE IF EXISTS %s", TABLE_USERS);

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_USER_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(UserDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL(DATABASE_DELETE);
        onCreate(db);
    }
}
