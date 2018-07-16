package com.lfork.a98620.lfree.userinfothis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.customview.PopupDialog;
import com.lfork.a98620.lfree.base.customview.PopupDialogOnclickListener;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.databinding.UserInfoThisActBinding;
import com.lfork.a98620.lfree.userinfoedit.UserInfoEditActivity;
import com.lfork.a98620.lfree.util.ToastUtil;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class UserInfoThisActivity extends AppCompatActivity implements View.OnClickListener, ViewModelNavigator {

    private static final String TAG = "UserInfoThisActivity";

    UserInfoThisActBinding binding;

    UserInfoThisViewModel viewModel;

    private Uri cameraImageUri, cropImageUri;

    /**
     * 定义三种状态
     */
    private static final int REQUESTCODE_PIC = 1;//相册
    private static final int REQUESTCODE_CAM = 2;//相机
    private static final int REQUESTCODE_CUT = 3;//图片裁剪

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.user_info_this_act);
        viewModel = new UserInfoThisViewModel(this);
        binding.setViewModel(viewModel);
        binding.head.setOnClickListener(this);
        setupActionBar();
    }


    //刷新一下数据
    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {

            viewModel.setNavigator(this);
            viewModel.start();
        }
    }

    public void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayShowTitleEnabled(true);
        actionBar.setTitle("用户信息详情");
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false);

    }


    public void portraitOnClick(ImageView view) {
        showMyDialog();
    }

    private void showMyDialog() {
        new PopupDialog(this, new PopupDialogOnclickListener() {
            @Override
            public void onFirstButtonClicked(PopupDialog dialog) {
                int checkCallPhonePermission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) + ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.showShort(getApplicationContext(), "请允许程序访问您的SD卡，以便上传图片^v^");
                    ActivityCompat.requestPermissions(UserInfoThisActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                } else {
                    openCamera();
                }
            }

            @Override
            public void onSecondButtonClicked(PopupDialog dialog) {
                if (ContextCompat.checkSelfPermission(UserInfoThisActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //6.0及以上的系统在使用危险权限的时候必须进行运行时权限处理
                    ActivityCompat.requestPermissions(UserInfoThisActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();

                }
            }

            @Override
            public void onCanceledClicked(PopupDialog dialog) {

            }
        }, binding.all).show();


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
        cameraImageUri = initImageUri();

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //将url关联到相机拍摄的图片上面
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        startActivityForResult(intent, REQUESTCODE_CAM);
    }

    private Uri initImageUri() {
        //创建file对象用于储存拍摄的照片
        File portraitFile = new File(getExternalCacheDir(), "portrait.jpg");

        try {
            if (portraitFile.exists()) {
                portraitFile.delete();
            }
            portraitFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(UserInfoThisActivity.this,
                    "com.lfork.a98620.lfree.fileprovider", portraitFile);
        } else {
            return Uri.fromFile(portraitFile);
        }

    }


    /**
     * 打开UCrop图片裁剪功能
     *
     * @param uri 这里需要content类型的uri
     */
    private void startPhotoZoom(Uri uri) {
        cropImageUri = Uri.fromFile(new File(getExternalCacheDir(), "crop_portrait.jpg")); //在某些场景还是得用fromFile
        UCrop.of(uri, cropImageUri)     //源uri 为content类型的, 目标uri为file类型
                .withAspectRatio(1, 1)
                .withMaxResultSize(1080, 1920)
                .start(this);

//
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.putExtra("crop", true);
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 300);
//        intent.putExtra("outputY", 300);
//        intent.putExtra("scale", true); //黑边
//        intent.putExtra("scaleUpIfNeeded", true); //黑边
//        intent.putExtra("return-data", false);        //设置为true的话将会返回一个bitmap 设置为false的话就会返回路径
//        intent.putExtra("noFaceDetection", true);
//        startActivityForResult(intent, REQUESTCODE_CUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 222:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    openCamera();
                } else {
                    // Permission Denied
                    Toast.makeText(UserInfoThisActivity.this, "很遗憾你把相机权限禁用了", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 4:
                if (data == null) {
                    return;
                }
                String log = data.getStringExtra("data_return");
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, log, Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, log, Toast.LENGTH_LONG).show();
                }
                break;
            case REQUESTCODE_CAM:
                if (cameraImageUri == null) {
                    return;
                }
//                Uri uri = data.getData();   //这里肯定是空的， 因为处理结果已经放到 cameraImageUri里面去了
                startPhotoZoom(cameraImageUri);
                break;
            case REQUESTCODE_PIC:
                if (data == null || data.getData() == null) {
                    return;
                }
                startPhotoZoom(data.getData()); //照片选择的结果也是标准的content uri了，所以直接传参进去就可以了
                break;
            case UCrop.REQUEST_CROP:
                if (data != null) {
                    final Uri resultUri = UCrop.getOutput(data);  //返回的是file类型的uri
                    if (resultUri != null) {
                        viewModel.updateUserPortrait(resultUri.getPath());
                    } else {
                        ToastUtil.showShort(this, "图片剪裁失败");
                    }
                }
                break;

            case UCrop.RESULT_ERROR:
                ToastUtil.showShort(this, "剪切失败");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_action_bar, menu);
        MenuItem item = menu.getItem(0);
        item.setTitle("编辑");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu1:
                Intent intent = new Intent(UserInfoThisActivity.this, UserInfoEditActivity.class);
                startActivityForResult(intent, 4);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToast(String msg) {
        runOnUiThread(() -> ToastUtil.showShort(getBaseContext(), msg));
    }
}
