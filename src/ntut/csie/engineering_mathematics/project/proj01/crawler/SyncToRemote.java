package ntut.csie.engineering_mathematics.project.proj01.crawler;

import ntut.csie.engineering_mathematics.project.proj01.models.Relation;
import ntut.csie.engineering_mathematics.project.proj01.models.Website;

import java.util.TimerTask;

/**
 * Created by s911415 on 2017/03/21.
 */
public class SyncToRemote extends TimerTask {
    @Override
    public void run() {
        Website.commit();
        Relation.commit();
    }
}
