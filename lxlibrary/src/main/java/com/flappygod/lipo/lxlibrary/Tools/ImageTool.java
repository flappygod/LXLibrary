package com.flappygod.lipo.lxlibrary.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ImageTool {


    /***********
     * 将bitmap进行模糊处理
     * @param sentBitmap  源文件
     * @param context     上下文
     * @param radius      半径
     * @return
     */
    public static Bitmap blurBitMap(Bitmap sentBitmap, Context context, float radius) {
        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }
        return null;
    }


    /***********
     * 获取jsonarray成字符串列表
     * @param str json字符串
     * @return
     */
    public static List<String> getImageArray(String str) {
        List<String> ret = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(str);
            for (int s = 0; s < array.length(); s++) {
                ret.add(array.getString(s));
            }
            return ret;
        } catch (Exception e) {
            if (str.startsWith("http")) {
                ret.add(str);
            }
            return ret;
        }
    }

    /*************
     * 获取第一个
     * @param str  json
     * @return
     */
    public static String getImageFirst(String str) {
        List<String> ret = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(str);
            for (int s = 0; s < array.length(); s++) {
                ret.add(array.getString(s));
            }
            if (ret.size() > 0) {
                return ret.get(0);
            } else {
                return str;
            }
        } catch (Exception e) {
            return str;
        }
    }

}
