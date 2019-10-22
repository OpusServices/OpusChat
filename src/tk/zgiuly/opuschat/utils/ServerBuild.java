package tk.zgiuly.opuschat.utils;

import tk.zgiuly.opuschat.OpusServer;
import tk.zgiuly.opuschat.io.Config;
import tk.zgiuly.opuschat.types.ServerModule;

public class ServerBuild {
    public static ServerModule buildModule() {
        Config config = OpusServer.config;
        ServerModule serverModule = new ServerModule(
          config.getString("server_ip"),
          config.getString("server_name"),
          config.getInteger("server_port"),
          config.getString("server_password"),
          config.getString("server_login_message")
        );
        return serverModule;
    }
}
