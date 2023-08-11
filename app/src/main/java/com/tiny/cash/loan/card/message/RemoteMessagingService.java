package com.tiny.cash.loan.card.message;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tiny.cash.loan.card.feature.main.MainActivity;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.response.Response;
import com.tiny.cash.loan.card.net.response.data.bean.FcmTokenResult;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.LocalConfig;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RemoteMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private int id = 100;

    private String PUSH_CHANNEL_ID = "ID";
    private String PUSH_CHANNEL_NAME = "peacock";
    private int mNotificationId = 1000;
    private NotificationManager mNotificationManager= null;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    PUSH_CHANNEL_ID,
                    PUSH_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
    }
    /**
     * 判断是在前台还是后台
     */
    public static boolean isBackground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {

            if (appProcess.processName.equals(context.getPackageName())) {

                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {

                    Log.i("后台", appProcess.processName);

                    return true;

                }else{

                    Log.i("前台", appProcess.processName);

                    return false;

                }

            }

        }

        return false;

    }
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        if (isBackground(getApplicationContext())) {
        sendNotification( remoteMessage);
//        }
    }
    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(@NonNull @NotNull String token) {
        super.onNewToken(token);
        upLoadFcmtoken(token);
    }

    static  NetObserver fcmtokenObserver;
    /**
     * 谷歌FCM上报token
     */
    public static void upLoadFcmtoken(String token){
        Observable observable = NetManager.getApiService().uploadFcmToken(token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(fcmtokenObserver);
        fcmtokenObserver = new NetObserver<Response<FcmTokenResult>>() {
            @Override
            public void onNext(Response<FcmTokenResult> response) {

            }

            @Override
            public void onException(ResponseException netException) {

            }
        };
        observable.subscribeWith(fcmtokenObserver);
    }
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification( RemoteMessage messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(LocalConfig.LC_FIREBASEPUSH, "push");
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        String title = messageBody.getData().get("title");
        String desc =  messageBody.getData().get("desc");
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, PUSH_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(desc)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent);


        mNotificationManager.notify(mNotificationId, notificationBuilder.build());

    }

}