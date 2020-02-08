package tk.zgiuly.opuschat.threads;

import tk.zgiuly.opuschat.OpusServer;
import tk.zgiuly.opuschat.utils.ClientUtils;
import tk.zgiuly.opuschat.utils.MessageAnalyzer;
import tk.zgiuly.opuschat.utils.SecuredConnection;
import tk.zgiuly.opuschat.utils.SocketUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;
import java.util.StringTokenizer;

public class Client implements Runnable {
    private final Socket socket;
    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private String username;
    private PrintWriter out;
    private BufferedReader in;

    public Client(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            OpusServer.logger.info("Thread avviato!");
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            SecuredConnection securedConnection = new SecuredConnection(in, out);

            encryptCipher = securedConnection.getRsaCrypt();
            decryptCipher = securedConnection.getRsaDecrypt();

            OpusServer.logger.info("Invio la chiave asimmetrica al client");

            OpusServer.logger.info("Richiedo nickname");

            username = securedConnection.getNickname();

            if(username == null) {
                closeThread();
            }

            if(ClientUtils.clientExists(username)) {
                SocketUtils.sendMessage(out, encryptCipher, "Connessione rifiutata, client gia' esistente");
                closeThread();
                return;
            }

            Server.clients.put(username, this);

            OpusServer.logger.info("Invio messaggio di benvenuto");

            SocketUtils.sendMessage(out, encryptCipher, OpusServer.PREFIX + OpusServer.config.getString("server_login_message"));

            while (true) {
                String data = in.readLine();

                byte[] stage1 = Base64.getDecoder().decode(data.getBytes());
                byte[] stage2 = decryptCipher.doFinal(stage1, 0, 128);
                byte[] finalString = Base64.getDecoder().decode(stage2);

                StringTokenizer tokenizer = new StringTokenizer(new String(finalString));

                if (tokenizer.countTokens() == 1) {

                    String message = tokenizer.nextToken();

                    new MessageAnalyzer(message, username);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();

            Server.clients.remove(username);

            try {
                closeThread();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    public Cipher getEncryptCipher() {
        return encryptCipher;
    }

    public Cipher getDecryptCipher() {
        return decryptCipher;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUsername() {
        return username;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    private void closeThread() throws Throwable {
        socket.close();
        Thread.currentThread().interrupt();
    }
}
