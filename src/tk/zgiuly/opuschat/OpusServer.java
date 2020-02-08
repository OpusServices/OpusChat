package tk.zgiuly.opuschat;

import tk.zgiuly.opuschat.io.Config;
import tk.zgiuly.opuschat.threads.Server;
import tk.zgiuly.opuschat.utils.ServerBuild;

import java.util.logging.Level;
import java.util.logging.Logger;

public class OpusServer {
    public static final String ROOT = System.getProperty("user.dir");
    public static final Logger logger = Logger.getLogger(OpusServer.class.getName());
    public static final Config config = new Config();
    public static final String PREFIX = "Server: ";

    public static void main(String[] args) {
        logger.setLevel(Level.ALL);
        logger.info("Avvio server");

        try {
            new Server(ServerBuild.buildModule()).run();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
