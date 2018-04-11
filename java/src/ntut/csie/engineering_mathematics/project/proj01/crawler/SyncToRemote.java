package ntut.csie.engineering_mathematics.project.proj01.crawler;

import ntut.csie.engineering_mathematics.project.proj01.config.App;
import ntut.csie.engineering_mathematics.project.proj01.models.Relation;
import ntut.csie.engineering_mathematics.project.proj01.models.Website;

/**
 * Created by s911415 on 2017/03/21.
 */
public class SyncToRemote implements Runnable {
    @Override
    public void run() {
        System.out.println("Start sync");
        Website.commit();
        Relation.commit();
        System.out.println("Sync done waiting");
        try {
            Thread.sleep(App.SYNC_SLEEP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("==================");
    }
}
