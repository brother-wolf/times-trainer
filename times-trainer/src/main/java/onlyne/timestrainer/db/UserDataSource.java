package onlyne.timestrainer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDataSource {

    private UserDBHelper userDbHelper;
    private MultiplicationDBHelper multiplicationDBHelper;

    public UserDataSource(Context context) {
        userDbHelper = new UserDBHelper(context);
        multiplicationDBHelper = new MultiplicationDBHelper(context);
    }

    public SQLiteDatabase openForWriting() throws SQLException {
        SQLiteDatabase writableDatabase = userDbHelper.getWritableDatabase();
        maybeKickIt(UserDBHelper.TABLE_USERS, writableDatabase, userDbHelper);
        return writableDatabase;
    }

    public SQLiteDatabase openForReading() throws SQLException {
        SQLiteDatabase readableDatabase = userDbHelper.getReadableDatabase();
        maybeKickIt(UserDBHelper.TABLE_USERS, readableDatabase, userDbHelper);
        return readableDatabase;
    }

    public void close() {
        userDbHelper.close();
    }

    public String createUsername(String username) {
        Log.d(this.getClass().getName(), "creating user");
        SQLiteDatabase database = openForWriting();
        ContentValues values = new ContentValues();
        values.put(UserDBHelper.COLUMN_USERNAME, username);

        long insertId = database.insert(UserDBHelper.TABLE_USERS, null, values);

        String newUsername = getUsernameById(insertId, database);
        close();
        database.close();
        return newUsername;
    }

    public void maybeKickIt(String tableUsers, SQLiteDatabase database, SQLiteOpenHelper dbHelper) {
        if (!isTableInDatabase(tableUsers, database)) {
            dbHelper.onCreate(database);
        }
    }

    public boolean isUsername(String username) {
        Log.d(this.getClass().getName(), "checking if user exists");
        SQLiteDatabase database = openForReading();

        String whereClause = String.format("%s = \"%s\"", UserDBHelper.COLUMN_USERNAME, username);

        Cursor cursor = database.query(
                UserDBHelper.TABLE_USERS,
                UserDBHelper.ALL_COLUMNS,
                whereClause, null, null, null, null);

        String dbUsername = null;

        if (cursor.moveToFirst()) {
            dbUsername = cursor.getString(cursor.getColumnIndex(UserDBHelper.COLUMN_USERNAME));
        }

        cursor.close();
        close();
        database.close();
        return username.equals(dbUsername);
    }

    private boolean isTableInDatabase(String tableName, SQLiteDatabase database) {
        Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        boolean tableIsCreated = false;

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {

                String tableInDbName = c.getString(0);
                if (tableInDbName.equals(tableName)) tableIsCreated = true;
                c.moveToNext();
            }
        }
        return tableIsCreated;
    }

    private String getUsernameById(long insertId, SQLiteDatabase database) {
        Log.d(this.getClass().getName(), "getting user by id");

        String whereClause = String.format("%s = %d", UserDBHelper.COLUMN_ID, insertId);

        Cursor cursor = database.query(
                UserDBHelper.TABLE_USERS,
                UserDBHelper.ALL_COLUMNS,
                whereClause, null, null, null, null);
        cursor.moveToFirst();
        String dbUsername = cursor.getString(cursor.getColumnIndex(UserDBHelper.COLUMN_ID));

        cursor.close();
        return dbUsername;
    }
}
