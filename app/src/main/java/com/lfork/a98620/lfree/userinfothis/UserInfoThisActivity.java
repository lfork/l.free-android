package com.lfork.a98620.lfree.userinfothis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.UserInfoThisActBinding;
import com.lfork.a98620.lfree.util.ImageTool;
import com.lfork.a98620.lfree.util.ProviderHelper;
import com.lfork.a98620.lfree.util.mvvmadapter.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class UserInfoThisActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UserInfoThisActivity";

    UserInfoThisActBinding binding;

    UserInfoThisViewModel viewModel;

    private Uri imageUri;

    /**
     * 定义三种状态
     */
    private static final int REQUESTCODE_PIC = 1;//相册
    private static final int REQUESTCODE_CAM = 2;//相机
    private static final int REQUESTCODE_CUT = 3;//图片裁剪

    PopupWindow avatorPop;

    private File portraitFile;

    protected int mScreenWidth;

    //刷新一下数据
    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refreshData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.user_info_this_act);
        viewModel = new UserInfoThisViewModel(this);
        binding.setViewModel(viewModel);
        binding.head.setOnClickListener(this);
        initActionBar();
    }


    public void portraitOnClick(ImageView view) {
        showMyDialog();
    }


    public void initActionBar() {
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
                startActivityForResult(intent, 4);
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
        switch (requestCode) {
            case 4:
                String log = data.getStringExtra("data_return");
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, log, Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, log, Toast.LENGTH_LONG).show();
                }
                break;
            case REQUESTCODE_CAM:
                if (portraitFile.length() < 1){
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    startPhotoZoom(ProviderHelper.getImageContentUri(getApplicationContext(), portraitFile.getPath()));     //小结， 最有用的是file路径。  我们需要把其他的uri转化为file路径，也要会把file路径转化成uri
                } else {
                    startPhotoZoom(imageUri);
                }
                break;
            case REQUESTCODE_PIC:
                if (data== null || data.getData() == null){
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    File uriFile = new File(ImageTool.getPath(UserInfoThisActivity.this, data.getData()));
                    startPhotoZoom(ProviderHelper.getImageContentUri(getApplicationContext(), uriFile.getPath()));
                } else {
                    startPhotoZoom(data.getData());

                }

                break;
            case REQUESTCODE_CUT:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }

    }

    private void showMyDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.user_info_this_pop_show_dialog, null);
        RelativeLayout layout_choose = view.findViewById(R.id.layout_choose);
        RelativeLayout layout_photo = view.findViewById(R.id.layout_photo);
        RelativeLayout layout_close = view.findViewById(R.id.layout_close);
        layout_photo.setOnClickListener(arg0 -> {
            int checkCallPhonePermission = ActivityCompat.checkSelfPermission(UserInfoThisActivity.this, Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UserInfoThisActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
            } else {
                openCamera();
                avatorPop.dismiss();
            }
        });

        layout_choose.setOnClickListener(arg0 -> {
            if (ContextCompat.checkSelfPermission(UserInfoThisActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //6.0及以上的系统在使用危险权限的时候必须进行运行时权限处理
                ActivityCompat.requestPermissions(UserInfoThisActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                openAlbum();
                avatorPop.dismiss();
            }
        });

        layout_close.setOnClickListener(v -> avatorPop.dismiss());
        DisplayMetrics metric = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        avatorPop = new PopupWindow(view, mScreenWidth, 200);
        avatorPop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    avatorPop.dismiss();
                    return true;
                }
                return false;
            }
        });

        avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        avatorPop.setTouchable(true);
        avatorPop.setFocusable(true);
        avatorPop.setOutsideTouchable(true);
        avatorPop.setBackgroundDrawable(new BitmapDrawable());
        // 动画效果 从底部弹起
        avatorPop.setAnimationStyle(R.style.AppTheme);
        avatorPop.showAtLocation(binding.all, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUESTCODE_PIC);
    }

    /**
     * 调用相机
     */
    private void openCamera() {
        //创建file对象用于储存拍摄的照片
        portraitFile = new File(getExternalCacheDir(), "output_image.jpg");

        try {
            if (portraitFile.exists()) {
                portraitFile.delete();
            }
            portraitFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(UserInfoThisActivity.this, "com.lfork.a98620.lfree.fileprovider", portraitFile);
        } else {
            imageUri = Uri.fromFile(portraitFile);
        }

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //将url关联到相机拍摄的图片上面
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUESTCODE_CAM);
    }

    private void setPicToView(Intent data) {
        Uri uri = data.getData();
        if (uri != null) {
            Image.setImage(binding.head, ProviderHelper.getFilePathOnKitKat(getApplicationContext(), uri));
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
        }
    }

    /**
     * 打开系统图片裁剪功能
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true); //黑边
        intent.putExtra("scaleUpIfNeeded", true); //黑边
        intent.putExtra("return-data", false);        //设置为true的话将会返回一个bitmap 设置为false的话就会返回路径
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUESTCODE_CUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 222:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    openCamera();
                    avatorPop.dismiss();
                } else {
                    // Permission Denied
                    Toast.makeText(UserInfoThisActivity.this, "很遗憾你把相机权限禁用了", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                    avatorPop.dismiss();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View view) {
        portraitOnClick((ImageView) view);
    }
}
