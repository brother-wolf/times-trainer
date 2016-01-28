package onlyne.timestrainer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import onlyne.timestrainer.Multiplication;

public class UserDataSource {

    // Database fields
    private UserDBHelper userDbHelper;

    public UserDataSource(Context context) {
        userDbHelper = new UserDBHelper(context);
    }

    public SQLiteDatabase open() throws SQLException {

        return userDbHelper.getWritableDatabase();
    }

    public void close() {
        userDbHelper.close();
    }

    public String createUsername(String username) {
        Log.d(this.getClass().getName(), "creating user");
        SQLiteDatabase database = open();
        ContentValues values = new ContentValues();
        values.put(UserDBHelper.COLUMN_USERNAME, username);

        long insertId = database.insert(UserDBHelper.TABLE_USERS, null, values);

        String newUsername = getUsernameById(insertId, database);
        close();
        return newUsername;
    }

    public boolean isUsername(String username) {
        Log.d(this.getClass().getName(), "checking if user exists");
        SQLiteDatabase database = open();

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
        return username.equals(dbUsername);
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
