import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;

public class Config {
    public enum ConfigGrammar {
        INPUT_FILE,
        OUTPUT_FILE,
        LOG_FILE;
    }
    private HashMap<String, String> configFile = new HashMap<String, String>();
    public static final String DELIMITER = "=";
    public Config(String configFileName) throws IOException {
        Scanner scanner = new Scanner(new File(configFileName));
        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] ConfigFields = line.split(DELIMITER);
                ConfigFields[0]=ConfigFields[0].trim();
                ConfigFields[1]=ConfigFields[1].trim();


                if (ConfigFields[0].equals(ConfigGrammar.INPUT_FILE.toString())) {
                    if (ConfigFields[1]==null)
                        Log.Log("log.log", "Troubles with input file");
                    else
                        configFile.put(ConfigFields[0], ConfigFields[1]);
                } else if (ConfigFields[0].equals(ConfigGrammar.OUTPUT_FILE.toString())){
                    if (ConfigFields[1]==null)
                        Log.Log("log.log", "Troubles with output file");
                    else
                        configFile.put(ConfigFields[0], ConfigFields[1]);
                }else if (ConfigFields[0].equals(ConfigGrammar.LOG_FILE.toString())){
                    if (ConfigFields[1]==null)
                        Log.Log("log.log", "Troubles with log file");
                    else
                        configFile.put(ConfigFields[0], ConfigFields[1]);}
                else
                    Log.Log("log.log", "Unable to read config file");

            }

        }
        catch (Exception exception) {
            if (Log.mylogger == null){
                Log.Log("log.log", "Troubles with config file");
            }
        }
        finally {
            scanner.close();
        }
    }

    public String get_file_name(String name) {
        return configFile.get(name);
    }
}
