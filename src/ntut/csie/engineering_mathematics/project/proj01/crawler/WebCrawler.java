package ntut.csie.engineering_mathematics.project.proj01.crawler;

import ntut.csie.engineering_mathematics.project.proj01.models.Relation;
import ntut.csie.engineering_mathematics.project.proj01.models.Website;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by s911415 on 2017/03/21.
 */
public class WebCrawler {
    public WebCrawler(Website web) {

        //Web crawler
        try {
            System.out.println(String.format("Getting: %s", web.getUrl()));

            Document doc = Jsoup.connect(web.getUrl()).followRedirects(true).get();
            web.setVisited();
            Element title = doc.getElementsByTag("title").first();
            if (title == null) {
                web.setTitle("Untitled");
            } else {
                web.setTitle(title.text().trim());
            }
            Elements newsHeadlines = doc.select("a, area");

            for (Element e : newsHeadlines) {
                String url = e.attr("abs:href").trim();
                Website newWebsite = Website.getInstance(url);
                if (newWebsite == null || web == newWebsite || Relation.containRelation(web, newWebsite)) {
                    continue;
                }
                new Relation(web, newWebsite);

                System.out.println(String.format("Found Link: %s", newWebsite.getUrl()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
