package slingge.notificationmanagerdown;

/**
 * Created by Slingge on 2017/2/3 0003.
 */

public interface DownloadListenter {

    void onProgress(int progress);//下载进度
    void onSuccess();//下载成功
    void onFailed();//下载失败
    void onPaused();//下载暂停
    void onCanceled();//下载取消

}
