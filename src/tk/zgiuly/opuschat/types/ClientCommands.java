package tk.zgiuly.opuschat.types;

public enum ClientCommands {
    MSG("/msg"),
    QUIT("/quit");

    public final String command;

    ClientCommands(String command) {
        this.command = command;
    }
}
