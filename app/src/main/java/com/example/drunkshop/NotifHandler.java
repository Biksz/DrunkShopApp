package com.example.drunkshop;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotifHandler {

    private static final String ID = "notification_channel";
    private static final int NOTIFICATION_ID = 0;
    private Context context;
    private NotificationManager manager;

    public NotifHandler(Context context) {
        this.context = context;
        this.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        handleChannel();
    }

    private void handleChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        NotificationChannel channel = new NotificationChannel(ID, "Lets Get Drunk", NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.MAGENTA);
        channel.setDescription("Iszol te eleget? Nem hinném, Gyere és rendelj valamit ;)");
        this.manager.createNotificationChannel(channel);
    }

    public void sendNotification(String msg){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ID).setContentTitle("DrunkApp").setContentText(msg).setSmallIcon(R.drawable.ic_cart);
        this.manager.notify(NOTIFICATION_ID, builder.build());
    }
}
