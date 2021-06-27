import org.apache.commons.math3.linear.*;

public class SVD {
    //private double[] beta;

    public static double[]  getSVD(double[][] z_matrix, double[] x_vector){
        RealMatrix rm = MatrixUtils.createRealMatrix(z_matrix);
        RealVector rv = MatrixUtils.createRealVector(x_vector);
        //DecompositionSolver decompositionSolver = new SingularValueDecomposition(rm.multiply(rm.transpose())).getSolver();
        DecompositionSolver decompositionSolver = new SingularValueDecomposition(rm.transpose().multiply(rm)).getSolver();
        RealVector rr = rm.preMultiply(rv);
        RealVector answer = decompositionSolver.solve(rm.preMultiply(rv));
        double[] answer_vector = new double[answer.getDimension()];
        for (int i =0; i<answer.getDimension(); i++){
            answer_vector[i] = answer.getEntry(i);
        }
        return answer_vector;
        //return decompositionSolver.solve(new Array2DRowRealMatrix(rv)).getColumn(0);
    }

    public static double[]  getSVD2(double[][] z_matrix, double[] x_vector){
        RealMatrix rm = MatrixUtils.createRealMatrix(z_matrix);
        RealVector rv = MatrixUtils.createRealVector(x_vector);
        DecompositionSolver decompositionSolver = new SingularValueDecomposition(rm.multiply(rm.transpose())).getSolver();
        RealVector answer = decompositionSolver.solve(rm.transpose().preMultiply(rv));
        //System.out.println("hbhb" + rm.transpose().preMultiply(rv).getDimension());
        //RealVector answer = decompositionSolver.solve(rm.preMultiply(rv));
        double[] answer_vector = new double[answer.getDimension()];
        for (int i =0; i<answer.getDimension(); i++){
            answer_vector[i] = answer.getEntry(i);
        }
        return answer_vector;
        //return decompositionSolver.solve(new Array2DRowRealMatrix(rv)).getColumn(0);
    }

    /*public SVD(){
        this.beta = new double[0];
    }

    public void setBeta(double[] beta) {
        this.beta = beta;
    }

    public double get (double t){
        double res = 0.;
        for (int i = 0; i < this.beta.length; i++) {
            res += this.beta[i] * Math.pow(t, i);
        }
        return res;
    }

    public double getBeta(int i){
        double ret = beta[i];
        return ret;
    }
    public int lenght ()
    {
        return beta.length;
    }
    public String getBeta(){
        String str = "";
        for (int i = 0; i < this.beta.length; i++) {
            str = str + this.beta[i] + " ";
            //str = str + String.format("%.3f",this.beta[i]) + " ";
        }
        return str;
    }*/
}
