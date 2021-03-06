
package com.ll.filebrowser.util;

import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileFinder extends AsyncTask<File, Void, List<File>> {

    private static final String TAG = "FileFinder";

    public static final int LOAD_FAILED_NO_SURCH_DIR = -1;

    public interface IFileFinderCallback {
        void onLoadSuccess(List<File> files);

        void onLoadFailed(int resonCode);
    }

    private IFileFinderCallback mFileFinderCallback;

    public FileFinder(IFileFinderCallback fileFinderCallback) {
        super();
        mFileFinderCallback = fileFinderCallback;
    }

    @Override
    protected List<File> doInBackground(File... params) {
        if (params == null || params.length == 0 || params[0] == null ) {
            return null;
        }
        // TODO show dialog
        File dir = params[0];
        File[] subFiles = dir.listFiles();
        if(dir != null && dir.exists() && subFiles != null){
            synchronized (dir) {
                List<File> result = new ArrayList<File>();
                for (int i = 0; i < subFiles.length; i++) {
                    File subFile = subFiles[i];
                    if (subFile.exists()
                            && (subFile.isDirectory() || subFile.isFile() || subFile.isHidden()))
                        result.add(subFile);
                }
                return result;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<File> result) {
        Logger.d(TAG, "onPostExecute()[files:" + result + "]");
        if (mFileFinderCallback != null && result != null) {
            mFileFinderCallback.onLoadSuccess(result);
        } else if (mFileFinderCallback != null && result == null) {
            mFileFinderCallback.onLoadFailed(LOAD_FAILED_NO_SURCH_DIR);
        }

    }
}
