package slingge.notificationmanagerdown;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,DownloadTask.DownProgressListenter{

    private ProgressBar progressBar;

    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = (DownloadService.DownloadBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);//启动服务
        bindService(intent, connection, BIND_AUTO_CREATE);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    private void init() {
        Button btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        Button btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_pause.setOnClickListener(this);
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        DownloadTask downloadTask=new DownloadTask();
        downloadTask.setDownProgressListenter(this);
    }

    @Override
    public void onClick(View view) {
        if (downloadBinder == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btn_start:
                String Url = "https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";
                downloadBinder.startDownload(Url);
                break;
            case R.id.btn_pause:
                downloadBinder.pauseDown();
                break;
            case R.id.btn_cancel:
                downloadBinder.cancleDown();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "无法使用权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public void Progress(int prigress) {
        progressBar.setProgress(prigress);
    }


}
