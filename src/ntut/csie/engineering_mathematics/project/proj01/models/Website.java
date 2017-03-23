package ntut.csie.engineering_mathematics.project.proj01.models;

import com.sun.istack.internal.Nullable;
import ntut.csie.engineering_mathematics.project.helper.Crypt;
import ntut.csie.engineering_mathematics.project.proj01.Storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by s911415 on 2017/03/21.
 */
public class Website {
    public static Queue<Website> unfinishedQueue = new LinkedList<>();
    private static int _currentMaxId = 0;
    private static int _lastMaxId = 0;
    private static ConcurrentSkipListMap<Integer, Website> _websitePool = new ConcurrentSkipListMap<>();
    private static HashMap<String, Website> _urlHashPool = new HashMap<>();
    private static boolean _initialized = false;
    private static final List<String> WHITE_LIST = Arrays.asList(
            "csie.ntut.edu.tw/", "gec.ntut.edu.tw/", "oaa.ntut.edu.tw/",".cc.ntut.edu.tw/", "/140.124"
    );
    private static final List<String> BLOCK_LIST = Arrays.asList(
            ".pdf", ".gif", ".png", ".jpg", ".doc", ".odt", ".xls", ".ppt", "downloadfile.php",
            "140.124.9.15"//V 集合
    );
    public boolean _synced = false;

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
    public static Website getInstance(String _url) {
        final String url = processUrl(_url);

        if (url == null || url.isEmpty() || !url.startsWith("http")) return null;
        if (BLOCK_LIST.stream().anyMatch(url::contains)) return null;
        if (WHITE_LIST.stream().noneMatch(url::contains)) return null;

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

        _createTime = new Timestamp(System.currentTimeMillis());
        _viewTime = null;
    }

    public void setVisited() {
        _viewTime = new Timestamp(System.currentTimeMillis());
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
                    } else {
                        w._synced = true;
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

    public boolean isDone() {
        return this._viewTime != null && !this._title.isEmpty();
    }

    synchronized public static boolean commit() {
        try {
            int count = 0;
            ConcurrentSkipListMap<Integer, Website> copiedPool = new ConcurrentSkipListMap<>(_websitePool);
            for (Website website : copiedPool.values()) {
                PreparedStatement ps = Storage.getConnection().prepareStatement(
                        "INSERT INTO `websites` (`id`, `url_hash`, `title`, `url`, `create_time`, `view_time`) " +
                                "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY " +
                                "UPDATE title=?, view_time=?"
                );
                if (website._synced) continue;
                ps.setInt(1, website.getId());
                ps.setString(2, website._urlHash);
                ps.setString(3, website._title);
                ps.setString(4, website._url);
                ps.setTimestamp(5, website._createTime);
                ps.setTimestamp(6, website._viewTime);

                ps.setString(7, website._title);
                ps.setTimestamp(8, website._viewTime);

                try {
                    ps.execute();
                    if (website.isDone()) {
                        website._synced = true;
                    }
                } catch (Exception e) {
                    website._synced = false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return true;
    }
}
