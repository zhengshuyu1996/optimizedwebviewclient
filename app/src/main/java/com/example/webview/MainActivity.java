package com.example.webview;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
	
	WebView webview;
	ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        webview = (WebView)findViewById(R.id.webview);
        WebViewClient wv = new WebViewClient() {
        	
        	
			// Load opened URL in the application instead of standard browser
			// application
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.d("liang/OverrideUrl", url);
				view.loadUrl(url);
				return true;
			}
			public void onPageFinished(WebView view, String url) {
		    	Log.d("liang/endtime", String.valueOf(System.currentTimeMillis()));
		    }
			/*
			public WebResourceResponse shouldInterceptRequest(WebView view,
		            String url) {
				Log.d("liang/shouldInterceptRequest", url);
				WebResourceResponse response = null;
				
			    if (url.contains("jpg")) {
			          try {
			              InputStream localCopy = getAssets().open("a.jpg");
			              response = new WebResourceResponse("image/jpg", "UTF-8", localCopy);
			          } catch (IOException e) {
			              e.printStackTrace();
			          }        
			    }
			    return response;
		    }
		    */
			
		};
		wv = new OptimizedWebViewClient(wv);
        webview.setWebViewClient(wv);
        /*
        webview.setWebChromeClient(new WebChromeClient() {
			// Set progress bar during loading
			public void onProgressChanged(WebView view, int progress) {
				MainActivity.this.setProgress(progress * 100);
			}
		});
		*/
		WebSettings websettings = webview.getSettings();
		websettings.setJavaScriptEnabled(true);		
		websettings.setBuiltInZoomControls(true);
		websettings.setDomStorageEnabled(false);
		websettings.setDatabaseEnabled(false);
		websettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		
		//String url =this.getIntent().getData().toString();
		//Log.d("liang", String.valueOf(System.currentTimeMillis()));
		String url = "http://sinanews.sina.cn/sinanewsapp/discovery/v600.d.html?deviceId=7fc4682eda52779c&from=6062095012&%E2%80%A6";
		Log.d("liang/starttime", String.valueOf(System.currentTimeMillis()));
		webview.loadUrl(url);

		}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
