package onlyne.timestrainer;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import onlyne.timestrainer.db.MultiplicationDataSource;

public class MultiplicationActivity extends AbstractTimesTrainerActivity {
    private int score = 0;
    private int attempts = 0;
    private Random dice = new Random(now());
    private Queue<Multiplication> multiplicationsQueue;
    private long timerStart = 0;
    private MultiplicationDataSource multiplicationDataSource;
    private int MILLI_SECOND_INTERVAL = 1000;
    private Handler timerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiplication_input_frame);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        Bundle extras = getIntent().getExtras();
        setUsername(extras.getString(LoginScreenActivity.USERNAME));
        TextView nameTV = (TextView) findViewById(R.id.name);
        nameTV.setText(getUsername());

        multiplicationDataSource = new MultiplicationDataSource(this);

        findViewById(R.id.submit).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                doEvaluateAnswer();
            }
        });

        List<Multiplication> savedMultiplications = multiplicationDataSource.getAllMultiplications(getUsername());
        multiplicationsQueue = new LinkedList<>();
        for (Multiplication multiplication : savedMultiplications) {
            multiplicationsQueue.add(multiplication);
        }

        TextView scoreTV = (TextView) findViewById(R.id.score);
        TextView attemptsTV = (TextView) findViewById(R.id.attempts);

        score = countCorrectScores();
        attempts = multiplicationsQueue.size();

        scoreTV.setText(String.format("%d", score));
        attemptsTV.setText(String.format("%d", attempts));

        displayNewQuestion();
        timerHandler = new Handler();
        startRepeatingTask();
    }

    private int countCorrectScores() {
        int correct = 0;
        for (Multiplication multiplication : multiplicationsQueue) {
            if (multiplication.multiplicand * multiplication.multiplier == multiplication.answer) {
                correct++;
            }
        }
        return correct;
    }

    @Override
    protected void onDestroy() {
        stopRepeatingTask();
        super.onDestroy();
    }


    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            updateClock();
            timerHandler.postDelayed(mStatusChecker, MILLI_SECOND_INTERVAL);
        }
    };

    private void updateClock() {
        TextView timerTV = (TextView) findViewById(R.id.timer);
        timerTV.setText(getClockTime());
    }


    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        timerHandler.removeCallbacks(mStatusChecker);
    }

    private String getClockTime() {
        long timeInSeconds = (now() - timerStart) / 1000;
        long seconds = timeInSeconds % 60;
        long minutes = timeInSeconds / 60L;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private long now() {
        return new Date().getTime();
    }

    private void doEvaluateAnswer() {
        incrementAttempts();
        storeAnswer();
        displayNewQuestion();
    }

    private void incrementAttempts() {
        attempts++;
        TextView attemptsTV = (TextView) findViewById(R.id.attempts);
        attemptsTV.setText(Integer.toString(attempts));
    }

    private void clearAnswerForm() {
        ((EditText) findViewById(R.id.answer)).setText("");
    }

    private void storeAnswer() {
        long timeTakenInMillis = now() - timerStart;
        int multiplicand = getTextFieldAsInteger(R.id.multiplicand);
        int multiplier = getTextFieldAsInteger(R.id.multiplier);
        int answer = getTextFieldAsInteger(R.id.answer);

        if (multiplicand * multiplier == answer) {
            score++;
            TextView scoreTV = (TextView) findViewById(R.id.score);
            scoreTV.setText(Integer.toString(score));
        }

        Multiplication multiplication = new Multiplication(multiplicand, multiplier, answer, timeTakenInMillis);
        multiplicationsQueue.add(multiplication);
        multiplicationDataSource.createMultiplication(multiplication, getUsername());
    }

    private Integer getTextFieldAsInteger(int textFieldId) {
        return Integer.valueOf(((TextView) findViewById(textFieldId)).getText().toString());
    }

    private void setTextFieldFromInteger(int textFieldId, int value) {
        ((TextView) findViewById(textFieldId)).setText(intToString(value));
    }

    private void displayNewQuestion() {
        setTextFieldFromInteger(R.id.multiplicand, roll(12));
        setTextFieldFromInteger(R.id.multiplier, roll(12));
        clearAnswerForm();
        resetTimer();
    }

    private void resetTimer() {
        timerStart = now();
    }

    private String intToString(int roll) {
        return String.format("%d", roll);
    }

    private int roll(int sides) {
        return dice.nextInt(sides) + 1;
    }
}
