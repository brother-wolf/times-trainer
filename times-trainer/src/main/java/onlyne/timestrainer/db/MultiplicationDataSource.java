package onlyne.timestrainer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import onlyne.timestrainer.Multiplication;

public class MultiplicationDataSource {
    private MultiplicationDBHelper multiplicationDbHelper;

    public MultiplicationDataSource(Context context) {
        multiplicationDbHelper = new MultiplicationDBHelper(context);
    }

    public SQLiteDatabase openForWriting() throws SQLException {
        SQLiteDatabase writableDatabase = multiplicationDbHelper.getWritableDatabase();
        maybeKickIt(MultiplicationDBHelper.TABLE_MULTIPLICATIONS, writableDatabase, multiplicationDbHelper);
        return writableDatabase;
    }

    private SQLiteDatabase openForReading() {
        SQLiteDatabase readableDatabase = multiplicationDbHelper.getReadableDatabase();
        maybeKickIt(MultiplicationDBHelper.TABLE_MULTIPLICATIONS, readableDatabase, multiplicationDbHelper);
        return readableDatabase;
    }

    public void maybeKickIt(String tableUsers, SQLiteDatabase database, SQLiteOpenHelper dbHelper) {
        if (!isTableInDatabase(tableUsers, database)) {
            dbHelper.onCreate(database);
        }
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



    public void close() {
        multiplicationDbHelper.close();
    }

    public Multiplication createMultiplication(Multiplication multiplication, String username) {

        SQLiteDatabase database = openForWriting();
        long insertId = database.insert(
                MultiplicationDBHelper.TABLE_MULTIPLICATIONS,
                null,
                prepareContentInsertionValues(multiplication, username));

        String whereClause = String.format("%s = %d", MultiplicationDBHelper.COLUMN_ID, insertId);

        Cursor cursor = database.query(
                MultiplicationDBHelper.TABLE_MULTIPLICATIONS,
                MultiplicationDBHelper.ALL_COLUMNS,
                whereClause, null, null, null, null);

        Multiplication newMultiplication = null;

        if (cursor.moveToFirst()) {
            newMultiplication = toMultiplication(cursor);
        }

        cursor.close();
        close();
        return newMultiplication;
    }

    public List<Multiplication> getAllMultiplications(String username) {
        Log.i(this.getClass().getName(), "getting all multilications");
        SQLiteDatabase database = openForWriting();

        logTableNames(database);

        List<Multiplication> multiplications = new ArrayList<>();

//        Log.d(this.getClass().getName(), "" + database.getVersion());
        String whereClause = String.format("%s = \"%s\"", MultiplicationDBHelper.COLUMN_USERNAME, username);

        Cursor cursor = null;
        try {
            cursor = database.query(
                    MultiplicationDBHelper.TABLE_MULTIPLICATIONS,
                    MultiplicationDBHelper.ALL_COLUMNS,
                    whereClause,
                    null, null, null, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Multiplication multiplication = toMultiplication(cursor);
                    multiplications.add(multiplication);
                    cursor.moveToNext();
                }
            }
        } catch (SQLException sqle) {
            Log.e(this.getClass().getName(), sqle.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        close();
        return multiplications;
    }

    private void logTableNames(SQLiteDatabase database) {
        Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                Log.d(this.getClass().getName(), "Table Name=> " + c.getString(0));
                c.moveToNext();
            }
        }
    }

    private Multiplication toMultiplication(Cursor cursor) {
        return new Multiplication(
                cursor.getInt(cursor.getColumnIndex(MultiplicationDBHelper.COLUMN_MULTIPLICAND)),
                cursor.getInt(cursor.getColumnIndex(MultiplicationDBHelper.COLUMN_MULTIPLIER)),
                cursor.getInt(cursor.getColumnIndex(MultiplicationDBHelper.COLUMN_ANSWER)),
                cursor.getLong(cursor.getColumnIndex(MultiplicationDBHelper.COLUMN_TIME_TAKEN_IN_MILLIS)));
    }

    private ContentValues prepareContentInsertionValues(Multiplication multiplication, String username) {
        ContentValues values = new ContentValues();
        values.put(MultiplicationDBHelper.COLUMN_USERNAME, username);
        values.put(MultiplicationDBHelper.COLUMN_MULTIPLICAND, multiplication.multiplicand);
        values.put(MultiplicationDBHelper.COLUMN_MULTIPLIER, multiplication.multiplier);
        values.put(MultiplicationDBHelper.COLUMN_ANSWER, multiplication.answer);
        values.put(MultiplicationDBHelper.COLUMN_TIME_TAKEN_IN_MILLIS, multiplication.timeTakenInMillis);
        return values;
    }
}
