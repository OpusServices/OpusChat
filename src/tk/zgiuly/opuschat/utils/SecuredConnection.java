package tk.zgiuly.opuschat.utils;

import tk.zgiuly.opuschat.OpusServer;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.StringTokenizer;

public class SecuredConnection {
    private PublicKey clientPublicKey;
    private final BufferedReader in;
    private final PrintWriter out;
    private Cipher rsaCrypt;
    private Cipher rsaDecrypt;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public SecuredConnection(BufferedReader in, PrintWriter out) throws Throwable {
        this.in = in;
        this.out = out;
        sendPublicKey();
        getPublicKeyFromClient();
    }

    private void sendPublicKey() throws Throwable {
        generateKey();
    }

    private void generateKey() throws Throwable {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair pair = generator.generateKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();

        rsaDecrypt = Cipher.getInstance("RSA");
        rsaDecrypt.init(Cipher.DECRYPT_MODE, privateKey);

        out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        out.flush();
    }

    public String getNickname() throws Throwable {
        String data = in.readLine();

        byte[] stage1 = Base64.getDecoder().decode(data.getBytes());
        byte[] stage2 = rsaDecrypt.doFinal(stage1, 0, 128);
        byte[] finalString = Base64.getDecoder().decode(stage2);

        StringTokenizer tokenizer = new StringTokenizer(new String(finalString));

        if (tokenizer.countTokens() != 1) {
            OpusServer.logger.warning("Disconnetto il client per mancanza di nickname");
            SocketUtils.sendMessage(out, rsaCrypt, "Connessione rifiutata!");
            return null;
        }
        return tokenizer.nextToken();
    }

    private void getPublicKeyFromClient() throws Throwable {
        String data = in.readLine();

        byte[] decoded = Base64.getDecoder().decode(data.getBytes());

        KeyFactory factory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);

        this.clientPublicKey = factory.generatePublic(keySpec);
        rsaCrypt = Cipher.getInstance("RSA");
        rsaCrypt.init(Cipher.ENCRYPT_MODE, this.clientPublicKey);
    }

    public Cipher getRsaDecrypt() {
        return rsaDecrypt;
    }

    public Cipher getRsaCrypt() {
        return rsaCrypt;
    }
}
