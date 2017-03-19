package ntut.csie.engineering_mathematics.project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://en.wikipedia.org/").get();
            Elements newsHeadlines = doc.select("#mp-itn b a");

            for(Element e : newsHeadlines){
                System.out.println(String.format("Link: %s, Text: %s", e.attr("abs:href"), e.text().trim()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
