package helpers;

public class ConsoleNotification {
    private static boolean notify;

    public static void setToShowStatistics(boolean notify) {
        ConsoleNotification.notify = notify;
    }

    public static boolean allowToShowStatistics() {
        return notify;
    }
}
