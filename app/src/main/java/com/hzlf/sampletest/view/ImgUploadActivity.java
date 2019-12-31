package com.hzlf.sampletest.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.http.NetworkUtil;
import com.hzlf.sampletest.http.eLab_API;
import com.hzlf.sampletest.model.UploadImg;
import com.hzlf.sampletest.others.ImgAdapter;
import com.hzlf.sampletest.others.MyApplication;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImgUploadActivity extends Activity implements OnClickListener {

    private static final int REQUEST_IMAGE = 2;
    private Button uploadButton;
    private DBManage dbmanage = new DBManage(this);
    private Spinner sp_img_type;
    private List<String> picList = new ArrayList<>(), status = new ArrayList<>();
    private RecyclerView rv_add_img;
    private ImgAdapter adapter_img;
    private Context _context;
    private int fail_num = 0, picNum;
    private String img_type = null, number = null;
    private SharedPreferences sharedPreferences;
    private BuildBean dialog_ImgUpload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_img_upload);
        initView();
    }

    private void initView() {
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        _context = this;
        Button selectButton = findViewById(R.id.selectImage);
        uploadButton = findViewById(R.id.uploadImage);
        sp_img_type = findViewById(R.id.spinner_img_type);
        rv_add_img = findViewById(R.id.rv_img_add);
        selectButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);

        ArrayAdapter ada_img_type = ArrayAdapter.createFromResource(_context, R.array.IMG_TYPE,
                android.R
                        .layout.simple_spinner_dropdown_item);
        sp_img_type.setAdapter(ada_img_type);

        //GridLayoutManager 对象 这里使用 GridLayoutManager 是网格布局的意思
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        //设置RecyclerView 布局
        rv_add_img.setLayoutManager(layoutManager);
        //设置Adapter
        adapter_img = new ImgAdapter(this, picList);
        rv_add_img.setAdapter(adapter_img);

        adapter_img.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(position);
            }
        });

        adapter_img.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectImage:
                /*MultiImageSelector.create()
                        .showCamera( boolean) // 是否显示相机. 默认为显示
                 .count( int) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                  .single() // 单选模式
                    .multi() // 多选模式, 默认模式;
                    .origin(ArrayList < String >) // 默认已选择图片. 只有在选择模式为多选时有效
                    .start(Activity / Fragment, REQUEST_IMAGE);*/
                MultiImageSelector.create()
                        .start(ImgUploadActivity.this, REQUEST_IMAGE);
                break;
            case R.id.uploadImage:
                uploadImage();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            List<String> selectPaths;
            if (resultCode == RESULT_OK) {
                selectPaths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                picList.addAll(selectPaths);
            }
            //selectPaths = null;
        }
    }

    // 刷新图片
    @Override
    protected void onResume() {
        super.onResume();
        adapter_img.changList_add(picList);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void attemptImgUpload(String path) {
        if (NetworkUtil.isNetworkAvailable(_context)) {
            eLab_API request = HttpUtils.GsonApi();
            String token;
            if (((MyApplication) getApplication()).getToken() == null) {
                token = "Bearer " + sharedPreferences.getString("token", "");
            } else {
                token = "Bearer " + ((MyApplication) getApplication()).getToken();
            }
            number = ((MyApplication) getApplication()).getNumber();
            Map<String, String> params = new HashMap<>();
            params.put("id", number);
            params.put("type", img_type);

            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", path,
                    requestFile);
            Call<UploadImg> call = request.ApplyImgUpload(token, params, body);
            call.enqueue(new Callback<UploadImg>() {
                @Override
                public void onResponse(Call<UploadImg> call, Response<UploadImg> response) {
                    if (response.code() == 401) {
                        Log.v("ImgUpload请求", "token过期");
                        Intent intent_login = new Intent();
                        intent_login.setClass(ImgUploadActivity.this,
                                LoginActivity.class);
                        intent_login.putExtra("login_type", 1);
                        DialogUIUtils.dismiss(dialog_ImgUpload);
                        startActivity(intent_login);
                    } else if (response.code() == 200) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("success")) {
                                status.add("1");
                                Log.v("图片上传成功", response.body().getMessage());
                            } else {
                                status.add("0");
                                fail_num++;
                                Log.v("图片上传失败", response.body().getMessage());
                            }
                        } else {
                            Log.v("ImgUpload请求成功!", "response.body is null");
                        }
                        dialog_ImgUpload = DialogUIUtils.showLoading(_context, "已上传 " +
                                status.size() + "/" + picNum, false, true, false, false);
                        dialog_ImgUpload.show();
                        if (status.size() >= picList.size()) {
                            DialogUIUtils.dismiss(dialog_ImgUpload);
                            Snackbar.make(uploadButton, "共上传" + picList.size() + "张图片,其中失败" +
                                            fail_num + "张",
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            for (int i = 0; i < picList.size(); i++) {
                                if (status.get(i).equals("1")) {
                                    dbmanage.addImagePath(number, picList.get(i));
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<UploadImg> call, Throwable t) {
                    Log.v("ImgUpload请求失败!", t.getMessage());
                }
            });
        } else {
            Snackbar.make(uploadButton, "当前无网络",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void ImgOnClick(int pos) {
        String path = picList.get(pos);
        showImage(path);
    }

    public void ImgOnLongClick(final int pos) {
        // 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        // 设置Title的图标
        builder.setIcon(R.drawable.ic_launcher);
        // 设置Title的内容
        builder.setTitle("提示");
        // 设置Content来显示一个信息
        builder.setMessage("确定删除第" + (pos + 1) + "张图片?");
        // 设置一个PositiveButton
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        try {
                            adapter_img.removeItem(pos);
                            Snackbar.make(rv_add_img, "图片删除成功",
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        } catch (Exception e) {
                            // TODO 自动生成的 catch 块
                            e.printStackTrace();
                            Snackbar.make(rv_add_img, "图片删除失败",
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }
                });
        // 设置一个NegativeButton
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });
        // 显示出该对话框
        builder.show();
    }

    public void uploadImage() {
        img_type = sp_img_type.getSelectedItem().toString();
        if (adapter_img.getImgList().size() > 0) {
            picList = adapter_img.getImgList();
            picNum = picList.size();
            for (String onePic : picList) {
                if (onePic != null) {
                    try {
                        Thread.sleep(300);
                        pic_compress(onePic);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(uploadButton, "上传的文件路径出错",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1500); // 延时1s执行
        } else if (adapter_img.getImgList().size() == 0) {
            Snackbar.make(rv_add_img, "至少选择一张图片",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void showImage(String path) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.img_item,
                (ViewGroup) findViewById(R.id.dialog_img));
        ImageView imageview = layout
                .findViewById(R.id.imageView);
        AlertDialog.Builder dialog_img = new AlertDialog.Builder(
                ImgUploadActivity.this).setView(layout)
                .setPositiveButton("确定", null);
        dialog_img.show();
        Glide.with(_context).load(path)
                .placeholder(R.drawable.logo)
                .error(R.drawable.error)
                .into(imageview);
    }

    public void pic_compress(String filePath) {
        /*1、quality-压缩质量，默认为76
        2、isKeepSampling-是否保持原数据源图片的宽高
        3、fileSize-压缩后文件大小
        4、outfile-压缩后文件存储路径
        如果不配置，Tiny内部会根据默认压缩质量进行压缩，压缩后文件默认存储在：
        ExternalStorage/Android/data/${packageName}/tiny/目录下*/
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source(filePath).asFile().withOptions(options).compress(new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile) {
                //return the compressed file path
                //Log.v("压缩图片", outfile);
                attemptImgUpload(outfile);
            }
        });
    }
}