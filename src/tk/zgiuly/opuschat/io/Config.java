package tk.zgiuly.opuschat.io;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tk.zgiuly.opuschat.OpusServer;

import java.io.*;

public class Config {
    private JSONParser parser;
    private JSONObject object;
    private BufferedReader reader;
    private File configFile;

    public Config() {
        try {
            setupVariables();
            checks();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupVariables() throws IOException {
        parser = new JSONParser();
        configFile = new File(OpusServer.ROOT, "/server_config.json");
    }

    private void checks() throws IOException, ParseException {

        if(!configFile.exists()) generateConfig();
        reader = new BufferedReader(new FileReader(configFile));

        object = (JSONObject) parser.parse(reader);
        if (object == null) throw new NullPointerException("Config non valida");
    }


    private void generateConfig() throws IOException {
        configFile.createNewFile();
        object = new JSONObject();
        object.put("server_ip", "127.0.0.1");
        object.put("server_name", "Default Server");
        object.put("server_port", "8080");
        object.put("server_password", "DefaultPassword");
        object.put("server_login_message", "Default login message");
        BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
        writer.write(object.toJSONString());
        writer.flush();
        writer.close();
    }

    public String getString(String key) {
        return (String)object.get(key);
    }

    public int getInteger(String key) {
        return Integer.parseInt((String)object.get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.getBoolean((String)object.get(key));
    }
}
