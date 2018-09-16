package com.lfork.a98620.lfree.goodsuploadupdate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.bindingadapter.ImageBinding;
import com.lfork.a98620.lfree.base.customview.PopupDialog;
import com.lfork.a98620.lfree.base.customview.PopupDialogOnclickListener;
import com.lfork.a98620.lfree.base.image.GlideEngine;
import com.lfork.a98620.lfree.databinding.GoodsUploadActBinding;
import com.lfork.a98620.lfree.main.MainActivity;
import com.lfork.a98620.lfree.util.ToastUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoodsUploadUpdateActivity extends AppCompatActivity implements GoodsUploadUpdateNavigator {

    private static final int REQUEST_CODE_CHOOSE = 1; //相册 多张选择

    private static final int REQUESTCODE_CAM = 2;//相机 单张拍摄

    private GoodsUploadUpdateViewModel viewModel;

    private GoodsUploadActBinding binding;

    private static final String TAG = "GoodsUploadUpdate";

    private List<ImageView> imageViews = new ArrayList<>();
    private File portraitFile;

    private Uri imageUri;

    private int tempImageCount;

    int launchType,goodsId,categoryId;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        launchType = intent.getIntExtra("launch_type", 0);
        goodsId = intent.getIntExtra("goods_id", 0);
        categoryId = intent.getIntExtra("category_id", 0);

        if (launchType == 0) {
            Log.d(TAG, "onCreate: 启动参数有误");
            finish();
            return;
        }



        binding = DataBindingUtil.setContentView(this, R.layout.goods_upload_act);
        viewModel = new GoodsUploadUpdateViewModel(getApplicationContext());
        binding.setViewModel(viewModel);
        initActionBar();
        setupSpinner();
        if (imageViews.size() < 1) {
            imageViews.add(binding.image1);
            imageViews.add(binding.image2);
            imageViews.add(binding.image3);
            imageViews.add(binding.image4);
            imageViews.add(binding.image5);
        }
        /*
          避免滑动冲突
         */
        binding.editDescription.setOnTouchListener((view, motionEvent) -> {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.start(launchType, goodsId, categoryId);
            viewModel.setNavigator(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null)
         viewModel.onDestroy();
    }



    private void setupSpinner(){
        Spinner spinner = binding.spinner;
//        spinner.setAdapter();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setCategoryId(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayShowTitleEnabled(true);
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
//                viewModel.uploadGoods();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.common_action_bar, menu);
//        MenuItem item = menu.getItem(0);
//        item.setTitle("完成");
        return true;
    }

    List<Uri> mSelected = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE:
                if (resultCode == RESULT_OK) {
                    if (mSelected != null) {
                        mSelected.addAll(Matisse.obtainResult(data));
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
        Matisse.from(GoodsUploadUpdateActivity.this)
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
        if (mSelected == null) {
            mSelected = new ArrayList<>();
        }
        mSelected.add(imageUri);
        ImageBinding.setImage(imageViews.get(mSelected.size() - 1), imageUri);
        viewModel.setImage(portraitFile.getPath());
    }

    /**
     * 刷新imageView
     */
    public void setImages() {
        for (int i = 0; i < mSelected.size(); i++) {
            ImageBinding.setImage(imageViews.get(i), mSelected.get(i));
        }
        viewModel.setImages(mSelected);
    }

    @Override
    public void deleteImage(int index){
        //mSelected -1
        Log.d(TAG, "deleteImage: " + index);
        mSelected.remove(index);


        //重新设置image
        //更新viewModel里面的image的路径
        setImages();

    }

    @Override
    public void showMyDialog(int count) {
        tempImageCount = count;
        new PopupDialog(this, new PopupDialogOnclickListener() {
            @Override
            public void onFirstButtonClicked(PopupDialog dialog) {
                int checkCallPhonePermission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) + ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.showLong(getApplicationContext(), "请允许程序访问您的SD卡，以便上传图片^v^");
                    ActivityCompat.requestPermissions(GoodsUploadUpdateActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                } else {
                    openCamera();
                }
            }

            @Override
            public void onSecondButtonClicked(PopupDialog dialog) {
                if (ContextCompat.checkSelfPermission(GoodsUploadUpdateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //6.0及以上的系统在使用危险权限的时候必须进行运行时权限处理
                    ActivityCompat.requestPermissions(GoodsUploadUpdateActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                   selectImages(count);
                }
            }

            @Override
            public void onCanceledClicked(PopupDialog dialog) {

            }
        }, binding.getRoot()).show();

    }

    @Override
    public void setDefaultCategory(int category) {
        runOnUiThread(() -> {
            binding.spinner.setSelection(category);
        });

    }

    @Override
    public void setImages(List<Uri> images) {
        runOnUiThread(() -> {
            mSelected.addAll(images);
            tempImageCount = mSelected.size();
            setImages();
        });
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
                    Toast.makeText(this, "很遗憾你把相机权限禁用了", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImages(tempImageCount);
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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

    @Override
    public void uploadSucceed(String msg) {
        runOnUiThread(() -> {
            ToastUtil.showLong(getApplicationContext(), msg);
            Intent intent = new Intent();
            Log.d(TAG, "uploadSucceed: " +viewModel.getCategoryId() );
            intent.putExtra("category", viewModel.getCategoryId() - 1);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public void uploadFailed(String log) {
        runOnUiThread(() -> {
            ToastUtil.showShort(getApplicationContext(), log);
        });

    }


    public static void openUploadActivityForResult(Activity context){
        Intent intent = new Intent(context, GoodsUploadUpdateActivity.class);
        intent.putExtra("launch_type", MainActivity.CODE_UPLOAD);
        context.startActivityForResult(intent, MainActivity.CODE_UPLOAD);

    }

    public static void openUpdateActivityForResult(Activity context,int goodsId, int categoryId){
        Intent intent = new Intent(context, GoodsUploadUpdateActivity.class);
        intent.putExtra("launch_type", MainActivity.CODE_UPDATE);
        intent.putExtra("goods_id", goodsId);
        intent.putExtra("category_id", categoryId);
        context.startActivityForResult(intent, MainActivity.CODE_UPDATE);
    }


    @Override
    public void showToast(String msg) {
        runOnUiThread(() -> {
            ToastUtil.showShort(getApplicationContext(), msg);
        });
    }
}