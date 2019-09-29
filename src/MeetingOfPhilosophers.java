import helpers.ConsoleNotification;

class MeetingOfPhilosophers {
    public static void main(String[] args) {
        int numberOfPhilosophers = Integer.parseInt(args[0]);
        int meetingTimeInSeconds = Integer.parseInt(args[1]);
        int maxTimeEating = Integer.parseInt(args[2]);
        int maxThinkingTime = Integer.parseInt(args[3]);
        int showConsoleStatus = Integer.parseInt(args[4]);

        ConsoleNotification.setToShowStatistics(showConsole(showConsoleStatus));
        Meeting meeting = new Meeting(meetingTimeInSeconds);
        meeting.organizeMeeting(numberOfPhilosophers, maxTimeEating, maxThinkingTime);
        meeting.finishMeeting();


    }

    private static boolean showConsole(int showConsoleStatus) {
        return showConsoleStatus > 0;
    }

}
