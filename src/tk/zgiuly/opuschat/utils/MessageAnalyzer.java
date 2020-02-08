package tk.zgiuly.opuschat.utils;

import tk.zgiuly.opuschat.threads.Client;
import tk.zgiuly.opuschat.threads.Server;
import tk.zgiuly.opuschat.types.ClientCommands;

import java.util.StringTokenizer;

public class MessageAnalyzer {
    private String message;
    private final String baseName;

    public MessageAnalyzer(String message, String baseName) throws Throwable {
        this.message = message;
        this.baseName = baseName;
        analyze();
    }

    private void analyze() throws Throwable {
        boolean isCommand = false;
        ClientCommands commands = null;
        for (ClientCommands value : ClientCommands.values()) {
            if(message.startsWith(value.command)) {
                isCommand = true;
                commands = value;
                message.replaceFirst(value.command, "");
                break;
            }
        }
        if(!isCommand)  {
            forward();
            return;
        }
        dispatch(commands);
    }

    private void forward() {
        SocketUtils.forwardMessage(message, baseName);
    }

    private void dispatch(ClientCommands commands) throws Throwable {
        StringTokenizer tokenizer = new StringTokenizer(message);

        if(commands == ClientCommands.MSG) {
            if(tokenizer.countTokens() != 2) return;

            String target = tokenizer.nextToken();
            String msg = tokenizer.nextToken();

            Client client = ClientUtils.getClient(target);
            if(client == null) return;

            if(target.equals(baseName)) return;

            SocketUtils.sendMessage(client.getOut(), client.getEncryptCipher(), baseName + ": " + msg);
        } else if(commands == ClientCommands.QUIT) {
            Client client1 = ClientUtils.getClient(baseName);
            if(client1 == null) return;
            client1.getSocket().close();
            Server.clients.remove(baseName);
        }
     }
}
