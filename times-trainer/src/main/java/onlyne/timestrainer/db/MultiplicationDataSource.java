package onlyne.timestrainer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import onlyne.timestrainer.Multiplication;

public class MultiplicationDataSource {
    private MultiplicationDBHelper multiplicationDbHelper;

    public MultiplicationDataSource(Context context) {
        multiplicationDbHelper = new MultiplicationDBHelper(context);
    }

    public SQLiteDatabase open() throws SQLException {
        return multiplicationDbHelper.getWritableDatabase();
    }

    public void close() {
        multiplicationDbHelper.close();
    }

    public Multiplication createMultiplication(Multiplication multiplication, String username) {
        SQLiteDatabase database = open();
        ContentValues values = new ContentValues();
        values.put(MultiplicationDBHelper.COLUMN_USERNAME, username);
        values.put(MultiplicationDBHelper.COLUMN_MULTIPLICAND, multiplication.multiplicand);
        values.put(MultiplicationDBHelper.COLUMN_MULTIPLIER, multiplication.multiplier);
        values.put(MultiplicationDBHelper.COLUMN_ANSWER, multiplication.answer);
        values.put(MultiplicationDBHelper.COLUMN_TIME_TAKEN_IN_MILLIS, multiplication.timeTakenInMillis);

        long insertId = database.insert(MultiplicationDBHelper.TABLE_MULTIPLICATIONS, null, values);

        String whereClause = String.format("%s = %d", MultiplicationDBHelper.COLUMN_ID, insertId);

        Cursor cursor = database.query(
                MultiplicationDBHelper.TABLE_MULTIPLICATIONS,
                MultiplicationDBHelper.ALL_COLUMNS,
                whereClause, null, null, null, null);

        Multiplication newMultiplication = null;

        if (cursor.moveToFirst()) {
            newMultiplication = cursorToMultiplication(cursor);
        }

        cursor.close();
        close();
        return newMultiplication;
    }

    public List<Multiplication> getAllMultiplications(String username) {
        SQLiteDatabase database = open();

        List<Multiplication> multiplications = new ArrayList<Multiplication>();

        String whereClause = String.format("%s = \"%s\"", MultiplicationDBHelper.COLUMN_USERNAME, username);

        Cursor cursor = database.query(
                MultiplicationDBHelper.TABLE_MULTIPLICATIONS,
                MultiplicationDBHelper.ALL_COLUMNS,
                whereClause,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Multiplication multiplication = cursorToMultiplication(cursor);
                multiplications.add(multiplication);
                cursor.moveToNext();
            }
        }
        cursor.close();
        close();
        return multiplications;
    }

    private Multiplication cursorToMultiplication(Cursor cursor) {
        return new Multiplication(
                cursor.getInt(cursor.getColumnIndex(MultiplicationDBHelper.COLUMN_MULTIPLICAND)),
                cursor.getInt(cursor.getColumnIndex(MultiplicationDBHelper.COLUMN_MULTIPLIER)),
                cursor.getInt(cursor.getColumnIndex(MultiplicationDBHelper.COLUMN_ANSWER)),
                cursor.getLong(cursor.getColumnIndex(MultiplicationDBHelper.COLUMN_TIME_TAKEN_IN_MILLIS)));
    }
}
