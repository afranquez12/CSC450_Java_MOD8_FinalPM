package Final_PM;


//Import required packages
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* Purpose:
* This program demonstrates Java concurrency concepts using two threads.
* Thread 1 counts up to 20, while Thread 2 waits for Thread 1 to finish
* and then counts down from 20 to 0. The program uses ReentrantLock and
* Condition to synchronize the threads, ensuring thread-safe execution.
*/
public class Mod8 {
private static final Lock lock = new ReentrantLock(); // Lock for thread synchronization
private static final Condition condition = lock.newCondition(); // Condition for signaling
private static boolean threadOneFinished = false; // Flag to indicate when Thread 1 finishes

// Function for Thread One: Count up to 20
public static void countUp() {
   lock.lock(); // Acquire the lock
   try {
       for (int i = 0; i <= 20; i++) {
           System.out.println("Thread 1 - Count Up: " + i);
           Thread.sleep(100); // Simulate work
       }
       threadOneFinished = true; // Signal that Thread One is finished
       condition.signal(); // Notify Thread Two to proceed
   } catch (InterruptedException e) {
       Thread.currentThread().interrupt(); // Restore interrupted status
       System.err.println("Thread 1 interrupted: " + e.getMessage());
   } finally {
       lock.unlock(); // Release the lock
   }
}

// Function for Thread Two: Count down from 20 to 0
public static void countDown() {
   lock.lock(); // Acquire the lock
   try {
       while (!threadOneFinished) {
           condition.await(); // Wait for Thread One to finish
       }
       for (int i = 20; i >= 0; i--) {
           System.out.println("Thread 2 - Count Down: " + i);
           Thread.sleep(100); // Simulate work
       }
   } catch (InterruptedException e) {
       Thread.currentThread().interrupt(); // Restore interrupted status
       System.err.println("Thread 2 interrupted: " + e.getMessage());
   } finally {
       lock.unlock(); // Release the lock
   }
}

public static void main(String[] args) {
   // Create two threads
   Thread thread1 = new Thread(Mod8::countUp);
   Thread thread2 = new Thread(Mod8::countDown);

   // Start the threads
   thread1.start();
   thread2.start();

   // Wait for threads to finish
   try {
       thread1.join();
       thread2.join();
   } catch (InterruptedException e) {
       Thread.currentThread().interrupt(); // Restore interrupted status
       System.err.println("Main thread interrupted: " + e.getMessage());
   }
}
}
