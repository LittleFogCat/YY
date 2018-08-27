package top.littlefogcat.yy;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@SuppressLint("SimpleDateFormat")
public class NotificationService extends Service {
    private static final String TAG = "NotificationService";
    public static final String CHANNEL_ID = "yy_id";
    public static final String CHANNEL_NAME = "yy通知";
    private NotificationData mNotificationData;
    private static final SimpleDateFormat sdf = new SimpleDateFormat(Constant.THE_FORMAT);

    public NotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean clear = intent.getBooleanExtra(Constant.INTENT_EXTRA_FLAG_CLEAR_NOTIFICATION, false);
        if (clear) {
            stopForeground(true);
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (nm != null) {
                nm.cancel(1003);
            }
        } else {
            Http.get(Constant.REMOTE_JSON_URL, new Http.Callback() {
                @Override
                public void onCallback(String s) {
                    Log.d(TAG, "onStartCommand onCallback: " + s);
                    try {
                        JSONObject obj = new JSONObject(s);
                        getNotificationData().type = obj.getInt("type");
                        getNotificationData().title = obj.getString("title");
                        getNotificationData().text = obj.getString("text");
                        if (getNotificationData().type != 0) {
                            showNotification();
                        } else {
                            showDefaultNotification();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onError(e);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    showDefaultNotification();
                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void showDefaultNotification() {
        String today = sdf.format(System.currentTimeMillis());
        int dayDiff = -1;
        try {
            dayDiff = DateCalculator.dayBetween(Constant.THE_DAY, today, Constant.THE_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getNotificationData().title = "爱您";
        getNotificationData().text = "爱您的第" + (dayDiff + 1) + "天";
        showNotification();
    }

    private void showNotification() {
        boolean showPersistNotification = SpUtils.getBoolean(Constant.SP_KEY_SHOW_PERSIST_NOTIFICATION, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (nManager != null) {
                nManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH));
                Notification notification = new Notification.Builder(this, CHANNEL_ID)
                        .setContentTitle(getNotificationData().title)
                        .setContentText(getNotificationData().text)
                        .setSmallIcon(R.drawable.logo_no_border)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_no_border))
                        .setWhen(System.currentTimeMillis())
                        .build();
                if (showPersistNotification) {
                    startForeground(1003, notification);
                } else {
                    stopForeground(true);
                    nManager.notify(1003, notification);
                }
            }
        } else {
            Notification notification = new Notification.Builder(this)
                    .setContentTitle(getNotificationData().title)
                    .setContentText(getNotificationData().text)
                    .setSmallIcon(R.drawable.logo_no_border)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_no_border))
                    .setWhen(System.currentTimeMillis())
                    .getNotification();
            if (showPersistNotification) {
                startForeground(1003, notification);
            } else {
                stopForeground(true);
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (nm != null) {
                    nm.notify(1003, notification);
                }
            }
        }
        scheduleNext();
    }

    private void scheduleNext() {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (am != null) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + 1);
            c.set(Calendar.MINUTE, 0);
            Intent intent = new Intent(this, NotificationService.class);
            am.set(AlarmManager.RTC, c.getTimeInMillis(), PendingIntent.getService(this, 0, intent, 0));
        }
    }

    private NotificationData getNotificationData() {
        if (mNotificationData == null) {
            mNotificationData = new NotificationData();
        }
        return mNotificationData;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
