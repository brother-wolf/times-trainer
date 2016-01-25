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
    private DBHelper dbHelper;
    private String[] allColumns = {DBHelper.COLUMN_ID, DBHelper.COLUMN_MULTIPLICAND, DBHelper.COLUMN_MULTIPLIER, DBHelper.COLUMN_ANSWER};

    public MultiplicationDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Multiplication createMultiplication(Multiplication Multiplication) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_MULTIPLICAND, Multiplication.multiplicand);
        values.put(DBHelper.COLUMN_MULTIPLIER, Multiplication.multiplier);
        values.put(DBHelper.COLUMN_ANSWER, Multiplication.answer);

        long insertId = database.insert(DBHelper.TABLE_MULTIPLICATIONS, null, values);

        Cursor cursor = database.query(
                DBHelper.TABLE_MULTIPLICATIONS,
                allColumns, DBHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Multiplication newMultiplication = cursorToMultiplication(cursor);
        cursor.close();
        return newMultiplication;
    }

    public List<Multiplication> getAllMultiplications() {


        List<Multiplication> Multiplications = new ArrayList<Multiplication>();

        Cursor cursor = database.query(DBHelper.TABLE_MULTIPLICATIONS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Multiplication Multiplication = cursorToMultiplication(cursor);
            Multiplications.add(Multiplication);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return Multiplications;
    }

    private Multiplication cursorToMultiplication(Cursor cursor) {
        return new Multiplication(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));
    }
}
