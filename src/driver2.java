import java.util.LinkedList;



public class driver2 {
    public static void main(String[] args) throws InterruptedException {
        
        int bufferCapacity = 10;
        int produceItem = 100;

        Thread[] producerThread = new Thread[2];
        Thread[] consumerThread = new Thread[5];
        LinkedList<Integer> buffer = new LinkedList<>();
        Locks p = new Locks(buffer, producerThread, consumerThread, bufferCapacity);

        
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

        long timeOut = System.nanoTime() - timeIn;
        System.out.printf("Time: %.5f ms", timeOut * 1e-6);
    }
}