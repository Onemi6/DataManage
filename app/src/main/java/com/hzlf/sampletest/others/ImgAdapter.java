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

import com.bumptech.glide.Glide;
import com.hzlf.sampletest.R;

import java.util.List;

public class ImgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View
        .OnClickListener, View.OnLongClickListener {
    private static final int VIEW_TYPE = -1;
    private List<String> imgList;
    private Activity mActivity;
    private ImgAdapter.OnClickListener mOnClickListener = null;
    private ImgAdapter.OnLongClickListener mOnLongClickListener = null;
    private int defItem = -1;

    public ImgAdapter(Activity activity, List<String> imgList) {
        this.mActivity = activity;
        this.imgList = imgList;
    }

    //加载item 的布局  创建ViewHolder实例
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View emptyView = LayoutInflater.from(mActivity).inflate(R.layout.rv_empty, parent, false);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.img_item, parent, false);
        if (VIEW_TYPE == viewType) {
            return new ImgAdapter.EmptyViewHolder(emptyView);
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
                Glide.with(mActivity).load(picPath)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.error)
                        .into(((ViewHolder) holder).img_add);
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
