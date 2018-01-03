package cn.edu.gdmec.android.boxuegu.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import cn.edu.gdmec.android.boxuegu.R;

/**
 * Created by ASUS on 2017/12/28.
 */

public class VideoPlayActivity extends AppCompatActivity {
    private VideoView videoView;
    private MediaController controller;
    private String videoPath;//本地视频
    private int position;//传递视频详情界面点击的视频位置
    private String uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
               WindowManager.LayoutParams.FLAG_FULLSCREEN );//设置界面全部显示
        setContentView(R.layout.activity_video_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置界面为横屏
        videoPath = getIntent().getStringExtra("videoPath");//获取从播放记录界面传递过来的视频地址
        position = getIntent().getIntExtra("position",0);
        init();
    }

    private void init() {
        videoView = (VideoView) findViewById(R.id.videoView);
        controller = new MediaController(this);
        videoView.setMediaController(controller);
        play();
    }

    private void play() {
        if (TextUtils.isEmpty(videoPath)){
            Toast.makeText(this,"本地没有此视频，暂无法播放",Toast.LENGTH_SHORT).show();
            return;
        }
        if (position == 0){
            uri = "android.resource://" + getPackageName() + "/" + R.raw.video11;
        }else if(position == 1){
            uri = "android.resource://" + getPackageName() + "/" + R.raw.beyond;
        }

            videoView.setVideoPath(uri);
            videoView.start();
        }
        @Override
   public  boolean onKeyDown(int keyCode,KeyEvent event){
            Intent data = new Intent();
            data.putExtra("position",position);
            setResult(RESULT_OK,data);
            return super.onKeyDown(keyCode, event);

    }
}
