package org.fuelsyourcyb.exathunk;

public class Client {
    private final String id;

    public Client(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void run() {
        System.out.println("Client "+id);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Need an id");
            System.exit(-1);
        } else {
            String id = args[0];
            Client client = new Client(id);
            client.run();
        }
    }
}