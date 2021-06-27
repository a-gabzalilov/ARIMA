import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DWtest {
    private double[] X_data;
    private List<Double> last;
    private int d;

    private double find_a(){
        double[] a_coef = new double[1];
        double[][] Z = new double[1][X_data.length];
        double[] X = new double[X_data.length];
        for (int i =0; i<X_data.length; i++)
        {
            X[i]=X_data[i];
            if (i-1<0)
                Z[0][i] = 0;
            else
                Z[0][i] = X_data[i-1];
        }
        a_coef = SVD.getSVD2(Z, X);
        return a_coef[0];
    }

    private void first_differences(){
        double[] X_New = new double[X_data.length-1];
        for (int i = 0; i<X_data.length-1; i++)
        {
            X_New[i] = X_data[i+1] - X_data[i];
        }
        last.add(X_New[X_New.length-1]);
        X_data = X_New.clone();
    }

    public DWtest(double[] X)
    {
        last = new ArrayList<Double>();
        X_data = X.clone();
        while (!DW())
        {
            first_differences();
            d++;
        }
        System.out.println(d);
    }
    private boolean DW (){
        double[] e = new double[X_data.length];
        double a = find_a();
        return a<=1;
    }

    public double[] Integrate(double[] YY){
        double[] Int = new double[X_data.length+d];
        double[] Y = YY.clone();
        if (d ==0)
            return Y.clone();
        int count = 1;
        while (count<=d)
        {
            Int[count-1] = 0;
            for (int i = count; i<Y.length+1; i++)
            {
                Int[i] = Y[i-1]+Int[i-1];
            }
            count++;
            Y = new double[Y.length+1];
            for (int i = 0; i<Y.length;i++)
                Y[i] = Int[i];
        }
        X_data = Int.clone();
        return Int.clone();
    }

    public double[] Integrate(double[] X1, double[] X2){
        double[] Int = new double[X1.length + X2.length -1 +d];
        double[] XX = new double[X1.length + X2.length -1+d];
        for (int i =0; i<Int.length; i++)
        {
            if (i < X1.length)
                XX[i] = X1[i];
            else
                if(i-X1.length<X2.length)
                    XX[i] = X2[i-X1.length];
        }
        double[] Y = XX.clone();
        if (d ==0)
            return Y.clone();
        int count = 1;
        while (count<=d)
        {
            Int[count-1] = 0;
            for (int i = count; i<Y.length; i++)
            {
                Int[i] = Y[i-1]+Int[i-1];
            }
            count++;
            Y = new double[Y.length];
            for (int i = 0; i<Y.length;i++)
                Y[i] = Int[i];
        }
        return Int.clone();
    }

    /*public double[] Integrate(double[] XX, int size){
        double[] Int = new double[size];
        double[] Y = XX.clone();
        if (d ==0)
            return Y.clone();
        int count = 1;
        while (count<=d)
        {
            Y[count-1] = last.get(count-1);
            Int[count-1] = X_data[X_data.length-1];
            for (int i = count; i<size; i++)
            {
                Int[i] = Y[i-1]+Int[i-1];
            }
            count++;
            System.arraycopy(Int, 0, Y, 0, Y.length);
        }
        return Int.clone();
    }*/

    /*public double[] Integrate(double[] XX, int size){
        double[] Int = new double[size];
        double[] Y = XX.clone();
        if (d ==0)
            return Y.clone();
        int count = 1;
        while (count<=d)
        {
            Int[count-1] = X_data[X_data.length-1];
            for (int i = count; i<size; i++)
            {
                Int[i] = Y[i-1]+Int[i-1];
            }
            count++;
            for (int i = 0; i<Y.length;i++)
                Y[i] = Int[i];
        }
        return Int.clone();
    }*/

    public double[] getX_data(){
        return X_data.clone();
    }
    public int GetLength(){
        return X_data.length;
    }
    public String get_d(){
        return "D: " + d + " ";
    }
}
