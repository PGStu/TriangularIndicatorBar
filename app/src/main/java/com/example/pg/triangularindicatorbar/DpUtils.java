package com.example.pg.triangularindicatorbar;

import android.content.res.Resources;

public class DpUtils {
        /**
         * dp转px
         *
         * @param dipValue
         * @return
         */
        public static int dip2px(float dipValue) {
            final float scale = Resources.getSystem().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        }

        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         *
         * @param pxValue
         * @return
         */
        public static int px2dip(float pxValue)
        {
            final float scale = Resources.getSystem().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }
}
