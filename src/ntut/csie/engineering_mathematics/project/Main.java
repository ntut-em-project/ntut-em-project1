package ntut.csie.engineering_mathematics.project;

import com.mathworks.engine.MatlabEngine;
import com.mathworks.matlab.types.Complex;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {
    public static <T> CompletableFuture<T> makeCompletableFuture(Future<T> future) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void outMatrix(Complex[] r) {
        for (int i = 0; i < r.length; i++) {
            System.out.println(String.format("%.3f", r[i].real));
        }
    }

    public static void outMatrix(Complex[][] r) {
        for (int row = 0; row < r.length; row++) {
            for (int col = 0; col < r[row].length; col++) {
                System.out.print(String.format("%.3f\t", r[row][col].real));
            }
            System.out.println();
        }
    }

    public static void outMatrix(double[][] r) {
        for (int row = 0; row < r.length; row++) {
            for (int col = 0; col < r[row].length; col++) {
                System.out.print(String.format("%.3f\t", r[row][col]));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Document doc = null;
        try {
            double[][] input = {
                    {0, 0, 1},
                    {.5, 0, 0},
                    {.5, 1, 0}
            };
            final int n = input.length;
            final int iter = 30;

            //Start MATLAB asynchronously
            final MatlabEngine ml = MatlabEngine.startMatlabAsync().get();
            ml.putVariable("P", input);
            ml.putVariable("q", .85);

            // Evaluate the command in MATLAB
            ml.eval("ee=ones(" + n + ", " + n + ")/" + n + ";");
            ml.eval("R=ones(" + n + ",1);");
            ml.eval("A=q*P+(1-q)*ee;");
            ml.eval("[Q, D]=eig(A);");
            ml.eval("R=Q*D^" + iter + "*Q^-1*R;");
            outMatrix(ml.<double[][]>getVariable("A"));
            outMatrix(ml.<Complex[]>getVariable("R"));
            // Disconnect from the MATLAB session
            ml.disconnect();
        } catch (Exception e) {
            e.printStackTrace();

        }

        //Web crawler
        try {
            doc = Jsoup.connect("http://en.wikipedia.org/").get();
            Elements newsHeadlines = doc.select("#mp-itn b a");

            for (Element e : newsHeadlines) {
                System.out.println(String.format("Link: %s, Text: %s", e.attr("abs:href"), e.text().trim()));
            }

            System.out.println("");
            System.out.println("========================");
            System.out.println("");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}