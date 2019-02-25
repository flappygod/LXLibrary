package com.flappygod.lipo.lxlibrary.Tools;

import android.graphics.Bitmap;

import com.google.zxing.hpQr.BarcodeFormat;
import com.google.zxing.hpQr.MultiFormatWriter;
import com.google.zxing.hpQr.WriterException;
import com.google.zxing.hpQr.common.BitMatrix;

public class BarCodeTool {

    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

    public static Bitmap creatBarcode(String contents, int desiredWidth, int desiredHeight) {
        return creatBarcode(contents, desiredWidth, desiredHeight, BLACK, WHITE);
    }


    /************
     * 创建条码
     * @param contents    文本
     * @param desiredWidth  宽度
     * @param desiredHeight 高度
     * @param colorCode  条码颜色
     * @param colorBG  条码背景
     * @return
     */
    public static Bitmap creatBarcode(String contents, int desiredWidth, int desiredHeight, int colorCode, int colorBG) {
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contents, barcodeFormat, desiredWidth, desiredHeight);
            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? colorCode : colorBG;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            return null;
        }

    }
}
