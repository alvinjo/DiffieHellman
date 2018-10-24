package DiffieHellman;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

public class AliceBob implements Runnable{
    KeyExchange exchange;
    int secretNum;
    double finalKey;
    int type = 1;
    String ID;
    PrimRootFinder prf = new PrimRootFinder();


    public AliceBob(String ID, KeyExchange exchange){
        this.exchange = exchange;
        this.ID = ID;
    }


    public void setValues(){
        synchronized (KeyExchange.lock){
            if(exchange.pgIsNull()){
                System.out.println(ID + " is setting p and g values");

                int p = generatePrime();
                System.out.println("prime " + p);
                ArrayList<Integer> primRoots = new PrimRootFinder().getPrimitiveRoots(p);
                System.out.println("primroot size " + primRoots.size());
                int g = primRoots.get(new Random().nextInt(primRoots.size()));

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

            exchange.setAlicePreKey(prf.performModPow(exchange.getG(), secretNum, exchange.getP()).intValue());



            System.out.println("Alice prekey:" + exchange.getAlicePreKey());
        }else{
            System.out.println(exchange.getG() + "^" + secretNum + " % " + exchange.getP());

            exchange.setBobPreKey(prf.performModPow(exchange.getG(), secretNum, exchange.getP()).intValue());

            System.out.println("Bob prekey:" + exchange.getBobPrekey());
        }
    }


    public double computeFinalKey(){
        if(type == 0){
            System.out.println(ID + " bPreKey:" + exchange.getBobPrekey() + " secretnum:" + secretNum + " p:" + exchange.getP());
            System.out.println(ID + " finalkey: " + exchange.getBobPrekey() + "^" + secretNum + " % " + exchange.getP());
            return prf.performModPow(exchange.getBobPrekey(), secretNum, exchange.getP()).intValue();

        }else{
            System.out.println(ID + " aPreKey:" + exchange.getAlicePreKey() + " secretnum:" + secretNum + " p:" + exchange.getP());
            System.out.println(ID + " finalkey: " + exchange.getAlicePreKey() + "^" + secretNum + " % " + exchange.getP());
            return prf.performModPow(exchange.getAlicePreKey(), secretNum, exchange.getP()).intValue();
        }
    }


    public int generatePrime(){
        Random random = new Random();
        int prime = 6;
        while(!isPrime(prime)){
            prime = random.nextInt(1000000)+3;
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

