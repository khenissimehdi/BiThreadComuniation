package main.java;

public class BiThreadCom<V> {
    private V value ;
    private Object lock = new Object();
    private  boolean valueGotSet;

    public void setValue(V value) {
        synchronized (lock) {
            this.value = value;
            valueGotSet = true;
            lock.notify();
        }
    }

    public V getValue() throws InterruptedException {
        synchronized (lock) {
            while(!valueGotSet) {
                lock.wait();
            }
            return value;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var rdv = new BiThreadCom<String>();
        new Thread(
                () -> {
                    try {
                        Thread.sleep(3_000);
                        rdv.setValue("Message");
                    } catch (InterruptedException e) {
                        throw new AssertionError(e);
                    }
                })
                .start();
        System.out.println(rdv.getValue());
    }
}
