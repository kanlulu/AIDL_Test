package com.kanlulu.aidl_test.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * dp,px,sp转换
 * Created by lingxiaoming on 2016/8/4 0004.
 */
public class PixelUtils {
    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }



}
