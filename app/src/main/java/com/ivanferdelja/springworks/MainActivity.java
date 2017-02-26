package com.ivanferdelja.springworks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    String puzzleSource = "47494638396178001e00f00000ff00ffff00ff21f904000000000021fe2353696e6365207768656e\n" +
            "2069732070696e6b2061207368616465206f6620677261793f002c0000000078001e000002e48c8f\n" +
            "a9cbed0fa39cb4da8bb3deb  _  cfb0f86e24896e689a6eacab6ee0bc7f24  _  c8fc00d24b881\n" +
            "e77c9febfd803b5df140f4  (_)  3d924ae270a8740011c26a3220c432a3  | |  d02813f2edfa\n" +
            "ac374  ___  _ __   _ __  _  _ __    __ _ __      __ ___   _ __ | | __ ___   9165\n" +
            "9d6f  / __|| '_ \\ | '__|| || '_ \\  / _` |\\ \\ /\\ / // _ \\ | '__|| |/ // __|  ed06\n" +
            "36f9  \\__ \\| |_) || |   | || | | || (_| | \\ V  V /| (_) || |   |   < \\__ \\  34b3\n" +
            "daee  |___/| .__/ |_|   |_||_| |_| \\__, |  \\_/\\_/  \\___/ |_|   |_|\\_\\|___/  7d2a\n" +
            "0dd6       | |                      __/ |                                   86c4\n" +
            "feef1b29f  |_|  9b964627a8c08776b  |___/   87447a8d6679848a7f5c7b84028b8885839b5\n" +
            "070719e95       877a666c526893899         27a7b9c9b0f877c9aa2aba63396a4a4b9a1949\n" +
            "f17a2acb8b67dba687e9160c79ab6bc6d91bf84b0ae5991ce6d97c954566e72cd6589d0d56847d1c\n" +
            "1a2d360b6e5efd1dea44ae5df30e1f2f3f4f5f6f7f8f9fafbfcfdfefff0f30a08702003bPinkPink";

    Bitmap bitmap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        final ImageView imageView2 = (ImageView) findViewById(R.id.image2);

        TextView puzzleTextView = (TextView) findViewById(R.id.puzzle);
        puzzleTextView.setText(puzzleSource);

        TextView gifBinaryTextView = (TextView) findViewById(R.id.gifbinary);

        String pureHexGif = removeNonHexChars(puzzleSource);
        Bitmap originalGifBitmap = buildBitmapAndDisplayHex(pureHexGif, gifBinaryTextView);
        if (originalGifBitmap != null) {
            imageView.setImageBitmap(originalGifBitmap);
        }

        // Adjusting the color table
        String adjustedGif1 = adjustColorTableInGif("d3ff00", "ff00ff", pureHexGif);
        bitmap1 = buildBitmapAndDisplayHex(adjustedGif1);
        if (bitmap1 != null) {
            imageView2.setImageBitmap(bitmap1);
        }
    }

    private Bitmap buildBitmapAndDisplayHex(String hexGif) {
        return buildBitmapAndDisplayHex(hexGif, null);
    }

    private Bitmap buildBitmapAndDisplayHex(String hexGif, TextView textView) {
        byte[] gifBytes = hexStringToByteArray(hexGif);
        try {
            String gifBinary = new String(gifBytes, "UTF-8");
            if (textView != null) {
                textView.setText(gifBinary);
            }
            return BitmapFactory.decodeByteArray(gifBytes, 0, gifBytes.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Assumes gif with color table size of 2
    private String adjustColorTableInGif(String newColor1, String newColor2, String gifHex) {
        /*
        474946 - header
        383961 - header
        7800   - width
        1e00   - height
        f00000 - Screen descriptor
        ff00ff - Color #1
        00ff00 - Color #2
        */
        int color1offset = 26;
        StringBuilder sb = new StringBuilder();
        sb.append(gifHex.substring(0, color1offset));
        sb.append(newColor1);
        sb.append(newColor2);
        sb.append(gifHex.substring(color1offset + newColor1.length() + newColor2.length()));
        return sb.toString();
    }

    private String removeNonHexChars(String noneHexString) {
        char[] chars = noneHexString.toCharArray();
        StringBuilder sb = new StringBuilder();
        char c;
        int cint;
        for (int i = 0; i < chars.length; i++) {
            c = chars[i];
            cint = (int) c;
            if ((cint >= 48 && cint <= 57) || (cint >= 65 && cint <= 70) || (cint >= 97 && cint <= 102)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
