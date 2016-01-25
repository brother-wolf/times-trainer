package onlyne.timestrainer;

public class Multiplication {
    public final int multiplicand, multiplier, answer;
    public final long timeTakenInMillis;

    public Multiplication(int multiplicand, int multiplier, int answer, long timeTakenInMillis) {
        this.multiplicand = multiplicand;
        this.multiplier = multiplier;
        this.answer = answer;
        this.timeTakenInMillis = timeTakenInMillis;
    }
}
