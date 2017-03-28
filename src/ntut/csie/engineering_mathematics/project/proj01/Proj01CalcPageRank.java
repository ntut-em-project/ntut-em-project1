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
    private static int INERATOR_COUNT = 50;

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
                ml.eval(String.format("P(:,%d) = P(:,%d) / nnz(P(:,%d));", i + 1, i + 1, i + 1));
            }

            ml.putVariable("q", DAMPING_FACTOR);
            ml.eval("ee=ones(" + N + ", " + N + ") / " + N + ";");

            ml.eval("A=q*P+(1-q)*ee;");
            ml.eval("R=ones(" + N + ",1);"); //Initialize page rank value

            /*
              Using PAP-1
             */
            System.out.println("Calc using PAP-1...");
            final long PAP_START_TIME = System.nanoTime();
            System.out.println("Calc eigenvalues and eigenvectors...");
            ml.eval("[Q D] = eig(A);");
            ml.eval("D = sparse(D);");
            System.out.println("Mul ing...");
            ml.eval(String.format("Rn = Q * D ^ %d * Q^-1", INERATOR_COUNT));
            final long PAP_END_TIME = System.nanoTime();

            /*
              Using Normal mul
             */
            System.out.println("Calc using normal mul...");
            final long NORMAL_MUL_START_TIME = System.nanoTime();
            System.out.println("Mul ing...");
            ml.eval(String.format("Rn2 = A ^ %d", INERATOR_COUNT));
            final long NORMAL_MUL_END_TIME = System.nanoTime();


            System.out.println(String.format("使用對角矩陣相乘時間: %dns", PAP_END_TIME - PAP_START_TIME));
            System.out.println(String.format("使用一般乘法時間: %dns", NORMAL_MUL_END_TIME - NORMAL_MUL_START_TIME));


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
