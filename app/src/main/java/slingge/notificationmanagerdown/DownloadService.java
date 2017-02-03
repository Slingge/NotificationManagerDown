package slingge.notificationmanagerdown;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


/**
 * Created by Slingge on 2017/2/3 0003.
 */

public class DownloadService extends Service {

    private DownloadTask downloadTask;
    private String downloadUrl;
    private DownloadListenter listenter=new DownloadListenter() {
        @Override
        public void onProgress(int progress) {

        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailed() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onCanceled() {

        }
    };



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }






}
