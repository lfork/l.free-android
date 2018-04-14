package com.lfork.a98620.lfree.goodsupload;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.GoodsUploadActBinding;
import com.lfork.a98620.lfree.util.customview.PopupDialog;
import com.lfork.a98620.lfree.util.customview.PopupDialogOnclickListener;
import com.lfork.a98620.lfree.util.image.GlideEngine;
import com.lfork.a98620.lfree.util.mvvmadapter.Image;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoodsUploadActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE = 1; //相册 多张选择

    private static final int REQUESTCODE_CAM = 2;//相机 单张拍摄

    private GoodsUploadViewModel viewModel;

    private GoodsUploadActBinding binding;

    private List<ImageView> imageViews = new ArrayList<>();
    private File portraitFile;
    private Uri imageUri;

    private int imageViewIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.goods_upload_act);
        viewModel = new GoodsUploadViewModel(this);
        binding.setViewModel(viewModel);
        initActionBar();

        if (imageViews.size() < 1) {
            imageViews.add(binding.image1);
            imageViews.add(binding.image2);
            imageViews.add(binding.image3);
            imageViews.add(binding.image4);
            imageViews.add(binding.image5);
        }
    }


    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("宝贝上传");
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
                viewModel.uploadGoods();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_action_bar, menu);
        MenuItem item = menu.getItem(0);
        item.setTitle("完成");
        return true;
    }

    List<Uri> mSelected;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE:
                if (resultCode == RESULT_OK) {
                    if (mSelected != null) {
                        mSelected.addAll( Matisse.obtainResult(data));
                    } else {
                        mSelected = Matisse.obtainResult(data);
                    }
                    Log.d("Matisse", "mSelected: " + mSelected);
                    setImages();
                }
                break;
            case REQUESTCODE_CAM:
                if (portraitFile.length() < 1) {
                    return;
                }
                setImage();
                break;
        }


    }

    public void selectImages(int count) {
        Matisse.from(GoodsUploadActivity.this)
                .choose(MimeType.of(MimeType.PNG, MimeType.GIF, MimeType.JPEG))
                .countable(true)
                .maxSelectable(count)
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .theme(R.style.Matisse_Dracula)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    public void setImage(){
        if (imageViewIndex == -1) {
            return;
        }
        mSelected.add(imageUri);
        Image.setImage(imageViews.get(imageViewIndex), imageUri);
        imageViewIndex = -1;
        viewModel.setImage(portraitFile.getPath());
    }

    public void setImages() {
        for (int i = imageViewIndex; i < mSelected.size(); i++) {
            Image.setImage(imageViews.get(i), mSelected.get(i));
        }
        viewModel.setImages(mSelected);

    }

    public void showMyDialog(int count, int index) {
        imageViewIndex = index;
        new PopupDialog(this, new PopupDialogOnclickListener() {
            @Override
            public void onFirstButtonClicked(PopupDialog dialog) {
                int checkCallPhonePermission = ActivityCompat.checkSelfPermission(GoodsUploadActivity.this, Manifest.permission.CAMERA);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(GoodsUploadActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                } else {
                    openCamera();
                }
            }

            @Override
            public void onSecondButtonClicked(PopupDialog dialog) {
                if (ContextCompat.checkSelfPermission(GoodsUploadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //6.0及以上的系统在使用危险权限的时候必须进行运行时权限处理
                    ActivityCompat.requestPermissions(GoodsUploadActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                   selectImages(count);
                }
            }

            @Override
            public void onCanceledClicked(PopupDialog dialog) {

            }
        }, binding.getRoot()).show();


    }

    private void openCamera() {
        //创建file对象用于储存拍摄的照片
        portraitFile = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");

        try {
            if (portraitFile.exists()) {
                portraitFile.delete();
            }
            portraitFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.lfork.a98620.lfree.fileprovider", portraitFile);
        } else {
            imageUri = Uri.fromFile(portraitFile);
        }

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //将url关联到相机拍摄的图片上面
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUESTCODE_CAM);
    }

}


