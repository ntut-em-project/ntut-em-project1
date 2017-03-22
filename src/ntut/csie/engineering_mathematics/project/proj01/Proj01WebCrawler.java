package ntut.csie.engineering_mathematics.project.proj01;

import ntut.csie.engineering_mathematics.project.proj01.config.App;
import ntut.csie.engineering_mathematics.project.proj01.crawler.SyncToRemote;
import ntut.csie.engineering_mathematics.project.proj01.crawler.WebCrawler;
import ntut.csie.engineering_mathematics.project.proj01.models.Relation;
import ntut.csie.engineering_mathematics.project.proj01.models.Website;

/**
 * Created by s911415 on 2017/03/21.
 */
public class Proj01WebCrawler {

    public static void main(String[] args) {
        Website.init();
        Relation.init();

        //final Timer timer = new Timer(true);
        final SyncToRemote task = new SyncToRemote();
        //timer.scheduleAtFixedRate(task, App.SYNC_INTERVAL_MS, App.SYNC_INTERVAL_MS);
        final Thread thread = new Thread(() -> {
            long lastSyncTime = 0;
            while (!Website.unfinishedQueue.isEmpty()) {
                Website w = Website.unfinishedQueue.poll();
                new WebCrawler(w);

                long curTime = System.currentTimeMillis();
                if (curTime - lastSyncTime > App.SYNC_INTERVAL_MS) {
                    task.run();
                    lastSyncTime = System.currentTimeMillis();
                }

                try {
                    Thread.sleep(App.CRAWL_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        while (!thread.isInterrupted()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
