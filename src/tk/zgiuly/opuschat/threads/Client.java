package tk.zgiuly.opuschat.threads;

import tk.zgiuly.opuschat.OpusServer;

import java.net.Socket;

public class Client implements Runnable {
    private final Socket socket;

    public Client(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        OpusServer.logger.info("Thread avviato!");
    }
}
