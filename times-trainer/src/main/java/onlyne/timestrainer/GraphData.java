package onlyne.timestrainer;

import android.support.annotation.NonNull;

import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

class GraphData {
    public enum DataType {
        QUESTIONS,
        CORRECT,
        PERCENTAGE
    }
//    public static final String QUESTIONS = "questions";
//    public static final String CORRECT = "correct";
    private List<Integer> questionsPerNumber;
    private List<Integer> correctAnswersPerNumber;

    public GraphData() {
        questionsPerNumber = new ArrayList<>(13);
        correctAnswersPerNumber = new ArrayList<>(13);
        initArray(questionsPerNumber);
        initArray(correctAnswersPerNumber);
    }

    private void initArray(List<Integer> list) {
        for (int index = 0; index < 13; index++) {
            list.add(index, 0);
        }
    }

    public GraphData tally(Queue<Multiplication> allMultiplications) {
        for (Multiplication multiplication : allMultiplications) {
            tally(multiplication);
        }
        return this;
    }

    private void tally(Multiplication multiplication) {
        questionsPerNumber.set(multiplication.multiplicand, questionsPerNumber.get(multiplication.multiplicand) + 1);
        questionsPerNumber.set(multiplication.multiplier, questionsPerNumber.get(multiplication.multiplier) + 1);

        if (multiplication.multiplicand * multiplication.multiplier == multiplication.answer) {
            correctAnswersPerNumber.set(multiplication.multiplicand, correctAnswersPerNumber.get(multiplication.multiplicand) + 1);
            correctAnswersPerNumber.set(multiplication.multiplier, correctAnswersPerNumber.get(multiplication.multiplier) + 1);
        }
    }

    public BarGraphSeries<DataPoint> asBarGraph(DataType type) {
        return new BarGraphSeries<>(getDataPoints(type));
    }

    public LineGraphSeries<DataPoint> asLineGraph(DataType type) {
        return new LineGraphSeries<>(getDataPoints(type));
    }

    @NonNull
    private DataPoint[] getDataPoints(DataType type) {
        List<Integer> data = new ArrayList<>();
        switch (type) {
            case QUESTIONS:
                data = questionsPerNumber;
                break;
            case CORRECT:
                data = correctAnswersPerNumber;
                break;
            case PERCENTAGE:
                data = getPercentiles();
                break;
        }

        DataPoint[] dataPoints = new DataPoint[12];

        for (int index = 1; index < 13; index++) {
            dataPoints[index - 1] = new DataPoint(index, data.get(index));
        }
        return dataPoints;
    }

    private List<Integer> getPercentiles() {
        List<Integer> data = new ArrayList<>(13);
        initArray(data);
        List<Integer> numerator = correctAnswersPerNumber;
        List<Integer> denominator = questionsPerNumber;
        for (int index = 1; index < 13; index++) {
            if (denominator.get(index) > 0) data.set(index, (numerator.get(index) * 100) / denominator.get(index));
            else data.set(index, 0);
        }
        return data;
    }
}
