package DiffieHellman;

import java.util.concurrent.CyclicBarrier;

public class KeyExchange{
    public static final Object lock = new Object();
    CyclicBarrier barrier = new CyclicBarrier(2);
    private double p = -1;
    private double g = -1;
    private double alicePreKey = -1;
    private double bobPrekey = -1;


    public synchronized void setPG(double p, double g){
        this.p = p;
        this.g = g;
    }

    public double getP(){
        return p;
    }

    public double getG(){
        return g;
    }

    public double getAlicePreKey(){
        return alicePreKey;
    }

    public double getBobPrekey(){
        return bobPrekey;
    }

    public synchronized boolean pgIsNull(){
        return p == -1 && g == -1;
    }

    public synchronized boolean aKeyNotSet(){
        return alicePreKey == -1;
    }

    public synchronized boolean bKeyNotSet(){
        return bobPrekey == -1;
    }

    public synchronized void setAlicePreKey(double key){
        alicePreKey = key;
    }

    public synchronized void setBobPreKey(double key){
        bobPrekey = key;
    }


}
