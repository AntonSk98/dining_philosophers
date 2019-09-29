import java.util.concurrent.Semaphore;

class Fork {
    private Semaphore semaphore = new Semaphore(1, false);
    void takeFork() throws InterruptedException {
        semaphore.acquire();
    }

    void putFork() {
        semaphore.release();
    }

    boolean isFree() {
        return semaphore.availablePermits() > 0;
    }
}
