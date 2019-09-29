/* The short description of the behaviour of a philosopher
        First: The initial state of a philosopher is being hungry
        Secondly: A philosopher tries to take his left fork. If this attempt fails then the philosopher keeps being hungry
        Thirdly: A philosopher tries to take his second fork. If this attempt fails then the philosopher puts down his first fork
            and goes on being hungry
        After that: A philosopher is eating
        After that: A philosopher puts his first as well as his second fork
        After that: A philosopher begins to think
        Finally: A philosopher again wants to eat. The loop starts again.
    */
import helpers.Logger;

import java.util.Random;

public class Philosopher extends Thread {
    private int number;
    private final Fork leftFork;
    private final Fork rightFork;
    private int maxEatingTime;
    private int maxThinkingTime;
    private boolean stopMeeting;

    //variables needed for the statistics
    private float thinkingTime = 0;
    private float eatingTime = 0;
    private int numberOfMeals = 0;

    Philosopher(int number, Fork leftFork, Fork rightFork, int maxEatingTime, int maxThinkingTime) {
        this.number = number;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.maxEatingTime = maxEatingTime;
        this.maxThinkingTime = maxThinkingTime;
    }

    void stopMeeting() {
        this.stopMeeting = true;
    }

    @Override
    public void run() {
        while (!stopMeeting) {
            beHungry();
            try {
                if (!canTakeForks())
                    continue;
                beingEating();
                putLeftFork();
                putRightFork();
                beingThinking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean canTakeForks() throws InterruptedException {
        synchronized (leftFork) {
            if (leftFork.isFree()) {
                takeLeftFork();
            } else
                return false;
        }
        synchronized (rightFork) {
            if (rightFork.isFree()) {
                takeRightFork();
            } else {
                putLeftFork();
                return false;
            }
        }
        return true;
    }

    private void beHungry() {
        Logger.log("Philosopher N: "+ number +" is hungry");
    }

    private void takeLeftFork() throws InterruptedException {
        leftFork.takeFork();
        Logger.log("Philosopher N: "+ number +" took left fork");
    }

    private void takeRightFork() throws InterruptedException {
        rightFork.takeFork();
        Logger.log("Philosopher N: "+ number +" took right fork");
    }

    private void putLeftFork() {
        leftFork.putFork();
        Logger.log("Philosopher N: "+ number +" put left fork");
    }

    private void putRightFork() {
        rightFork.putFork();
        Logger.log("Philosopher N: "+ number +" put right fork");
    }

    private void beingThinking() throws InterruptedException {
        int periodOfThinking = new Random().nextInt(maxThinkingTime + 1);
        Thread.sleep(periodOfThinking);
        thinkingTime += periodOfThinking;
        Logger.log("Philosopher N: "+ number +" is thinking");
    }

    private void beingEating() throws InterruptedException {
        int periodOfEating = new Random().nextInt(maxEatingTime + 1);
        Thread.sleep(periodOfEating);
        eatingTime += periodOfEating;
        numberOfMeals ++;
        Logger.log("Philosopher N: "+ number +" is eating");
    }

    float getThinkingTime() {
        return thinkingTime;
    }

    float getEatingTime() {
        return eatingTime;
    }

    int getNumberOfMeals() {
        return numberOfMeals;
    }

    int getNumber() {
        return number;
    }
}
