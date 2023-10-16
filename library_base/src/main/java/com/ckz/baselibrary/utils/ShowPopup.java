package com.ckz.baselibrary.utils;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.ckz.library_base.R;


/**
 * Created by Administrator on 2018/3/13.
 */

public class ShowPopup {

    private  ShowPopup showPopup;

    private Activity activity;

    private LayoutInflater inflater;

    private View popView;

    private PopupWindow popupWindow;

    private LinearLayout simpleView;


//    /**
//     * 初始化
//     * @param activity
//     */
//    public static ShowPopup getInstance(Activity activity){
//        if (showPopup == null){
//            synchronized (ShowPopup.class){
//                showPopup = new ShowPopup(activity);
//            }
//        }
//        return showPopup;
//    }

    public ShowPopup(Activity activity){
        this.activity = activity;
    }



    /**
     * 输入布局文件，设置popwindow
     * @param layoutId
     * @return
     */
    public ShowPopup createLayoutPopupWindow(int layoutId){
        inflater = LayoutInflater.from(activity);
        popView = inflater.inflate(layoutId,null);
        if (popupWindow==null){
            popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindow.setBackgroundDrawable(dw);
        return this;
    }

    public ShowPopup createLayoutPopupWindow(View view){

        return createLayoutPopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public ShowPopup createLayoutPopupWindow(View view,float alpha){

        return createLayoutPopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT,alpha);
    }
    public ShowPopup createLayoutPopupWindow(View view,int height){

        return createLayoutPopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,height,0);
    }

    public ShowPopup createLayoutPopupWindow(View view,int height,float alpha){

        return createLayoutPopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,height,alpha);
    }
    public ShowPopup createLayoutPopupWindow(View view,int width,int height,float alpha){
        popView = view;
        if (popupWindow==null){
            popupWindow = new PopupWindow(popView, width, height);
        }else {
            popupWindow.setContentView(popView);
        }
        setWindowAlpha(alpha);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        return this;
    }

    private ShowPopup setWindowAlpha(float alpha){
        if(popupWindow!=null){
            setBackgroundAlpha(alpha);
            popupWindow.setOnDismissListener(new OnDismissListener(){

                @Override
                public void onDismiss() {
                    setBackgroundAlpha(1.0f);
                }
            });
        }
        return this;
    }
    public interface OnPositionClickListener {
        void OnPositionClick(PopupWindow popup, View view, int position);
    }
    private OnPositionClickListener positionClickListener;

    public void setPositionClickListener(OnPositionClickListener positionClickListener) {
        this.positionClickListener = positionClickListener;
    }
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) activity).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            ((Activity) activity).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            ((Activity) activity).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        ((Activity) activity).getWindow().setAttributes(lp);
    }
    /**
     * 简易popupWindow，不用输入布局
     * @param btnName
     * @return
     */
    public ShowPopup createSimplePopupWindow(String... btnName){
        simpleView = new LinearLayout(activity);
        simpleView.setGravity(Gravity.CENTER_HORIZONTAL);
        simpleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        simpleView.setOrientation(LinearLayout.VERTICAL);
        simpleView.setBackgroundColor(Color.WHITE);
        simpleView.setPadding(10,8,10,0);
        for (int i = 0;i<btnName.length;i++){
            TextView button = new TextView(activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            button.setLayoutParams(params);
            button.setGravity(Gravity.CENTER);
            button.setText(btnName[i]);
            button.setTextSize(18.0f);
            button.setPadding(0,25,0,25);
            button.setBackgroundColor(Color.parseColor("#ffffff"));
            simpleView.addView(button);
            View view = new View(activity);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1));
            view.setBackgroundColor(Color.parseColor("#D1D1D1"));
            simpleView.addView(view);
            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positionClickListener.OnPositionClick(popupWindow,view, finalI);
                }
            });
        }

        popupWindow = new PopupWindow(simpleView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindow.setBackgroundDrawable(dw);

        return this;
    }



    /**
     * 设置弹窗动画
     * @param animId
     * @return showPopu
     */
    public ShowPopup setAnim(int animId){
        if (popupWindow!=null){
            popupWindow.setAnimationStyle(animId);
        }
        return this;
    }
    /**
     * 设置默认动画
     */
    public ShowPopup defaultAnim(){
        if (popupWindow!=null){
            Log.d("showPopup","动画");
            popupWindow.setAnimationStyle(R.style.Animation);
        }
        return this;
    }

    /**
     * 将弹窗设置在底部
     * @param parent
     * @return
     */
    public ShowPopup atBottom(View parent){
        if (popupWindow!=null &&!popupWindow.isShowing()){

            Log.d("showPopup","弹窗");
            popupWindow.showAtLocation(parent, Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL,0,0);
        }
        return this;
    }

    /**
     * 设置悬浮框位置，偏移量
     * @param paren
     * @param x
     * @param y
     * @return
     */
    public ShowPopup dropDown(View paren, int x, int y){
        if (popupWindow!=null){
            popupWindow.showAsDropDown(paren,x,y);
        }
        return this;
    }

    public boolean isShowing(){
        if (popupWindow!=null){
            return popupWindow.isShowing();
        }
        return false;
    }

    /**
     * 设置弹窗的位置以及偏移量
     * @param parent
     * @param gravity
     * @param x
     * @param y
     * @return
     */
    public ShowPopup atLocation(View parent, int gravity, int x, int y){
        if (popupWindow!=null){
            popupWindow.showAtLocation(parent,gravity,x,y);
        }
        return this;
    }


    /**
     * 设置布局文件中控件的点击事件
     * @param id
     * @param listener
     * @return
     */
    public ShowPopup setClick(int id,View.OnClickListener listener){
        if (popView!=null){
            popView.findViewById(id).setOnClickListener(listener);
        }
        return this;
    }


    /**
     * 关闭弹窗的点击事件
     * @param id
     * @return
     */
    public ShowPopup setDismissClick(int id){
        if (popupWindow!=null && popView!=null){
            popView.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });
        }
        return this;
    }

    public void closePopupWindow(){
        if (popupWindow!=null){
            popupWindow.dismiss();
        }
    }

    /**
     * 获取view
     * @return
     */
    public View getView(){
        if (popView!=null){
            return popView;
        }
        return null;
    }

    public PopupWindow getWindow(){
        if(popupWindow!=null){
            return popupWindow;
        }
        return null;
    }

    public void release(){
        if (popupWindow!=null){
            popupWindow.dismiss();
            popupWindow = null;
        }
        showPopup = null;
    }



}
