package ntut.csie.engineering_mathematics.project.proj01.models;

import ntut.csie.engineering_mathematics.project.proj01.Storage;
import ntut.csie.engineering_mathematics.project.proj01.config.App;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by s911415 on 2017/03/21.
 */
public class Relation {
    private static ConcurrentSkipListMap<String, Relation> _relationPool = new ConcurrentSkipListMap<>();
    private int _id1, _id2;
    private String _hash;
    private static boolean _initialized = false;


    private static String calcHashTwoWebsite(Website ref, Website tgt) {
        return calcHashTwoWebsite(ref.getId(), tgt.getId());
    }

    private static String calcHashTwoWebsite(int ref, int tgt) {
        String id1 = String.valueOf(ref);
        String id2 = String.valueOf(tgt);
        return id1 + "|" + id2;
    }

    public static boolean containRelation(Website ref, Website tgt) {
        String id = calcHashTwoWebsite(ref, tgt);

        return _relationPool.containsKey(id);
    }

    public Relation(Website ref, Website tgt) {
        this(ref.getId(), tgt.getId());
    }

    private Relation(int ref, int tgt) {
        _id1 = ref;
        _id2 = tgt;
        _hash = calcHashTwoWebsite(ref, tgt);

        _relationPool.put(_hash, this);
    }

    public static void init() {
        if (_initialized) return;
        try {
            PreparedStatement ps = Storage.getConnection().prepareStatement(
                    "SELECT ref_uid, tgt_uid FROM relation"
            );
            ResultSet resultSet = ps.executeQuery();
            int initId = 0;
            while (resultSet.next()) {
                try {
                    int ref_id = resultSet.getInt("ref_uid");
                    int tgt_uid = resultSet.getInt("tgt_uid");
                    Relation r = new Relation(ref_id, tgt_uid);

                } catch (SQLException se) {
                    se.printStackTrace();
                }

            }

            _initialized = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    synchronized public static boolean commit() {
        try {
            PreparedStatement ps = Storage.getConnection().prepareStatement(
                    "INSERT INTO relation (`ref_uid`, `tgt_uid`) VALUES (?, ?) ON DUPLICATE KEY UPDATE ref_uid=ref_uid"
            );
            int count = 0;
            ConcurrentSkipListMap<String, Relation> copiedPool = new ConcurrentSkipListMap<>(_relationPool);
            for (Relation relation : copiedPool.values()) {
                ps.setInt(1, relation._id1);
                ps.setInt(2, relation._id2);

                ps.addBatch();
                count++;

                if (count % App.MAX_ROW_COUNT == 0) {
                    ps.executeBatch();
                    count = 0;
                }
            }

            //Force exec query
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return true;
    }

}
