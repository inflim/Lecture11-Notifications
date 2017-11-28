package ru.mail.park.lecture11;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String ACTION_REMOVE_PROGRESS = "remove_progress";
    private NotificationManager manager;

    private static final long[] VIBRATION_PATTERN = {0, 100, 50, 100};
    private static final int LIGHT_COLOR_ARGB = Color.GREEN;

    public static final String CHANNEL_DEFAULT = "default";
    public static final String CHANNEL_MESSAGES = "messages";
    public static final String CHANNEL_LIKES = "likes";

    private static final int NOTIFICATION_ID_SIMPLE = 0;
    private static final int NOTIFICATION_ID_MESSAGE = 1;
    private static final int NOTIFICATION_ID_IMAGE = 2;
    private static final int NOTIFICATION_ID_ONGOING = 3;

    @BindView(R.id.edit_message) EditText messageEdit;

    public int messageCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);;

        initChannels();

        String action = getIntent().getAction();
        if (! TextUtils.isEmpty(action))
            manager.cancel(NOTIFICATION_ID_ONGOING);
    }


    @OnClick(R.id.button_simple)
    public void showSimpleNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_DEFAULT);

        builder.setSmallIcon(R.drawable.ic_announcement)
        .setContentTitle(getString(R.string.simple_name))
        .setContentText(getString(R.string.simple_description))
        .setAutoCancel(true);

        addDefaultIntent(builder);

        manager.notify(NOTIFICATION_ID_SIMPLE, builder.build());
    }


    @OnClick(R.id.button_clear)
    public void clearAll() {
        manager.cancelAll();
    }

    @OnClick(R.id.button_send)
    public void showMessageNotification() {
        messageCount++;

        String messageToShow = messageEdit.getText().toString();

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.example_large_icon);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_DEFAULT)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.ic_message_black)
                .setContentTitle(getString(R.string.message_name))
                .setContentText(messageToShow)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(VIBRATION_PATTERN)
                .setLights(LIGHT_COLOR_ARGB, 100, 100)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setAutoCancel(true);


        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(messageToShow);
        style.setSummaryText(getString(R.string.message_summary, messageCount));
        builder.setStyle(style);

        addDefaultIntent(builder);
        addMessageIntent(builder, messageToShow);

        manager.notify(NOTIFICATION_ID_MESSAGE, builder.build());
    }

    @OnClick(R.id.button_image)
    public void showBigImageNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_DEFAULT);

        builder.setSmallIcon(R.drawable.ic_announcement)
                .setContentTitle(getString(R.string.big_image))
                .setContentText(getString(R.string.simple_description))
                .setColor(getResources().getColor(R.color.colorAccent))
                .setAutoCancel(true);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.example_large_icon);
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.bg_5);
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(image);
        style.bigLargeIcon(largeIcon);

        builder.setStyle(style);

        addDefaultIntent(builder);

        manager.notify(NOTIFICATION_ID_IMAGE, builder.build());
    }

    @OnClick(R.id.button_ongoing)
    public void showOngoingNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_DEFAULT);

        builder.setSmallIcon(R.drawable.ic_announcement)
                .setContentTitle(getString(R.string.ongoing))
                .setContentText(getString(R.string.infinite_progress))
                .setColor(getResources().getColor(R.color.colorAccent))
                .setOngoing(true)
                .setProgress(100, 1, true);

        addDefaultIntent(builder);

        addRemoveProgressIntent(builder);

        manager.notify(NOTIFICATION_ID_ONGOING, builder.build());
    }


    private void addMessageIntent(NotificationCompat.Builder builder, String message) {

        Intent contentIntent = new Intent(this, MessageActivity.class);
        contentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        contentIntent.putExtra(MessageActivity.EXTRA_TEXT, message);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, contentIntent, flags);

        builder.addAction(new NotificationCompat.Action(0, getString(R.string.show), pendingIntent));
    }

    private void addRemoveProgressIntent(NotificationCompat.Builder builder) {

        Intent contentIntent = new Intent(this, MainActivity.class);
        contentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        contentIntent.setAction(ACTION_REMOVE_PROGRESS);

        int flags = PendingIntent.FLAG_CANCEL_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, contentIntent, flags);

        builder.addAction(new NotificationCompat.Action(0, getString(R.string.remove), pendingIntent));
    }


    private void addDefaultIntent(NotificationCompat.Builder builder) {

        Intent contentIntent = new Intent(this, MainActivity.class);
        contentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // отменить старый и создать новый
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, contentIntent, flags);

        builder.setContentIntent(pendingIntent);

    }

    public void initChannels() {
        if (Build.VERSION.SDK_INT < 26)
            return;

        NotificationChannel defaultChannel = new NotificationChannel(CHANNEL_DEFAULT,
                getString(R.string.channel_default_name), NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(defaultChannel);

        NotificationChannel likesChannel = new NotificationChannel(CHANNEL_LIKES,
                getString(R.string.channel_likes_name), NotificationManager.IMPORTANCE_LOW);
        likesChannel.setDescription(getString(R.string.channel_likes_description));
        manager.createNotificationChannel(likesChannel);

        NotificationChannel messagesChannel = new NotificationChannel(CHANNEL_MESSAGES,
                getString(R.string.channel_messages_name), NotificationManager.IMPORTANCE_HIGH);
        messagesChannel.setDescription(getString(R.string.channel_messages_description));
        messagesChannel.setVibrationPattern(VIBRATION_PATTERN);
        messagesChannel.enableLights(true);
        messagesChannel.enableVibration(true);
        messagesChannel.setLightColor(LIGHT_COLOR_ARGB);
        manager.createNotificationChannel(messagesChannel);
    }
}
