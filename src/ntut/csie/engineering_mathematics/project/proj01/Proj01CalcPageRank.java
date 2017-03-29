package ntut.csie.engineering_mathematics.project.proj01;

import com.mathworks.engine.MatlabEngine;
import ntut.csie.engineering_mathematics.project.proj01.models.Relation;
import ntut.csie.engineering_mathematics.project.proj01.models.Website;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by s911415 on 2017/03/21.
 */
public class Proj01CalcPageRank {
    private static double DAMPING_FACTOR = 0.85;
    private static int ITERATOR_COUNT = 100;

    public static void main(String[] args) {
        Website.init();
        Relation.init();

        ArrayList<Relation> relationList = Relation.all();
        final int N = Website.getCurrentMaxId();
        double transferMatrix[][] = new double[N][N];

        System.out.println("Inputting matrix...");
        for (final Relation r : relationList) {
            int fromIdx = r.getReferenceId() - 1;
            int targetIdx = r.getTargetId() - 1;

            transferMatrix[targetIdx][fromIdx] = 1.0f;
        }

        try {
            //Start MATLAB
            System.out.println("Starting Matlab...");
            final MatlabEngine ml = MatlabEngine.startMatlab();

            System.out.println("Putting transfer matrix into matlab...");
            ml.putVariable("Po", transferMatrix);

            ml.eval("P=sparse(Po);");

            //計算機率矩陣
            System.out.println("Calcuating probability matrix...");
            for (int i = 0; i < N; i++) {
                ml.eval(String.format("count = nnz(P(:,%d));", i + 1));
                Double count = ml.<Double>getVariable("count");
                if (count.intValue() != 0) {
                    ml.eval(String.format("P(:,%d) = P(:,%d) / %f;", i + 1, i + 1, count));
                }
            }

            ml.putVariable("q", DAMPING_FACTOR);
            ml.eval("ee=ones(" + N + ", " + N + ") / " + N + ";");

            ml.eval("A=q*P+(1-q)*ee;");
            ml.eval("R=ones(" + N + ",1);"); //Initialize page rank value

            /*
              Using PAP-1
             */
            //System.out.println("Calculate using PAP-1...");
            //final long PAP_START_TIME = System.nanoTime();
            /*
            System.out.println("Calc eigenvalues and eigenvectors...");
            ml.eval("[Q D] = eig(A);");
            ml.eval("D = sparse(D);");
            System.out.println("Multiplying...");
            ml.eval(String.format("Rn = Q * (D ^ %d) * (Q^-1) * R;", ITERATOR_COUNT));
            */
            //final long PAP_END_TIME = System.nanoTime();

            /*
              Using Normal Multiplying
             */
            System.out.println("Calculate using normal mul...");
            final long NORMAL_MUL_START_TIME = System.nanoTime();
            System.out.println("Multiplying...");
            ml.eval(String.format("Rn2 = (A ^ %d) * R;", ITERATOR_COUNT));
            final long NORMAL_MUL_END_TIME = System.nanoTime();


            //System.out.println(String.format("使用對角矩陣相乘時間: %dns", PAP_END_TIME - PAP_START_TIME));
            System.out.println(String.format("使用一般乘法時間: %dns", NORMAL_MUL_END_TIME - NORMAL_MUL_START_TIME));

            double[] R;
            //ml.getVariable("Rn2");
            R = ml.<double[]>getVariable("Rn2");
            System.out.println(R.getClass());
            for (int i = 0, j = R.length; i < j; i++) {
                Website w = Website.getWebsiteById(i + 1);
                if (w != null) {
                    w.setPageRank(R[i]);
                }
            }

            Website.commit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
