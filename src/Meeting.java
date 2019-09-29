import helpers.ConsoleNotification;
import helpers.Logger;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;
import static java.util.Arrays.stream;


class Meeting {
    private int numberOfPhilosophers;
    private int meetingTimeInSeconds;
    private int maxTimeEating;
    private int maxTimeThinking;

    private Philosopher[] philosophers;
    private Fork[] forks;

    Meeting(int meetingTimeInSeconds) {
        this.meetingTimeInSeconds = meetingTimeInSeconds;
    }

    void organizeMeeting(int numberOfPhilosophers, int maxTimeEating, int maxTimeThinking) {
        this.numberOfPhilosophers = numberOfPhilosophers;
        this.maxTimeEating = maxTimeEating;
        this.maxTimeThinking = maxTimeThinking;
        philosophers = new Philosopher[numberOfPhilosophers];
        forks = new Fork[numberOfPhilosophers];
        this.serveForks();
        this.initPhilosophers();

    }

    private void serveForks() {
        for (int i = 0; i<numberOfPhilosophers; i++) {
            forks[i] = new Fork();
        }
    }
    //in order to avoid the situation of a deadlock the last philosopher takes forks in the reversed order
    private void initPhilosophers() {
        if (numberOfPhilosophers > 1) {
            for (int i = 0; i<numberOfPhilosophers; i++) {
                Fork leftFork = forks[i];
                Fork rightFork = forks[(i+1)%forks.length];
                if (i == philosophers.length - 1) {
                    philosophers[i] = new Philosopher(philosophers.length - 1, rightFork, leftFork, maxTimeEating,maxTimeThinking);
                } else {
                    philosophers[i] = new Philosopher(i, leftFork, rightFork, maxTimeEating,maxTimeThinking);
                }
                new Thread(philosophers[i], "Philosopher " + (i + 1)).start();
            }
        } else Logger.log("One philosopher brought with him only one fork! Such a pity, but he is unable to eat with one fork!");
    }
    //when the time of meeting is up then the program starts to notify philosophers that it is time to go home)
    void finishMeeting() {
        if (numberOfPhilosophers > 1) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (numberOfPhilosophers > 1)
                        for (Philosopher philosopher : philosophers) {
                            philosopher.stopMeeting();
                        }
                    if(stream(philosophers).noneMatch(Thread::isAlive)) {
                        if (ConsoleNotification.allowToShowStatistics()){
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            showStatistics();
                        }
                    }
                }
            }, meetingTimeInSeconds*1000L);
        }
    }

    private void showStatistics() {
        Logger.log("");
        Logger.log("________________________Statistics_______________________");
        for (Philosopher philosopher: philosophers) {
            float timeOfStarvation = meetingTimeInSeconds - (philosopher.getThinkingTime()/1000) - (philosopher.getEatingTime()/1000);
            System.out.print("Philosopher id [" +philosopher.getNumber()+ "]; ");
            System.out.print("Count of meals: "+philosopher.getNumberOfMeals());
            System.out.print("; Time of starvation: "+ timeOfStarvation);
            System.out.println("; Percentage of starvation: "+timeOfStarvation/meetingTimeInSeconds*100+"%"+";");
        }
    }
}
