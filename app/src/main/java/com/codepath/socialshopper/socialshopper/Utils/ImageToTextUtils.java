package com.codepath.socialshopper.socialshopper.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by gumapathi on 11/12/17.
 */

public class ImageToTextUtils {
    private TextRecognizer detector;
    public List<String> convert(Context context, android.net.Uri imageUri) {
        List<String> returnWord = new ArrayList<String>();
        try {
            Bitmap bitmap = decodeBitmapUri(context, imageUri);
            if (detector.isOperational() && bitmap != null) {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> textBlocks = detector.detect(frame);
                String blocks = "";
                String lines = "";
                String words = "";
                for (int index = 0; index < textBlocks.size(); index++) {
                    //extract scanned text blocks here
                    TextBlock tBlock = textBlocks.valueAt(index);
                    blocks = blocks + tBlock.getValue() + "\n" + "\n";
                    for (Text line : tBlock.getComponents()) {
                        //extract scanned text lines here
                        lines = lines + line.getValue() + "\n";
                        for (Text element : line.getComponents()) {
                            //extract scanned text words here
                            words = words + element.getValue() + ", ";
                            returnWord.add(element.getValue());
                        }
                    }
                }
                if (textBlocks.size() == 0) {
                    returnWord.add("Scan Failed: Found nothing to scan");
                } else {
//                    scanResults.setText(scanResults.getText() + "Blocks: " + "\n");
//                    scanResults.setText(scanResults.getText() + blocks + "\n");
//                    scanResults.setText(scanResults.getText() + "---------" + "\n");
//                    scanResults.setText(scanResults.getText() + "Lines: " + "\n");
//                    scanResults.setText(scanResults.getText() + lines + "\n");
//                    scanResults.setText(scanResults.getText() + "---------" + "\n");
//                    scanResults.setText(scanResults.getText() + "Words: " + "\n");
//                    scanResults.setText(scanResults.getText() + words + "\n");
//                    scanResults.setText(scanResults.getText() + "---------" + "\n");
                }
            } else {
                returnWord.add("Could not set up the detector!");
            }
        } catch (Exception e) {
            Toast.makeText(context, "Failed to load Image", Toast.LENGTH_SHORT).show();
            Log.e("SocShpImgConv", e.toString());
        }
        return returnWord;
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }
}
