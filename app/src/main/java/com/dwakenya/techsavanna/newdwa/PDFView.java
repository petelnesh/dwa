package com.dwakenya.techsavanna.newdwa;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import pub.devrel.easypermissions.EasyPermissions;

public class PDFView extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    WebView PDFWebView;
    FabSpeedDial fabSpeedDial;
    private static final int WRITE_REQUEST_CODE = 300;
    private static final String TAG = PDFView.class.getSimpleName();
    String ImagePath;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
   
            final ProgressBar ProgressPdf = findViewById(R.id.ProgressPdf);
    
            PDFWebView = findViewById(R.id.PDFWebView);
            fabSpeedDial = findViewById(R.id.PDFDial);
            PDFWebView.getSettings().setJavaScriptEnabled(true);
            PDFWebView.getSettings().setSupportZoom(true);
            Intent intent = getIntent();
            ImagePath = intent.getStringExtra("PDFUrl");
    
            PDFWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + ImagePath);
            PDFWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    ProgressPdf.setVisibility(View.VISIBLE);
                    view.loadUrl(url);
                    return true;
                }
        
                @Override
                public void onPageFinished(WebView view, final String url) {
                    ProgressPdf.setVisibility(View.GONE);
                }
    
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                }
            });
            PDFWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress < 100) {
                        ProgressPdf.setVisibility(View.VISIBLE);
                
                    } else if (newProgress == 100) {
                        ProgressPdf.setVisibility(View.GONE);
                        fabSpeedDial.setVisibility(View.VISIBLE);
                
                    }
                }
        
            });
    
    
            fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
                @Override
                public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                    return true;
                }
        
                @Override
                public boolean onMenuItemSelected(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.action_download:
                            if (CheckSD.isSDCardPresent()) {
                        
                                //check if app has permission to write to the external storage.
                                if (EasyPermissions.hasPermissions(PDFView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    //Get the URL entered
                                    new DownloadFile(PDFView.this).execute(ImagePath);
                            
                                } else {
                                    //If permission is not present request for the same.
                                    EasyPermissions.requestPermissions(PDFView.this, getString(R.string.write_file), WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                                }
                        
                        
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "SD Card not found", Toast.LENGTH_LONG).show();
                        
                            }
                    
                    
                            break;
                        case R.id.action_share:
                            Intent i2 = new Intent(PDFView.this, FeedBackActivity.class);
                            startActivity(i2);
                            break;
                    }
                    return true;
                }
            });
        
    }
    
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, PDFView.this);
        }
    
      
        public void onPermissionsGranted(int requestCode, List<String> perms) {
            //Download the file once permission is granted
            new DownloadFile(PDFView.this).execute(ImagePath);
        }
    
        @Override
        public void onPermissionsDenied(int requestCode, List<String> perms) {
            Log.d(TAG, "Permission has been denied");
        }
        
    
        
    
    
}
