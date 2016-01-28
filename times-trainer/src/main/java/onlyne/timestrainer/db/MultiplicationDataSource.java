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

    // Database fields
    private SQLiteDatabase database;
    private MultiplicationDBHelper multiplicationDbHelper;
    private String[] allColumns = {MultiplicationDBHelper.COLUMN_ID, MultiplicationDBHelper.COLUMN_MULTIPLICAND, MultiplicationDBHelper.COLUMN_MULTIPLIER, MultiplicationDBHelper.COLUMN_ANSWER};

    public MultiplicationDataSource(Context context) {
        multiplicationDbHelper = new MultiplicationDBHelper(context);
    }

    public void open() throws SQLException {
        database = multiplicationDbHelper.getWritableDatabase();
    }

    public void close() {
        multiplicationDbHelper.close();
    }

    public Multiplication createMultiplication(Multiplication multiplication) {
        ContentValues values = new ContentValues();
        values.put(MultiplicationDBHelper.COLUMN_MULTIPLICAND, multiplication.multiplicand);
        values.put(MultiplicationDBHelper.COLUMN_MULTIPLIER, multiplication.multiplier);
        values.put(MultiplicationDBHelper.COLUMN_ANSWER, multiplication.answer);

        long insertId = database.insert(MultiplicationDBHelper.TABLE_MULTIPLICATIONS, null, values);

        Cursor cursor = database.query(MultiplicationDBHelper.TABLE_MULTIPLICATIONS,
                allColumns, MultiplicationDBHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Multiplication newMultiplication = cursorToMultiplication(cursor);

        cursor.close();
        return newMultiplication;
    }

    public List<Multiplication> getAllMultiplications() {


        List<Multiplication> multiplications = new ArrayList<Multiplication>();

        Cursor cursor = database.query(MultiplicationDBHelper.TABLE_MULTIPLICATIONS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Multiplication multiplication = cursorToMultiplication(cursor);
            multiplications.add(multiplication);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return multiplications;
    }

    private Multiplication cursorToMultiplication(Cursor cursor) {
        return new Multiplication(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getLong(3));
    }
}
