package HomeExam.scr.Main;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class OnlineClient {
    private final String ipAddress;
    private final static int secondsToInterruptWithNope = 5;

    // Connect to server
    public OnlineClient(String ipAddress) throws Exception {
        this.ipAddress = ipAddress;
        System.out.println("Client started");

        Socket connection = new Socket(ipAddress, 2048);

        ObjectOutputStream outToServer = new ObjectOutputStream(connection.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(connection.getInputStream());
        // Send message to server
        ExecutorService threadpool = Executors.newFixedThreadPool(1);

        Runnable receive = new Runnable() {
            @Override
            public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    try {
                        String nextMessage = (String) inFromServer.readObject();
                        System.out.println(nextMessage);
                        if (nextMessage.contains("options") || nextMessage.contains("Give")
                                || nextMessage.contains("Where")) { // options (your turn), Give (Opponent played
                                                                    // Favor), Where (You defused an exploding kitten)
                            outToServer.writeObject(br.readLine());
                        } else if (nextMessage.contains("<Enter>")) { // Press <Enter> to play Nope and Interrupt
                            int millisecondsWaited = 0;
                            while (!br.ready() && millisecondsWaited < (secondsToInterruptWithNope * 1000)) {
                                Thread.sleep(200);
                                millisecondsWaited += 200;
                            }
                            if (br.ready()) {
                                outToServer.writeObject(br.readLine());
                            } else
                                outToServer.writeObject(" ");
                        }
                    } catch (Exception e) {
                        System.exit(0);
                    }
                }
            }
        };

        threadpool.execute(receive);
    }

}
