package tk.zgiuly.opuschat.utils;

import tk.zgiuly.opuschat.threads.Client;
import tk.zgiuly.opuschat.threads.Server;

public class ClientUtils {
    public static boolean clientExists(String name) {
        return Server.clients.containsKey(name);
    }

    public static Client getClient(String name) {
        if(!clientExists(name)) return null;
        return Server.clients.get(name);
    }
}
