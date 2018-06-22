package com.epsilon.FunwithStatus.utills;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Helper {

    public static void hideSoftKeyboard(Context context) {
        if(((Activity)context).getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), 0);
        }
    }
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {

            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String getExtensionFromFilePath(String fullPath) {
        String filenameArray[] = fullPath.split("\\.");
        return filenameArray[filenameArray.length - 1];
    }

    public static String getFileName(String fullPath) {
        return fullPath.substring(fullPath.lastIndexOf(File.separator) + 1);
    }

    public static String getFileParent(String fullPath) {
        return fullPath.substring(0, fullPath.lastIndexOf(File.separator));
    }

    public static String getMimeTypeFromFilePath(String filePath) {
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                getExtensionFromFilePath(filePath));
        return (mimeType == null) ? "*/*" : mimeType;
    }

    public static boolean isImage(String fullPath) {

        File imageFile = new File(fullPath);
        if (imageFile == null || !imageFile.exists()) {
            return false;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getPath(), options);
        return options.outWidth != -1 && options.outHeight != -1;
    }
}
