package cn.edu.gdmec.android.boxuegu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.bean.UserBean;
import cn.edu.gdmec.android.boxuegu.utils.AnalysisUtils;
import cn.edu.gdmec.android.boxuegu.utils.DBUtils;

/**
 * Created by ASUS on 2017/12/28.
 */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_back;
    private TextView tv_main_title;
    private TextView tv_nickName, tv_signature, tv_user_name, tv_sex;
    private RelativeLayout rl_nickName, rl_sex, rl_signature, rl_title_bar;
    private static  final int CHANGE_NICKNAME = 1;
    private static  final int CHANGE_SIGNATURE = 2;
    private String spUserName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);//设置界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//从sharedPreferences获取登录时的用户名
        spUserName = AnalysisUtils.readLoginUserName(this);
        init();
        initData();
        setListener();
    }
   private void init() {
       tv_back = (TextView) findViewById(R.id.tv_back);
       tv_main_title = (TextView) findViewById(R.id.tv_main_title);
       tv_main_title.setText("个人资料");
       rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
       rl_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
       rl_nickName = (RelativeLayout) findViewById(R.id.rl_nickName);
       rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
       rl_signature = (RelativeLayout) findViewById(R.id.rl_signature);
       tv_nickName = (TextView) findViewById(R.id.tv_nickName);
       tv_user_name = (TextView) findViewById(R.id.tv_user_name);
       tv_sex = (TextView) findViewById(R.id.tv_sex);
       tv_signature = (TextView) findViewById(R.id.tv_signature);
    }
//获取数据
    private void initData() {
        UserBean bean = null;
        bean = DBUtils.getInstance(this).getUserInfo(spUserName);
        //判断是否有数据库
        if(bean == null){
            bean = new UserBean();
            bean.userName=spUserName;
            bean.nickName="问答精灵";
            bean.sex="男";
            bean.signature="问答精灵";
            DBUtils.getInstance(this).saveUserInfo(bean);
        }
        setValue(bean);
    }
//界面控件设置值
    private void setValue(UserBean bean) {
        tv_nickName.setText(bean.nickName);
        tv_user_name.setText(bean.userName);
        tv_sex.setText(bean.sex);
        tv_signature.setText(bean.signature);
    }
//设置控件的点击监听事件
    private void setListener() {
        tv_back.setOnClickListener(this);
        rl_nickName.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_signature.setOnClickListener(this);
    }
//控件的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back://返回键点击事件
                this.finish();
                break;
            case R.id.rl_nickName://昵称点击事件
                String name  = tv_nickName.getText().toString();//获取昵称控件上的数据
                Bundle dbName = new Bundle();
                dbName.putString("content",name);//传递界面上的昵称数据
                dbName.putString("title","昵称");
                dbName.putInt("flag",1);//flag传递1时表示修改昵称
                enterActivityForResult(ChangeUserInfoActivity.class,CHANGE_NICKNAME,dbName);//跳转到个人资料修改界面
                break;
            case R.id.rl_sex://性别点击事件
                String sex = tv_sex.getText().toString();//获取性别控件上的数据
                sexDialog(sex);
                break;
            case R.id.rl_signature://签名键点击事件
                String signature = tv_signature.getText().toString();
                Bundle dbSignature = new Bundle();
                dbSignature.putString("content",signature);
                dbSignature.putString("title","签名");//flag传递2时表示修改签名
                dbSignature.putInt("flag",2);
                enterActivityForResult(ChangeUserInfoActivity.class,CHANGE_SIGNATURE,dbSignature);//跳转到个人资料修改界面
                break;
            default:
                break;
        }
    }

    private void sexDialog(String sex) {
        int sexFlag = 0;
        if("男".equals(sex)){
            sexFlag=0;
        }else if("女".equals(sex)){
            sexFlag=1;
        }
        final  String items[]={"男","女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//先得到构造器
        builder.setTitle("性别");//设置标题
        builder.setSingleChoiceItems(items,sexFlag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//第二个是默认中的哪个项
                dialog.dismiss();
                Toast.makeText(UserInfoActivity.this,items[which],Toast.LENGTH_SHORT).show();
                setSex(items[which]);
            }
        });
        builder.create().show();
    }
    //更新界面上的性别数据
    private void setSex(String sex){
        tv_sex.setText(sex);
        DBUtils.getInstance(UserInfoActivity.this).updateUserInfo("sex",sex,spUserName);
    }
    private String new_info;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CHANGE_NICKNAME://个人资料修改界面回传的昵称数据
                if(data!=null){
                    new_info = data.getStringExtra("nickName");
                    if(TextUtils.isEmpty(new_info)||new_info==null){
                        return;
                    }
                    tv_nickName.setText(new_info);
                    DBUtils.getInstance(UserInfoActivity.this).updateUserInfo(
                            "nickName",new_info,spUserName);
                }
                break;
            case CHANGE_SIGNATURE:
                if (data!=null){
                    new_info = data.getStringExtra("signature");
                    if(TextUtils.isEmpty(new_info)||new_info==null){
                        return;
                    }
                    tv_signature.setText(new_info);
                }
                DBUtils.getInstance(UserInfoActivity.this).updateUserInfo(
                        "signature",new_info,spUserName);
                break;
        }
    }

    public void enterActivityForResult(Class<?> to, int requestCode, Bundle b) {
        Intent i = new Intent(this,to);
        i.putExtras(b);
        startActivityForResult(i,requestCode);
    }
}
