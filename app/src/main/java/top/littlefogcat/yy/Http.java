package top.littlefogcat.yy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by LittleFogCat on 2018/8/26.
 */
public class Http {
    public interface Callback {
        void onCallback(String s);

        void onError(Throwable t);
    }

    public static String get(String url) throws IOException {
        URL u = new URL(url);
        URLConnection conn = u.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        InputStream is = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public static void get(String url, Callback callback) {
        new Thread(() -> {
            try {
                callback.onCallback(get(url));
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(e);
            }
        }).start();
    }
}
