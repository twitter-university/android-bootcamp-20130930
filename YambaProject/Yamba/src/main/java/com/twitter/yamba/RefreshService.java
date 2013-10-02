package com.twitter.yamba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by mgargenta on 10/2/13.
 */
public class RefreshService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }
}
