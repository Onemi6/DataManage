package com.hzlf.sampletest.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.entityclass.UploadImg;
import com.hzlf.sampletest.http.UploadUtil;
import com.hzlf.sampletest.http.UploadUtil.OnUploadProcessListener;
import com.hzlf.sampletest.others.ImgAdapter;
import com.hzlf.sampletest.others.MyApplication;
import com.hzlf.sampletest.others.UsedPath;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;

public class ImgUploadActivity extends Activity implements OnClickListener,
        OnUploadProcessListener {
    //去上传文件
    protected static final int TO_UPLOAD_FILE = 1;
    //上传文件响应
    protected static final int UPLOAD_FILE_DONE = 2;
    //上传初始化
    private static final int UPLOAD_INIT_PROCESS = 4;
    //上传中
    private static final int UPLOAD_IN_PROCESS = 5;

    private static final int REQUEST_IMAGE = 2;

    private static String requestURL = UsedPath.api_ImgUpload_POST;
    private Button selectButton, uploadButton;

    private int num = 0, fail_num = 0;

    private DBManage dbmanage = new DBManage(this);
    private Spinner sp_img_type;
    private List<String> selectPaths = new ArrayList<String>(), picList = new ArrayList<String>(),
            status = new ArrayList<String>();
    private ArrayAdapter ada_img_type;
    private RecyclerView rv_add_img;
    private GridLayoutManager layoutmanager;
    private ImgAdapter adapter_img;
    private Context _context;
    private String img_type = null, number = null, picPath;
    private int pos;

    private ProgressDialog mypDialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_img_upload);
        _context = this;
        initView();
    }

    /**
     * 初始化数据
     */
    private void initView() {
        selectButton = findViewById(R.id.selectImage);
        uploadButton = findViewById(R.id.uploadImage);
        sp_img_type = findViewById(R.id.spinner_img_type);
        rv_add_img = findViewById(R.id.rv_img_add);
        selectButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        num = 0;
        number = ((MyApplication) getApplication()).getNumber();

        ada_img_type = ArrayAdapter.createFromResource(_context, R.array.IMG_TYPE, android.R
                .layout.simple_spinner_dropdown_item);
        sp_img_type.setAdapter(ada_img_type);

        //GridLayoutManager 对象 这里使用 GridLayoutManager 是网格布局的意思
        layoutmanager = new GridLayoutManager(this, 3);
        layoutmanager.setOrientation(GridLayoutManager.VERTICAL);
        //设置RecyclerView 布局
        rv_add_img.setLayoutManager(layoutmanager);
        //设置Adapter
        adapter_img = new ImgAdapter(this, picList);
        rv_add_img.setAdapter(adapter_img);

        adapter_img.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                /*Snackbar.make(view, "点击了第" + (position + 1) + "张", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                String path = picList.get(position);
                if (path != null) {
                    Options opt = new Options();
                    opt.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(path, opt);
                    int imageHeight = opt.outHeight;
                    int imageWidth = opt.outWidth;
                    Display display = getWindowManager().getDefaultDisplay();
                    Point point = new Point();
                    display.getRealSize(point);
                    int screenHeight = point.y;
                    int screenWidth = point.x;
                    int scale = 1;
                    int scaleWidth = imageWidth / screenWidth;
                    int scaleHeigh = imageHeight / screenHeight;
                    if (scaleWidth >= scaleHeigh && scaleWidth > 1) {
                        scale = scaleWidth;
                    } else if (scaleWidth < scaleHeigh && scaleHeigh > 1) {
                        scale = scaleHeigh;
                    }
                    opt.inSampleSize = scale;
                    opt.inJustDecodeBounds = false;
                    Bitmap bm = BitmapFactory.decodeFile(path, opt);
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.img_item,
                            (ViewGroup) findViewById(R.id.dialog_img));
                    ImageView imageview = layout
                            .findViewById(R.id.imageView);
                    imageview.setImageBitmap(bm);
                    AlertDialog.Builder dialog_img = new AlertDialog.Builder(
                            ImgUploadActivity.this).setView(layout)
                            .setPositiveButton("确定", null);
                    dialog_img.show();
                }
            }
        });

        adapter_img.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                //Snackbar.make(view, "长按了第" + (position + 1) + "行", Snackbar.LENGTH_LONG)
                //   .setAction("Action", null).show();
                pos = position;
                // 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        _context);
                // 设置Title的图标
                builder.setIcon(R.drawable.ic_launcher);
                // 设置Title的内容
                builder.setTitle("提示");
                // 设置Content来显示一个信息
                builder.setMessage("确定删除第" + (position + 1) + "张图片?");
                // 设置一个PositiveButton
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //dialog.dismiss();
                                try {
                                    adapter_img.removeItem(pos);
                                    Toast.makeText(_context, "删除图片成功",
                                            Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    // TODO 自动生成的 catch 块
                                    e.printStackTrace();
                                    Toast.makeText(_context, "删除图片失败",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                // 设置一个NegativeButton
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //dialog.dismiss();
                            }
                        });
                // 显示出该对话框
                builder.show();
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
                mypDialog = new ProgressDialog(ImgUploadActivity.this);
                // 实例化
                mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                // 设置进度条风格，风格为圆形，旋转的
                mypDialog.setTitle("上传图片中...");
                // 设置ProgressDialog 标题
                mypDialog.setIndeterminate(false);
                // 设置ProgressDialog 的进度条是否不明确
                mypDialog.setCancelable(false);
                // 设置ProgressDialog 是否可以按退回按键取消
                mypDialog.show();
                // 让ProgressDialog显示
                img_type = sp_img_type.getSelectedItem().toString();
                if (adapter_img.getImgList().size() > 0) {
                    picList = adapter_img.getImgList();
                    for (String onepath : picList) {
                        picPath = onepath;
                        if (picPath != null) {
                            toUploadFile();
                            handler.sendEmptyMessage(TO_UPLOAD_FILE);
                            // Log.e("picPath", picPath);
                        } else {
                            Toast.makeText(this, "上传的文件路径出错", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                } else if (adapter_img.getImgList().size() == 0) {
                    mypDialog.dismiss();
                    Toast.makeText(this, "无图片", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                selectPaths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                picList.addAll(selectPaths);
            }
            selectPaths = null;
        }
    }

    // 刷新图片
    @Override
    protected void onResume() {
        super.onResume();
        adapter_img.changList_add(picList);
    }

    /**
     * 上传服务器响应回调
     */
    @Override
    public void onUploadDone(int responseCode, String message) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_FILE_DONE;
        msg.arg1 = responseCode;
        msg.obj = message;
        handler.sendMessage(msg);
    }

    private void toUploadFile() {
        String fileKey = "file";
        UploadUtil uploadUtil = UploadUtil.getInstance();
        uploadUtil.setOnUploadProcessListener(this); // 设置监听器监听上传状态

        Map<String, String> params = new HashMap<String, String>();
        /*params.put("id", number);
        params.put("type", img_type);
        params.put("name", ((MyApplication) getApplication()).getName());*/

        String _requestURL = requestURL + "id=" + number + "&type=" + img_type + "&name=" + (
                (MyApplication) getApplication()).getName();
        Log.i("picPath",picPath);
        uploadUtil.uploadFile(picPath, fileKey, _requestURL, params);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /*
                 * case TO_UPLOAD_FILE: toUploadFile(); break;
                 */
                case UPLOAD_FILE_DONE:
                    String result = (String) msg.obj;
                    if (result.equals("上传失败")) {
                        mypDialog.dismiss();
                        Toast.makeText(ImgUploadActivity.this, result,
                                Toast.LENGTH_SHORT).show();
                        status.add("0");
                        fail_num++;
                    } else {
                        Gson gson = new Gson();
                        UploadImg uploadimg = gson
                                .fromJson(result, UploadImg.class);
                        if (uploadimg.getStatus().equals("success")) {
                            status.add("1");
                            if (status.size() >= picList.size()) {
                                mypDialog.dismiss();
                                Toast.makeText(
                                        ImgUploadActivity.this,
                                        "共上传" + picList.size() + "张图片,其中失败"
                                                + fail_num + "张",
                                        Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < picList.size(); i++) {
                                    if (status.get(i).equals("1")) {
                                        dbmanage.addImagePath(number,
                                                picList.get(i));
                                    }
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // do something
                                        Intent intent_details = new Intent();
                                        intent_details.setClass(
                                                ImgUploadActivity.this,
                                                DetailsActivity.class);
                                        intent_details.putExtra("info_number",
                                                number);
                                        finish();// 结束当前活动
                                        ImgUploadActivity.this
                                                .startActivity(intent_details);
                                    }
                                }, 2000); // 延时1s执行
                            }
                        } else {
                            mypDialog.dismiss();
                            status.add("0");
                            fail_num++;
                            Toast.makeText(ImgUploadActivity.this,
                                    uploadimg.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onUploadProcess(int uploadSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_IN_PROCESS;
        msg.arg1 = uploadSize;
        handler.sendMessage(msg);
    }

    @Override
    public void initUpload(int fileSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_INIT_PROCESS;
        msg.arg1 = fileSize;
        handler.sendMessage(msg);
    }
}