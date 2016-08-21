package ru.solandme.remindmeabout;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class SplashScreen extends AppCompatActivity {

    private String[] assetFiles;
    private String[] appImagesFiles;

    private ProgressBar progressBar;

    private static final String TAG = "SplashScreen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.copyAssetsProgressBar);

        new MyTask().execute();
    }

    private void startMainActivity() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private class MyTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            assetFiles = getListAssets("images");
            File f = new File(getApplicationContext().getFilesDir().getPath() + "/images/");
            appImagesFiles = f.list();
            progressBar.setMax(assetFiles.length);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.incrementProgressBy(values[0]);
        }

        @Override
        protected Void doInBackground(Void... params) {

            if ((appImagesFiles == null) || (!Arrays.asList(appImagesFiles).containsAll(Arrays.asList(assetFiles)))) {
                copyAssetFolder(getAssets(), "images", getApplicationContext().getFilesDir().getPath() + "/images");
            } else {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startMainActivity();
        }

        private String[] getListAssets(String folder) {
            try {
                return getAssets().list(folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private boolean copyAssetFolder(AssetManager assetManager,
                                        String fromAssetPath, String toPath) {
            try {
                assetFiles = assetManager.list(fromAssetPath);
                new File(toPath).mkdirs();
                boolean res = true;
                for (String file : assetFiles) {
                    if (file.contains("."))
                        res &= copyAsset(assetManager,
                                fromAssetPath + "/" + file,
                                toPath + "/" + file);
                    else
                        res &= copyAssetFolder(assetManager,
                                fromAssetPath + "/" + file,
                                toPath + "/" + file);
                }
                return res;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private boolean copyAsset(AssetManager assetManager,
                                  String fromAssetPath, String toPath) {
            InputStream in;
            OutputStream out;
            try {
                in = assetManager.open(fromAssetPath);
                new File(toPath).createNewFile();
                out = new FileOutputStream(toPath);
                copyFile(in, out);
                in.close();
                out.flush();
                out.close();
                Log.d(TAG, "copyAsset: " + toPath);
                publishProgress(1);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private void copyFile(InputStream in, OutputStream out) throws IOException {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }
    }
}

