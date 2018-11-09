package com.hzlf.sampletest.others;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzlf.sampletest.R;

import java.util.List;

public class ImgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View
        .OnClickListener, View.OnLongClickListener {
    private static final int VIEW_TYPE = -1;
    private List<String> imgList;
    private Activity mactivity;
    private ImgAdapter.OnClickListener mOnClickListener = null;
    private ImgAdapter.OnLongClickListener mOnLongClickListener = null;
    private int defItem = -1;

    public ImgAdapter(Activity activity, List<String> imgList) {
        this.mactivity = activity;
        this.imgList = imgList;
    }

    //加载item 的布局  创建ViewHolder实例
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View emptyview = LayoutInflater.from(mactivity).inflate(R.layout.rv_empty, parent, false);
        View view = LayoutInflater.from(mactivity).inflate(R.layout.img_item, parent, false);
        if (VIEW_TYPE == viewType) {
            return new ImgAdapter.EmptyViewHolder(emptyview);
        }
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ImgAdapter.ViewHolder(view);
    }

    //对RecyclerView子项数据进行赋值
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ImgAdapter.ViewHolder) {
            String picPath = imgList.get(position);

            if (!TextUtils.isEmpty(picPath)) {
                Options opt = new Options();
                opt.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picPath, opt);
                int imageHeight = opt.outHeight;
                int imageWidth = opt.outWidth;

                Display display = mactivity.getWindowManager().getDefaultDisplay();
                Point point = new Point();
                // 该方法已过时，使用getRealSize()方法替代。也可以使用getSize()，但是不能准确的获取到分辨率
                // int screenHeight = display.getHeight();
                // int screenWidth = display.getWidth();
                display.getRealSize(point);
                int screenHeight = point.y;
                int screenWidth = point.x;

                int scale = 1;
                int scaleWidth = imageWidth / screenWidth / 3;
                int scaleHeigh = imageHeight / screenHeight;
                if (scaleWidth >= scaleHeigh && scaleWidth > 1) {
                    scale = scaleWidth;
                } else if (scaleWidth < scaleHeigh && scaleHeigh > 1) {
                    scale = scaleHeigh;
                }
                opt.inSampleSize = scale;
                opt.inJustDecodeBounds = false;
                Bitmap bm = BitmapFactory.decodeFile(picPath, opt);
                ((ViewHolder) holder).img_add.setImageBitmap(bm);
                ((ViewHolder) holder).itemView.setTag(position);
            }
        } else if (holder instanceof ImgAdapter.EmptyViewHolder) {
            ((EmptyViewHolder) holder).mEmptyTextView.setText("没有图片");
        }

    }

    //返回子项个数
    @Override
    public int getItemCount() {
        //获取传入adapter的条目数，没有则返回 1
        return imgList.size() > 0 ? imgList.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (imgList.size() <= 0) {
            return VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    public List<String> getImgList() {
        return this.imgList;
    }

    /*public Info_Detail getItem(int position) {
        this.defItem = position;
        notifyDataSetChanged();
        return maininfoList.get(position);
    }*/

    public void removeItem(int position) {
        this.imgList.remove(position);
        notifyDataSetChanged();
    }

    public void changList_add(List<String> imgList) {
        this.imgList = imgList;
        notifyDataSetChanged();
    }


    /****************************************
     * Listener
     */

    public void setOnClickListener(ImgAdapter.OnClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (null != mOnClickListener) {
            mOnClickListener.onClick(view, (int) view.getTag());
        }
    }

    public void setOnLongClickListener(ImgAdapter.OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    @Override
    public boolean onLongClick(View view) {
        if (null != mOnLongClickListener) {
            mOnLongClickListener.onLongClick(view, (int) view.getTag());
        }
        // 消耗事件，否则长按逻辑执行完成后还会进入点击事件的逻辑处理
        return true;
    }

    /**
     * 手动添加点击事件
     */
    public interface OnClickListener {
        void onClick(View view, int position);
    }

    /**
     * 手动添加长按事件
     */
    public interface OnLongClickListener {
        void onLongClick(View view, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_add;

        public ViewHolder(View view) {
            super(view);
            img_add = view.findViewById(R.id.imageView);
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        private TextView mEmptyTextView;

        public EmptyViewHolder(View view) {
            super(view);
            mEmptyTextView = view.findViewById(R.id.rv_empty_text);
        }
    }
}
