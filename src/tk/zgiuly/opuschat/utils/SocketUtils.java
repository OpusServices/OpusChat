package tk.zgiuly.opuschat.utils;

import tk.zgiuly.opuschat.threads.Server;

import javax.crypto.Cipher;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class SocketUtils {
    public static void sendMessage(PrintWriter out, Cipher cipher, String message) throws Throwable {
        byte[] data = Base64.getEncoder().encode(message.getBytes());
        byte[] crypt = cipher.doFinal(data);
        byte[] finalCrypt = Base64.getEncoder().encode(crypt);

        out.println(new String(finalCrypt));
        out.flush();
    }

    public static void forwardMessage(String message, String baseNickname) {
        Server.clients.forEach((username, client) -> {
            try {
                if(client.getSocket().isClosed()) {
                    Server.clients.remove(username);
                    return;
                }

                if(username.equals(baseNickname)) return;

                String completeMessage = baseNickname + ": " + message;

                sendMessage(client.getOut(), client.getEncryptCipher(), completeMessage);
            }catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }
}
