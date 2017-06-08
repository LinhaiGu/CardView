package com.glh.cardview.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtil {

    /**
     * dp->px
     *
     * @param _context context
     * @param _dpValue dp
     * @return
     */
    public static float dip2px(Context _context, float _dpValue) {
        float scale = _context.getResources().getDisplayMetrics().density;
        return _dpValue * scale;
    }

    /**
     * 获取 DisplayMetrics
     *
     * @param _context context
     * @return DisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context _context) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        ((Activity) _context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics;
    }


}