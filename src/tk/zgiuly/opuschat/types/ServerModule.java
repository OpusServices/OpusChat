package tk.zgiuly.opuschat.types;

public class ServerModule {
    private final String server_ip;
    private final String server_name;
    private final int server_port;
    private final String server_password;
    private final String server_login_message;

    public ServerModule(String server_ip, String server_name, int server_port, String server_password, String server_login_message) {
        this.server_ip = server_ip;
        this.server_name = server_name;
        this.server_port = server_port;
        this.server_password = server_password;
        this.server_login_message = server_login_message;
    }


    public String getServer_ip() {
        return server_ip;
    }

    public String getServer_name() {
        return server_name;
    }

    public int getServer_port() {
        return server_port;
    }

    public String getServer_password() {
        return server_password;
    }

    public String getServer_login_message() {
        return server_login_message;
    }

    @Override
    public String toString() {
        return server_ip + ":" + server_port;
    }
}
