package DiffieHellman;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

public class AliceBob implements Runnable{
    KeyExchange exchange;
    int secretNum;
    double finalKey;
    int type = 1;
    String ID;


    public AliceBob(String ID, KeyExchange exchange){
        this.exchange = exchange;
        this.ID = ID;
    }


    public void setValues(){
        synchronized (KeyExchange.lock){
            if(exchange.pgIsNull()){
                System.out.println(ID + " is setting p and g values");

                int q = generatePrime();
                int p=6;
                while(!isPrime(p)){
                    p = q * new Random().nextInt(10) + 1;
                }
                double g = new Random().nextInt(10)%p;

                exchange.setPG(p, g);
                type = 0; //ALICE
                System.out.println("p: " + exchange.getP() + " g:" + exchange.getG());
            }
        }
    }


    @Override
    public void run() {
//        System.out.println(ID + " #setting values");
        setValues();
//        System.out.println(ID + " #waiting");
        await();
        System.out.println(ID + " #computing prekey");
        computePreKey();
//        System.out.println(ID + " #waiting");
        await();
        System.out.println(ID + " #computing finalkey");
        finalKey = computeFinalKey();
        await();
        System.out.println(ID + " finalKey:" + finalKey);
    }

    public void await(){
        try{
            exchange.barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }


    public void computePreKey(){
        secretNum = new Random().nextInt(50);
        System.out.println(ID + " secretnum:" + secretNum);
        if(type == 0){
            System.out.println(exchange.getG() + "^" + secretNum + " %" + exchange.getP());
            exchange.setAlicePreKey(Math.pow(exchange.getG(),secretNum)%exchange.getP());
            System.out.println("Alice prekey:" + exchange.getAlicePreKey());
        }else{
            System.out.println(exchange.getG() + "^" + secretNum + " %" + exchange.getP());
            exchange.setBobPreKey(Math.pow(exchange.getG(), secretNum)%exchange.getP());
            System.out.println("Bob prekey:" + exchange.getBobPrekey());
        }
    }


    public double computeFinalKey(){
        if(type == 0){
            System.out.println(ID + " bPreKey:" + exchange.getBobPrekey() + " secretnum:" + secretNum + " p:" + exchange.getP());
            return Math.pow(exchange.getBobPrekey(), secretNum)%exchange.getP();
        }else{
            System.out.println(ID + " aPreKey:" + exchange.getAlicePreKey() + " secretnum:" + secretNum + " p:" + exchange.getP());
            return Math.pow(exchange.getAlicePreKey(), secretNum)%exchange.getP();
        }
    }


    public int generatePrime(){
        Random random = new Random();
        int prime = 6;
        while(!isPrime(prime)){
            prime = random.nextInt(50);
        }
        return prime;
    }

    public boolean isPrime(int x){
        if(x<2){return false;}
        for (int i = 2; i <= x/2; i++) {
            if(x%i == 0){ return false; }
        }
        return true;
    }
    
}
