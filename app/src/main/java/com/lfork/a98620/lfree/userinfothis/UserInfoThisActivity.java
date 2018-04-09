package com.lfork.a98620.lfree.userinfothis;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.UserInfoThisActBinding;
import com.lfork.a98620.lfree.util.ImageTool;

import java.io.File;
import java.util.List;

public class UserInfoThisActivity extends AppCompatActivity {

    UserInfoThisActBinding binding;

    UserInfoThisViewModel viewModel ;

    /**
     * 定义三种状态
     */
    private static final int REQUESTCODE_PIC = 1;//相册
    private static final int REQUESTCODE_CAM = 2;//相机
    private static final int REQUESTCODE_CUT = 3;//图片裁剪

    PopupWindow avatorPop;

    private File mFile;

    protected int mScreenWidth;


    //刷新一下数据
    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null){
            viewModel.refreshData();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.user_info_this_act);
        viewModel = new UserInfoThisViewModel(this);
        binding.setViewModel(viewModel);

        initActionBar();

    }

    public void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("用户信息详情");
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu1:
                Intent intent = new Intent(UserInfoThisActivity.this, UserInfoThisEditActivity.class);
                startActivityForResult(intent, 1);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_action_bar, menu);
        MenuItem item = menu.getItem(0);
        item.setTitle("编辑");

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK)
                Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
        }

    }



    private void showMyDialog() {

//        View view = LayoutInflater.from(this).inflate(R.layout.currentuserinfo_pop_show_dialog, null);
//        RelativeLayout layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
//        RelativeLayout layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
//        RelativeLayout layout_close = (RelativeLayout) view.findViewById(R.id.layout_close);
//
//        layout_photo.setOnClickListener(arg0 -> {
//            openCamera();
//            avatorPop.dismiss();
//        });
//
//        layout_choose.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//                if (ContextCompat.checkSelfPermission(UserInfoThisActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//                    //6.0及以上的系统在使用危险权限的时候必须进行运行时权限处理
//                    ActivityCompat.requestPermissions(UserInfoThisActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//
//                } else {
//                    openPic();
//                    avatorPop.dismiss();
//                }
//
//            }
//        });
//
//        layout_close.setOnClickListener(v -> avatorPop.dismiss());
//
//
//        DisplayMetrics metric = new DisplayMetrics();
//        //getWindowManager().getDefaultDisplay().getMetrics(metric);
//        mScreenWidth = metric.widthPixels;
//        avatorPop = new PopupWindow(view, mScreenWidth, 200);
//        avatorPop.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                    avatorPop.dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
//        avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        avatorPop.setTouchable(true);
//        avatorPop.setFocusable(true);
//        avatorPop.setOutsideTouchable(true);
//        avatorPop.setBackgroundDrawable(new BitmapDrawable());
//        // 动画效果 从底部弹起
//        avatorPop.setAnimationStyle(R.style.AppTheme);
//        avatorPop.showAtLocation(mBinding.all, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 打开相册
     */
    private void openPic() {
        Intent picIntent = new Intent(Intent.ACTION_PICK, null);
        picIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(picIntent, REQUESTCODE_PIC);
    }

    /**
     * 调用相机
     */
    private void openCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!file.exists()) {
                file.mkdirs();
            }
            mFile = new File(file, System.currentTimeMillis() + ".jpg");
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion < 24) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
                startActivityForResult(intent, REQUESTCODE_CAM);
            } else {
                int checkCallPhonePermission = ActivityCompat.checkSelfPermission(UserInfoThisActivity.this, Manifest.permission.CAMERA);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserInfoThisActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                } else {
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, mFile.getAbsolutePath());
                    Uri uri = FileProvider.getUriForFile(this.getApplicationContext(), "com.example.yangq.lab_manage_sys.SwApplication.fileprovider", mFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, REQUESTCODE_CAM);
                }
            }
        } else {
            Toast.makeText(this, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
        }
    }

//    private void setPicToView(Intent data) {
//        Uri uri = data.getData();
//        if (uri != null) {
//            Glide.with(this).load(uri.getPath()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(mBinding.imageHead);
//            ContactsRepository repository = ContactsRepository.getInstance(this);
//            viewModel.setIsUpdating(true);
//            repository.updateUserPortrait(new DataSource.GeneralCallback<String>() {
//                @Override
//                public void success(String data1) {
//
//                    mRepository.refreshUsers();
//                    mRepository.getUsers(new DataSource.GeneralCallback<List<Contacts>>() {
//                        @Override
//                        public void success(List<Contacts> data) {
//                            viewModel.setIsUpdating(false);
//                            Toast.makeText(getApplicationContext(), data1, Toast.LENGTH_LONG).show();
//                        }
//
//                        @Override
//                        public void failed(String log) {
//                            viewModel.setIsUpdating(false);
//
//                        }
//                    });
//                }
//
//                @Override
//                public void failed(String log) {
//                    Toast.makeText(getApplicationContext(), log, Toast.LENGTH_LONG).show();
//                    viewModel.setIsUpdating(false);
//
//                }
//            }, uri.getPath());
//        }
//    }

    /**
     * 打开系统图片裁剪功能
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            File uriFile = new File(ImageTool.getPath(getApplicationContext(), uri));
            intent.setDataAndType(Uri.fromFile(uriFile), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }


        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true); //黑边
        intent.putExtra("scaleUpIfNeeded", true); //黑边
        intent.putExtra("return-data", true);        //设置为true的话将会返回一个bitmap 设置为false的话就会返回路径
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUESTCODE_CUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            //就像onActivityResult一样这个地方就是判断你是从哪来的。
            case 222:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    mFile = new File(file, System.currentTimeMillis() + ".jpg");
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, mFile.getAbsolutePath());
                    Uri uri = FileProvider.getUriForFile(this.getApplicationContext(), "com.example.yangq.lab_manage_sys.SwApplication.fileprovider", mFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, REQUESTCODE_CAM);
                } else {
                    // Permission Denied
                    Toast.makeText(UserInfoThisActivity.this, "很遗憾你把相机权限禁用了", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPic();
                    avatorPop.dismiss();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
