package ntut.csie.engineering_mathematics.project.proj01.crawler;

import ntut.csie.engineering_mathematics.project.proj01.config.App;
import ntut.csie.engineering_mathematics.project.proj01.models.Relation;
import ntut.csie.engineering_mathematics.project.proj01.models.Website;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by s911415 on 2017/03/21.
 */
public class WebCrawler {
    public WebCrawler(Website web) {

        //Web crawler
        try {
            System.out.println(String.format("Getting: %s", web.getUrl()));

            Document doc = Jsoup.connect(web.getUrl()).timeout(App.CRAWL_TIMEOUT).followRedirects(true).get();
            Element title = doc.getElementsByTag("title").first();
            if (title == null) {
                web.setTitle("Untitled");
            } else {
                web.setTitle(title.text().trim());
            }

            List<String> urls = new LinkedList<>();
            for (Element e : doc.select("a, area")) {
                urls.add(e.attr("abs:href").trim());
            }
            for (Element e : doc.select("frame, iframe")) {
                urls.add(e.attr("abs:src").trim());
            }

            for (final String url : urls) {
                Website newWebsite = Website.getInstance(url);
                if (newWebsite == null || web == newWebsite || Relation.containRelation(web, newWebsite)) {
                    continue;
                }
                new Relation(web, newWebsite);

                System.out.println(String.format("Found Link: %s", newWebsite.getUrl()));
            }
            web.setVisited();
        } catch (Exception e) {
            web.setTitle("--ERROR PAGE--" + e.getMessage());
            web.setVisited();
            e.printStackTrace();
        }
    }
}
