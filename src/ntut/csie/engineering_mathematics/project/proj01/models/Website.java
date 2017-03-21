package ntut.csie.engineering_mathematics.project.proj01.models;

import com.sun.istack.internal.Nullable;
import ntut.csie.engineering_mathematics.project.helper.Crypt;
import ntut.csie.engineering_mathematics.project.proj01.Storage;
import ntut.csie.engineering_mathematics.project.proj01.config.App;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by s911415 on 2017/03/21.
 */
public class Website {
    public static Queue<Website> unfinishedQueue = new LinkedList<>();
    private static int _currentMaxId = 0;
    private static int _lastMaxId = 0;
    private static ConcurrentHashMap<Integer, Website> _websitePool = new ConcurrentHashMap<>();
    private static HashMap<String, Website> _urlHashPool = new HashMap<>();
    private static boolean _initialized = false;

    private int _id;
    private String _urlHash;
    private String _title;
    private String _url;
    private double _pageRank;
    private Timestamp _createTime, _viewTime;

    public static void setCurMaxId(int mid) {
        _currentMaxId = mid;
        _lastMaxId = mid;
    }

    @Nullable
    public static Website getInstance(String url) {
        url = processUrl(url);

        if (url == null || url.isEmpty() || !url.startsWith("http")) return null;

        String hv = Crypt.sha256(url);
        Website w = _urlHashPool.get(hv);
        if (w == null) {
            w = new Website("", url);
            unfinishedQueue.add(w);
        }

        return w;
    }

    public Website(String title, String url) {
        this(++_currentMaxId, title, url);
    }

    private static String processUrl(String url) {
        int uHash = url.indexOf("#");
        if (uHash != -1) return url.substring(0, uHash);

        return url;
    }

    private Website(int id, String title, String url) {
        _url = processUrl(url);
        _title = title;

        _id = id;
        _urlHash = Crypt.sha256(_url);

        _websitePool.put(_id, this);
        _urlHashPool.put(_urlHash, this);

        _createTime = new Timestamp(new Date().getTime());
        _viewTime = null;
    }

    public void setVisited() {
        _viewTime = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    public Website setTitle(String title) {
        _title = title;

        return this;
    }

    public String getUrlHash() {
        return _urlHash;
    }

    public String getUrl() {
        return _url;
    }

    public double getPageRank() {
        return _pageRank;
    }

    public static void init() {
        if (_initialized) return;
        try {
            PreparedStatement ps = Storage.getConnection().prepareStatement(
                    "SELECT id, title, url, create_time, view_time FROM websites"
            );
            ResultSet resultSet = ps.executeQuery();
            int initId = 0;
            while (resultSet.next()) {
                try {
                    int _id = resultSet.getInt("id");
                    String _title = resultSet.getString("title");
                    String _url = resultSet.getString("url");
                    Timestamp _create = resultSet.getTimestamp("create_time");
                    Timestamp _view = resultSet.getTimestamp("view_time");

                    Website w = new Website(_id, _title, _url);
                    w._createTime = _create;
                    w._viewTime = _view;

                    if (_view == null) {
                        unfinishedQueue.add(w);
                    }

                    if (_id > initId) initId = _id;
                } catch (SQLException se) {
                    se.printStackTrace();
                }


            }

            setCurMaxId(initId);
            _initialized = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean commit() {
        try {
            PreparedStatement ps = Storage.getConnection().prepareStatement(
                    "INSERT INTO `websites` (`id`, `url_hash`, `title`, `url`, `create_time`, `view_time`) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE view_time=?"
            );
            int count = 0;
            for (Website website : _websitePool.values()) {
                //if (website.getId() < _lastMaxId) continue;
                ps.setInt(1, website.getId());
                ps.setString(2, website._urlHash);
                ps.setString(3, website._title);
                ps.setString(4, website._url);
                ps.setTimestamp(5, website._createTime);
                ps.setTimestamp(6, website._viewTime);
                ps.setTimestamp(7, website._viewTime);

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
