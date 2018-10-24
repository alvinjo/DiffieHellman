package DiffieHellman;

import java.util.concurrent.CyclicBarrier;

public class KeyExchange{
    public static final Object lock = new Object();
    CyclicBarrier barrier = new CyclicBarrier(2);
    private int p = -1;
    private int g = -1;
    private int alicePreKey = -1;
    private int bobPrekey = -1;


    public synchronized void setPG(int p, int g){
        this.p = p;
        this.g = g;
    }

    public int getP(){
        return p;
    }

    public int getG(){
        return g;
    }

    public int getAlicePreKey(){
        return alicePreKey;
    }

    public int getBobPrekey(){
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

    public synchronized void setAlicePreKey(int key){
        alicePreKey = key;
    }

    public synchronized void setBobPreKey(int key){
        bobPrekey = key;
    }


}

