package core.basesyntax;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MyTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 10;
    private int startPoint;
    private int finishPoint;

    public MyTask(int startPoint, int finishPoint) {
        this.startPoint = startPoint;
        this.finishPoint = finishPoint;
    }

    @Override
    protected Long compute() {
        if (finishPoint - startPoint <= THRESHOLD) {
            long sum = 0;
            for (int i = startPoint; i < finishPoint; i++) {
                sum += i;
            }
            return sum;
        } else {
            int midPoint = (startPoint + finishPoint) / 2;
            MyTask leftTask = new MyTask(startPoint, midPoint);
            MyTask rightTask = new MyTask(midPoint, finishPoint);

            leftTask.fork();
            rightTask.fork();

            long leftResult = leftTask.join();
            long rightResult = rightTask.join();

            return leftResult + rightResult;
        }
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        MyTask task = new MyTask(0, 100);
        long result = pool.invoke(task);
        System.out.println("Sum: " + result);
    }
}

