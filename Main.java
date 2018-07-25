package DiffieHellman;

public class Main {

    public static void main(String[] args) {
        KeyExchange exchange = new KeyExchange();

        AliceBob one = new AliceBob("one", exchange);
        AliceBob two = new AliceBob("two", exchange);

        Thread t1 = new Thread(one);
        Thread t2 = new Thread(two);
        t1.start();
        t2.start();
    }
}
