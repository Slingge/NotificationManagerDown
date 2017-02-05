package slingge.notificationmanagerdown;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.backup.BackupDataOutput;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.style.BulletSpan;
import android.widget.Toast;

import java.io.File;


/**
 * Created by Slingge on 2017/2/3 0003.
 */

public class DownloadService extends Service {

    private DownloadTask downloadTask;
    private String downloadUrl;
    private DownloadListenter listenter = new DownloadListenter() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("正在下载...",progress));
        }

        @Override
        public void onSuccess() {
            downloadTask=null;
            //下载成功将前台服务通知关闭，并创建一个下载成功的通知
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载完成",-1));
            Toast.makeText(DownloadService.this,"下载完成", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask=null;
            //下载失败时将前台服务通知关闭，并创建一个下载失败的通知
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载失败",-1));
            Toast.makeText(DownloadService.this,"下载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask=null;
            Toast.makeText(DownloadService.this,"下载暂停", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask=null;
            stopForeground(true);
            Toast.makeText(DownloadService.this,"下载取消", Toast.LENGTH_SHORT).show();
        }
    };

    private DownloadBinder mBind=new DownloadBinder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBind;
    }


    class DownloadBinder extends Binder{
        public void startDownload(String url){
            if(downloadTask==null){
                downloadUrl=url;
                downloadTask=new DownloadTask(listenter);
                downloadTask.execute(downloadUrl);
                startForeground(1,getNotification("正在下载...",0));
                Toast.makeText(DownloadService.this,"开始下载",Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDown(){
            if(downloadTask!=null){
                downloadTask.pauseDownload();
            }
        }

        public void cancleDown(){
            if(downloadTask!=null){
                downloadTask.canceDownload();
            }else{
                if(downloadUrl!=null){
                    //取消下载将文件删除，并将通知关闭
                    String fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file=new File(directory+fileName);
                    if(file.exists()){
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this,"下载取消",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }



    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title,int progress){
        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);//NotificationCompat,v7包或者v4包
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if(progress>0){
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }


}
