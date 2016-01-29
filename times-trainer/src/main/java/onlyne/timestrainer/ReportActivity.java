package onlyne.timestrainer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import onlyne.timestrainer.db.MultiplicationDataSource;

/**
 * http://www.android-graphview.org/
 */

public class ReportActivity extends AbstractTimesTrainerActivity {

    private MultiplicationDataSource multiplicationDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_frame);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        multiplicationDataSource = new MultiplicationDataSource(this);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Percentage correct answers by number");
        graph.setY(200);
        Viewport viewport = graph.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMaxX(12);

        setUsername(getIntent().getExtras().getString(LoginScreenActivity.USERNAME));

        GraphData graphData = new GraphData().tally(multiplicationDataSource.getAllMultiplications(getUsername()));

        LineGraphSeries<DataPoint> questions = graphData.asLineGraph(GraphData.DataType.QUESTIONS);
        questions.setColor(Color.RED);

        BarGraphSeries<DataPoint> correct = graphData.asBarGraph(GraphData.DataType.CORRECT);
        correct.setColor(Color.GREEN);
        correct.setDrawValuesOnTop(true);
        correct.setValuesOnTopColor(Color.BLUE);
        correct.setSpacing(20);

        BarGraphSeries<DataPoint> percentage = graphData.asBarGraph(GraphData.DataType.PERCENTAGE);
        percentage.setColor(Color.MAGENTA);
        percentage.setDrawValuesOnTop(true);
        percentage.setValuesOnTopColor(Color.BLUE);
        percentage.setSpacing(20);
        viewport.setYAxisBoundsManual(true);
        viewport.setMaxY(100);
        viewport.setXAxisBoundsStatus(Viewport.AxisBoundsStatus.FIX);

        graph.addSeries(percentage);
    }

    private List<Multiplication> getData() {
        List<Multiplication> data = new LinkedList<>();
        data.add(new Multiplication(3, 4, 12, 3444));
        data.add(new Multiplication(2, 1, 2, 2476));
        data.add(new Multiplication(5, 7, 35, 8437));
        data.add(new Multiplication(4, 4, 16, 2123));
        data.add(new Multiplication(3, 6, 17, 1897));
        data.add(new Multiplication(1, 1, 1, 123));
        data.add(new Multiplication(10, 9, 80, 7823));
        data.add(new Multiplication(1, 2, 2, 456));
        data.add(new Multiplication(3, 11, 33, 8074));
        data.add(new Multiplication(6, 7, 32, 9845));
        data.add(new Multiplication(7, 8, 54, 23984));
        data.add(new Multiplication(8, 4, 32, 8324));
        data.add(new Multiplication(2, 2, 4, 1234));
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}