package ntut.csie.engineering_mathematics.project.proj01.models;

import javafx.util.Pair;
import ntut.csie.engineering_mathematics.project.proj01.Storage;
import ntut.csie.engineering_mathematics.project.proj01.config.App;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by s911415 on 2017/03/21.
 */
public class Relation {
    private static ConcurrentSkipListMap<Long, Relation> _relationPool = new ConcurrentSkipListMap<>();
    private int _id1, _id2;
    private long _hash;
    private static boolean _initialized = false;


    private static long calcHashTwoWebsite(Website ref, Website tgt) {
        int id1 = ref.getId();
        int id2 = tgt.getId();
        return calcHashTwoWebsite(id1, id2);
    }

    private static long calcHashTwoWebsite(int ref, int tgt) {
        return ((long)ref << 32L) | (long)tgt;
    }

    public static Pair<Integer, Integer> decodeRelation(long id) {
        long id1 = id >> 32L;
        long id2 = id & 0xffffffffL;

        return new Pair<>((int)id1, (int)id2);
    }

    public static boolean containRelation(Website ref, Website tgt) {
        long id = calcHashTwoWebsite(ref, tgt);

        return _relationPool.containsKey(id);
    }

    public Relation(Website ref, Website tgt) {
        this(ref.getId(), tgt.getId());
    }

    private Relation(int id1, int id2) {
        _id1 = id1;
        _id2 = id2;
        _hash = calcHashTwoWebsite(_id1, _id2);

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
            ConcurrentSkipListMap<Long, Relation> copiedPool =new ConcurrentSkipListMap<>(_relationPool);
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
