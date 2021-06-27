import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Solve {
    public static final String Delimeters;
    public static final int Max_degree;
    public static final int P_max;
    public static final int Q_max;
    public static final double my_pValue;
    static
    {
        Delimeters = " ";
        Max_degree = 3;
        my_pValue = 0.05;
        P_max = 2;
        Q_max = 2;
    }

    private static int my_N = 0;
    private static double[] T_data;
    private static double[] X_data;
    private static double[] e_data;


    static void Solve (Config config) throws IOException{
        FileWriter output_file = new FileWriter(config.get_file_name(Config.ConfigGrammar.OUTPUT_FILE.toString()));
        FileWriter leftovers_file = new FileWriter("leftovers.txt");
        File input_file = new File(config.get_file_name(Config.ConfigGrammar.INPUT_FILE.toString()));
        Scanner scanner = new Scanner(input_file);
        try  {
            String line = scanner.nextLine();
            my_N = Integer.parseInt(line);
            T_data = new double[my_N];
            X_data = new double[my_N];
            e_data = new double[my_N];
            line = scanner.nextLine();
            String[] data = line.split(Delimeters);
            for (int i = 0; i < data.length; i++)
            {
                T_data[i] = Double.parseDouble(data[i]);
            }
            line = scanner.nextLine();
            data = line.split(Delimeters);
            for (int i = 0; i < data.length; i++)
            {
                X_data[i] = Double.parseDouble(data[i]);
            }
        }
        catch (Exception exception) {
            Log.Log("log.log", "Troubles with reading input file");
        }
        finally {
            scanner.close();
        }

        XYChart chart = QuickChart.getChart("ARIMA", "T", "X", "Исходный ряд", T_data, X_data);


        int new_size = (int) (my_N* 0.25);

        double[] t_find = Arrays.copyOfRange(T_data, 0, my_N-new_size);
        double[] t_predict = Arrays.copyOfRange(T_data, my_N-new_size-1, my_N);
        double[] x_find = Arrays.copyOfRange(X_data, 0, my_N-new_size);


        DWtest dWtest = new DWtest(x_find);
        ARIMA arima = new ARIMA(dWtest.GetLength(), dWtest.getX_data());
        arima.ARMA();

        //chart.addSeries("Предсказанный ряд", t_predict, arima.time_row(t_predict));
        //chart.addSeries("Предсказанный ряд", t_predict, dWtest.Integrate(arima.time_row(t_predict), t_predict.length));
        chart.addSeries("Предсказанный ряд", T_data, dWtest.Integrate(arima.time_row(), arima.time_row(t_predict)));
        chart.addSeries("Полученный ряд", t_find, dWtest.Integrate(arima.time_row()));
        chart.getStyler().setMarkerSize(2);
        output_file.write(dWtest.get_d()+arima.getValues());
        new SwingWrapper(chart).displayChart();
        output_file.close();
        leftovers_file.close();
    }
}