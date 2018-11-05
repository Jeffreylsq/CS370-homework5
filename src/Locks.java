import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class Locks {
	
	
	private final int Capacity;
	private Lock produceLock  = new ReentrantLock(); // one lock for producer
    private Lock consumeLock = new ReentrantLock(); // one lock for consumer
    private LinkedList<Integer> buffer;
    private Thread[] pThread;
    private Thread[] cThread;
    

    public Locks(LinkedList<Integer> buffer, Thread[] pThread, Thread[] cThread, int Capacity) {
        this.buffer = buffer;
        this.pThread = pThread;
        this.cThread = cThread;
        this.Capacity = Capacity;
    }
  
    
    public void consume() throws InterruptedException {
        while (buffer.size() > 0 || IfProducing()) {

            // Wait until the buffer is empty
        	consumeLock.lock();

            while (buffer.size() == 0) {
                Thread.sleep(1);
            }

            // Consume first item in buffer
            int item = buffer.removeFirst();
            
            System.out.println(Thread.currentThread().getName() + " consumed No.-" + item);
            
            consumeLock.unlock();

            // Sleep 1 second after consumption
            Thread.sleep(1000);
        }
    }

    public void produce(int numItems) throws InterruptedException {
        for (int item = 0; item < numItems; item++) {

            // Wait until the buffer is not full
        	produceLock.lock();

            synchronized (this) {
                // Wait for the buffer to request items
                wait();
                // Produce item
                buffer.add(item);
                System.out.println(Thread.currentThread().getName() + " produced No.-" + item);
            }

            produceLock.unlock();
        }
    }

    
    private boolean IfProducing() {
        boolean produce = false;
        for (int i = 0; i < pThread.length; i++) {
            if (pThread[i].isAlive()) {
                produce = true;
            }
        }

        return produce;
    }
    
    
    
    public void bufferLive() throws InterruptedException {
    	
        while (IfProducing()) {
            synchronized (this) {
            	    
                    
                if (pThread.length >= cThread.length)  // Producers more than Consumer
            {
                  
                      while(buffer.size() == Capacity) 
                      {
                        Thread.sleep(1500 * cThread.length);
                      }
                    
            }   
                    
                else {                                  //Consumer more than Producers
                    
                    while (buffer.size() == Capacity) {
                        Thread.sleep(cThread.length);
                    }
                }   
                    
                    notify();
            }
        }
    }

   
}