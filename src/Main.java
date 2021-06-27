public class Main {
    public static void main(String[] args) {
        if (args.length != 1){
            Log.Log("log.log", "Enter 1 configuration file");
            return;
        }
        try {

            Config config = new Config(args[0]);
            Solve.Solve(config);

        } catch (Exception exception) {
            if (Log.mylogger == null){
                Log.Log("log.log", exception.toString());
            }
        }
    }
}
