package tk.zgiuly.opuschat.threads;

import tk.zgiuly.opuschat.OpusServer;
import tk.zgiuly.opuschat.types.ServerModule;

import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static final Map<String, Socket> socketNames = new HashMap<>();
    private final ServerModule serverModule;

    public Server(ServerModule serverModule) {
        this.serverModule = serverModule;
    }

    public void run() throws Throwable {
        ServerSocket serverSocket = new ServerSocket(
                serverModule.getServer_port(),
                0,
                Inet4Address.getByName(serverModule.getServer_ip())
        );
        OpusServer.logger.info("Server avviato sull'ip " + serverModule.toString());
        while(true) {
            Socket socket = serverSocket.accept();
            Thread t1 = new Thread(new Client(socket));
            t1.start();
        }
    }
}
