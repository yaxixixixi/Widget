package com.example.treemenu;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.treemenu.dao.DaoSession;
import com.example.treemenu.dao.DataDao;


import java.util.ArrayList;
import java.util.List;

/**
 * @author yaxi
 * @date 2018/8/31
 */
public class PopupTreeMenu extends PopupWindow implements View.OnTouchListener {

    private DaoSession mDaoSession;
    private RecyclerView titleRecycler, contentRecycler;
    private List<Data> mDataTitles;
    private List<List<Data>> mAll;
    private Context mContext;

    private int serialNumber = 0;
    private ContentAdapter mContentAdapter;
    private final TitleAdapter mTitleAdapter;

    public PopupTreeMenu(Context context, App app) {
        mContext = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.popup_tree_menu, null);
        setContentView(inflate);

        mAll = new ArrayList<>();
        mDaoSession = app.getDaoSession();

        List<Data> list = mDaoSession.queryBuilder(Data.class).where(DataDao.Properties.BelongTo.lt(0))
                .orderAsc(DataDao.Properties.Id).build().list();
        mAll.add(list);
        mDataTitles = new ArrayList<>();
        Data fData = new Data(0L, "请选择", -123456D, -123456L);
        fData.setSelected(0);
        mDataTitles.add(fData);


        titleRecycler = inflate.findViewById(R.id.popup_recycler_title);
        contentRecycler = inflate.findViewById(R.id.popup_recycler_content);

        LinearLayoutManager titleManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager contentManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);


        mTitleAdapter = new TitleAdapter(mDataTitles);
        mContentAdapter = new ContentAdapter(list);

        mTitleAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mTitleAdapter.isFirstOnly(false);

        mContentAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mContentAdapter.isFirstOnly(false);

        titleRecycler.setLayoutManager(titleManager);
        contentRecycler.setLayoutManager(contentManager);

        titleRecycler.setAdapter(mTitleAdapter);
        contentRecycler.setAdapter(mContentAdapter);

        inflate.setOnTouchListener(this);
    }

    public PopupTreeMenu buildView() {
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(800);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.PopupWindowAnimStyle);
        setFocusable(true);
        setOutsideTouchable(true);
        return this;
    }

    public void show(View parentView) {
        showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    private class TitleAdapter extends BaseQuickAdapter<Data, BaseViewHolder> {

        public TitleAdapter(@Nullable List<Data> data) {
            super(R.layout.item_popup_title, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Data item) {
            helper.setText(R.id.item_title, item.getName());
            helper.setGone(R.id.item_bottom, item.getSelected() == 1);
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serialNumber = helper.getAdapterPosition();
                    showSpecifiedList(serialNumber);
                }
            });
        }
    }

    private class ContentAdapter extends BaseQuickAdapter<Data, BaseViewHolder> {

        public ContentAdapter(@Nullable List<Data> data) {
            super(R.layout.item_popup_content, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Data item) {
            helper.setText(R.id.item_title, item.getName());
            helper.setText(R.id.item_price, String.valueOf(item.getPrice()));
            helper.setGone(R.id.item_price, item.getPrice() > 0);
            if (item.getSelected() == 1) {
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.back));
            }
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Data data : mAll.get(serialNumber)) {
                        data.setSelected(0);
                    }
                    item.setSelected(1);
                    List<Data> list = mDaoSession.queryBuilder(Data.class).where(DataDao.Properties.BelongTo.eq(item.getId()))
                            .orderAsc(DataDao.Properties.Id).build().list();
                    if (list.size() == 0) {
                        return;
                    }
                    mAll.add(list);
                    setNewData(list);
                    ContentAdapter.this.notifyDataSetChanged();
                    if (serialNumber < mDataTitles.size() - 1) {
                        mTitleAdapter.setData(serialNumber, item);
                    } else {
                        mTitleAdapter.addData(serialNumber, item);
                    }
                    serialNumber++;
                }
            });
        }
    }


    private boolean backToPreviousList() {
        if (serialNumber > 0) {
            serialNumber--;
            mContentAdapter.setNewData(mAll.get(serialNumber));
            mContentAdapter.notifyDataSetChanged();
            return true;
        } else {
            return false;
        }
    }

    private boolean showSpecifiedList(int serialNumber) {
        if (serialNumber < mAll.size()) {
            mContentAdapter.setNewData(mAll.get(serialNumber));
            mContentAdapter.notifyDataSetChanged();
            return true;
        } else {
            return false;
        }
    }
}
