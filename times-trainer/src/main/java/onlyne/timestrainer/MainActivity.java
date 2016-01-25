package onlyne.timestrainer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int score = 0;
    private int attempts = 0;
    private Random dice = new Random(now());
    private Queue<Multiplication> multiplicationsQueue;
    private long timerStart = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                doEvaluateAnswer();
            }
        });

        multiplicationsQueue = new LinkedList<>();
        generateNewQuestion(
                (TextView) findViewById(R.id.multiplicand),
                (TextView) findViewById(R.id.multiplier));

        timerStart = now();
    }

    private long now() {
        return new Date().getTime();
    }

    private void doEvaluateAnswer() {
        incrementAttempts();

        long timeTakenInMillis = now() - timerStart;

        TextView multiplicandTV = (TextView) findViewById(R.id.multiplicand);
        TextView multiplierTV = (TextView) findViewById(R.id.multiplier);
        EditText answerET = (EditText) findViewById(R.id.answer);

        recordAnswer(multiplicandTV, multiplierTV, answerET, timeTakenInMillis);

        generateNewQuestion(multiplicandTV, multiplierTV);
        clearAnswerForm(answerET);
    }

    private void incrementAttempts() {
        attempts++;
        TextView attemptsTV = (TextView) findViewById(R.id.attempts);
        attemptsTV.setText(Integer.toString(attempts));
    }

    private void clearAnswerForm(EditText answerET) {
        answerET.setText("");
    }

    private void recordAnswer(TextView multiplicandTV, TextView multiplierTV, EditText answerET, long timeTakenInMillis) {
        int multiplicand = Integer.valueOf(multiplicandTV.getText().toString());
        int multiplier = Integer.valueOf(multiplierTV.getText().toString());
        int answer = Integer.valueOf(answerET.getText().toString());
        if (multiplicand * multiplier == answer) {
            score++;
            TextView scoreTV = (TextView) findViewById(R.id.score);
            scoreTV.setText(Integer.toString(score));
        }
        multiplicationsQueue.add(new Multiplication(multiplicand, multiplier, answer, timeTakenInMillis));
    }

    private void generateNewQuestion(TextView multiplicandTV, TextView multiplierTV) {
        multiplicandTV.setText(intToString(roll(12)));
        multiplierTV.setText(intToString(roll(12)));
    }

    private String intToString(int roll) {
        return String.format("%d", roll);
    }

    private int roll(int sides) {
        return dice.nextInt(sides) + 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
