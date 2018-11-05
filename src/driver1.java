import java.util.LinkedList;
/*
 * 
 *   5 producers 2 consumer;
 * 
 */
public class driver1 {
	
    public static void main(String[] args) throws InterruptedException {
        
    	 
         int bufferCapacity = 10;
         int produceItem = 100;

        Thread[] producerThread = new Thread[5];
        Thread[] consumerThread = new Thread[2];
        LinkedList<Integer> buffer = new LinkedList<>();
        Locks p = new Locks(buffer, producerThread, consumerThread, bufferCapacity);

        for (int i = 0; i < producerThread.length; i++) {
            producerThread[i] = new Thread(new Runnable() {
                
            	@Override
                public void run() {
                    try {
                        p.produce(produceItem);
                        
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        
        Thread bufferLive = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    p.bufferLive();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        
        
        for (int i = 0; i < consumerThread.length; i++) {
            consumerThread[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        p.consume();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        

        long timeIn = System.nanoTime();

        for (int i = 0; i < producerThread.length; i++) {
            producerThread[i].start();
        }

         bufferLive.start();

        for (int i = 0; i < consumerThread.length; i++) {
            consumerThread[i].start();
        }

        for (int i = 0; i < producerThread.length; i++) {
            producerThread[i].join();
        }

        for (int i = 0; i < consumerThread.length; i++) {
            consumerThread[i].join();
        }

       
        System.out.printf("Total Time : %.5f ms", (System.nanoTime() - timeIn) * 1e-6);
    }
}