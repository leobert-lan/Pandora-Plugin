package osp.leobert.android.plugin.pandora.ui;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ui.MessageType;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.ui </p>
 * <p><b>Project:</b> Pandora-Plugin2 </p>
 * <p><b>Classname:</b> Notify </p>
 * Created by leobert on 2021/7/7.
 */
public class Notify {
    public static void show(String info) {
        NotificationGroup group = new NotificationGroup("Pandora.Notify", NotificationDisplayType.BALLOON, true);
        Notification notification = group.createNotification(info, MessageType.INFO);
        Notifications.Bus.notify(notification);
    }
}
