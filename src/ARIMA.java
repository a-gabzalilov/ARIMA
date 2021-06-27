import java.util.Arrays;
import java.util.Random;

public class ARIMA {
    private final int P_max = 2;
    private final int Q_max = 2;
    private double[] X_data;
    private int my_N;
    private double[] Z_helpmatrix;
    private double[] new_c;
    private double[] new_a;
    private double[] new_e;
    private int new_p;
    private int new_q;

    //private double eps = 0.001;
    private double compact_size = 10;
    private double leftbound = 0;
    private double rightbound = 1;
    private double step = (rightbound-leftbound)/compact_size;

    public double[] e_data;

    public ARIMA(int n, double[] x)
    {
        my_N = n;
        this.X_data = x;
        e_data = new double[n];
    }

    private double get_Zvalue(int t){
        if(t < 0 || t >= Z_helpmatrix.length)
            return 0;
        return Z_helpmatrix[t];
    }

    private double get_Xvalue(int t){
        if(t < 0 || t >= X_data.length)
            return 0;
        return X_data[t];
    }
    private double get_eValue(int t)
    {
        if(t < 0 || t >= X_data.length)
            return 0;
        return new_e[t];
        //return e_data[t];
    }

    public double[] time_row(){
        double[] res = new double[my_N];
        for (int i = 0; i<my_N; i++)
        {
            res[i] = get_eValue(i);
            for (int j = 0; j<new_a.length; j++)
                res[i] += new_a[j]*get_Xvalue(i-j-1);
            for (int k = 0; k<new_c.length; k++)
                res[i] += new_c[k]*get_eValue(i-k-1);
        }
        return res.clone();
    }
    public double[] time_row(double[] time){
        double[] res = new double[time.length];
        double[] X_value = new double[my_N + time.length];
        X_value[my_N-2] = get_Xvalue(my_N-2);
        X_value[my_N-1] = get_Xvalue(my_N-1);
        double[] E_value = new double[my_N + time.length];
        E_value[my_N-2] = get_eValue(my_N-2);
        E_value[my_N-1] = get_eValue(my_N-1);
        /*double[] o = time_row();
        res[0] = o[o.length-1];
        X_value[my_N] = res[0];*/
        //Random r = new Random();
        for (int i = my_N; i<my_N+time.length; i++)
        {
            E_value[i] = 0;//(2*r.nextGaussian()-1)*0.01;
            res[i-my_N] = E_value[i];
            for (int j = 0; j<new_a.length; j++)
            {
                res[i-my_N] += new_a[j]*X_value[i-j-1];
            }
            X_value[i] =  res[i-my_N];
            /*for (int k = 0; k<new_c.length; k++)
            {
                res[i-my_N] += new_c[k]*E_value[i-k-1];
                //E_value[i] =
            }*/
        }
        return res.clone();
    }

    public String getValues(){
        String str = "P: "+ new_p +" Q: "+ new_q + " a: ";
        for (int j = 0; j<new_a.length; j++)
            str += new_a[j] + " ";
        str += "c: ";
        for (int k = 0; k<new_c.length; k++)
            str += new_c[k] + " ";
        return str;
    }

    public void ARMA()
    {
        double minerror = 1000000;
        for (int p = 0; p<=P_max; p++){
            for (int q = 0; q<=Q_max;q++)
            {
                int t_max = Math.max(p+1, q);
                Z_helpmatrix = new double[my_N];
                double[] c_coef = new double[q];
                double[] compact = new double[q];
                double minmax_error = 1000000;
                boolean exit = false;
                double[] a_optim = new double[p];
                double[] c_optim = new double[q];
                double[] e_optim = new double[my_N];
                do {

                    // find coefs c!

                    if (q != 0){
                        for (int i = 0; i < compact.length; i++)
                        {
                            c_coef[i] = compact[i];
                        }
                        if (c_coef[q-1] +step>= rightbound && c_coef[0]+step>=rightbound)
                            exit = true;
                        else
                        {
                            int l = 0;
                            while (true)
                            {
                                if (compact[l]+step<=rightbound) {
                                    compact[l] += step;
                                    break;
                                }
                                else
                                if(l<=q)
                                {
                                    compact[l]=0;
                                    l++;
                                }
                                else
                                    break;
                            }
                        }
                    }
                    else
                        exit = true;

                    //fill help matrix Z!

                    for (int t = 0; t < my_N; t++) {
                        Z_helpmatrix[t] = get_Xvalue(t);
                        if (q != 0) //checking
                            for (int tt = 1; tt <= c_coef.length; tt++) {
                                    Z_helpmatrix[t] -= c_coef[tt-1] * get_Zvalue(t - tt);
                            }
                    }

                    //find coefs a!

                    double[] a_coef = new double[p];

                    if (p >= 1) {
                        double[][] Z_matrix = new double[p][my_N];
                        double[] X_vector = new double[my_N];
                        for (int tt = 0; tt< my_N; tt++)
                            X_vector[tt] = get_Zvalue(tt);
                        for (int i = 0; i < p; i++) {
                            for (int j = 0; j<my_N; j++)
                                Z_matrix[i][j] = get_Zvalue(- i + j - 1);
                        }
                        a_coef = SVD.getSVD2(Z_matrix, X_vector);
                    }


                    //find e for fisrt t_max elements of row

                    for (int t = 0; t < t_max; t++) {
                        double left_side = get_Zvalue(t);
                        for (int tt = 0; tt < t; tt++) {
                            if (tt < a_coef.length)
                                left_side -= a_coef[tt] * get_Zvalue(t - tt-1);
                            else
                                left_side -= get_Zvalue(t - tt-1); //maybe tut!!!...........
                        }
                        e_data[t] = left_side;
                    }

                    //find e for the rest of the row

                    for (int t = t_max; t < my_N; t++) {
                        e_data[t]=get_Zvalue(t);
                        for (int tt = 0; tt< a_coef.length; tt++)
                            e_data[t] -= a_coef[tt]*get_Zvalue(t-tt-1);
                    }

                    //find error
                    double max_error = 0;
                    //if (q!=0)
                    for (int i=0; i<my_N;i++)
                    {
                        max_error += Math.pow(e_data[i], 2);
                    }
                    max_error = Math.sqrt(max_error);
                    if (max_error<minmax_error)
                    {
                        minmax_error = max_error;
                        c_optim = c_coef.clone();
                        a_optim = a_coef.clone();
                        e_optim = e_data.clone();
                    }
                    //System.out.println( Arrays.toString(a_coef)+Arrays.toString(c_coef));
                    //System.out.println("------>"+max_error+"\n");
                }while (!exit);

                if (minmax_error<minerror)
                {
                    minerror = minmax_error;
                    new_a = a_optim.clone();
                    new_c = c_optim.clone();
                    new_e = e_optim.clone();
                    new_p = p;
                    new_q = q;
                }

                /*String c_coef_string = "";
                String a_coef_string = "";
                if (q != 0) //checking
                    c_coef_string = Arrays.toString(c_optim);
                if (p != 0) //checking
                    a_coef_string = Arrays.toString(a_optim);
                String str = "a " + a_coef_string + "; c " + c_coef_string + "DW_test " + "\n";
                //output_file.write(str);
                //System.out.println(DWtest.DW(a_optim, X_data));
                System.out.println("AAA"+Arrays.toString(a_optim) + Arrays.toString(c_optim)+"\n");*/
            }
        }
    }
}
