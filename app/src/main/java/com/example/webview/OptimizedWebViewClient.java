package com.example.webview;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OptimizedWebViewClient extends WebViewClient{
	
	public WebViewClient wvc;
	static Map<String,String> url_map;
	
	public OptimizedWebViewClient(WebViewClient wvc) {
		super();
		Log.d("liang/opt", "Constructor2");
		this.wvc = wvc;
		File file=new File("/sdcard/webview/url.txt");
		if(!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				Log.d("liang/file","createfileerr");
				e.printStackTrace();
			}
		}
		url_map = new HashMap<String,String>();
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			url_map = (HashMap<String,String>)in.readObject();
			in.close();
//			BufferedReader br;
//			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//			String line=null;
//			while((line=br.readLine())!=null) {
//				url_map.put(line.split(" ")[0], line);
//			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public WebResourceResponse afterInterceptRequest(WebView view, String url) {
		String mimetype = "";
		String encoding = "";
		Log.d("liang/opt", url);
//		File file=new File("/sdcard/webview/url.txt");
//        if(!file.exists())
//        {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        
//        Map<String,String> url_map = new HashMap<String,String>();
//		try {
//			BufferedReader br;
//			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//			String line=null;
//			while((line=br.readLine())!=null) {
//				url_map.put(line.split(" ")[0], line);
//	        }
//
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
          
        if(url_map.containsKey(url)) {
        	Log.d("liang/found", url);
        	mimetype = url_map.get(url).split(" ")[3];
        	encoding = url_map.get(url).split(" ")[2];

			WebResourceResponse response = null;
			if(mimetype == null)
				return null;
			if(!mimetype.toLowerCase().contains("html")){
				try {
					Log.d("liang/out", url+' '+mimetype+' '+encoding);
					InputStream localCopy = new FileInputStream("/sdcard/webview/res"+url.hashCode());
//					InputStream localCopy = new FileInputStream("/sdcard/"+url.hashCode());

					if(encoding!=null){
						response = new WebResourceResponse(mimetype, encoding, localCopy);
					}
					else{
						response = new WebResourceResponse(mimetype, "UTF-8", localCopy);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return response;
        }
        else {
//			url_map.put(url, url+' '+ url.hashCode()+' '+ encoding+' '+ mimetype);
			final String turl = url;
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						URL murl = new URL(turl);
						URLConnection connection = murl.openConnection();
						String mimetype = connection.getContentType();
						String encoding = connection.getContentEncoding();
						url_map.put(turl, turl+' '+ turl.hashCode()+' '+ encoding+' '+ mimetype);
						InputStream in = connection.getInputStream();
						FileOutputStream fout = new FileOutputStream("/sdcard/webview/res" + turl.hashCode());
//						FileOutputStream fout = new FileOutputStream("/sdcard/" + turl.hashCode());
						int ch = 0;
						while ((ch = in.read()) != -1) {
							fout.write(ch);
						}
						Log.d("liang/file", turl+' '+turl.hashCode()+' '+mimetype+' '+encoding);
					} catch (Exception e) {
						Log.d("liang/threaderr", turl);
						e.printStackTrace();
					}
				}
			};
			new Thread(r).start();
			return null;
		}
//        	try {
//    			URL murl = new URL(url);
//    			URLConnection connection = murl.openConnection();
//    			mimetype = connection.getContentType();
//    			encoding = connection.getContentEncoding();
//    			InputStream in = connection.getInputStream();
//    			FileOutputStream fout = new FileOutputStream("/sdcard/webview/"+url.hashCode());
//    			int ch = 0;
//    			while((ch=in.read()) != -1){
//    	            fout.write(ch);
//    	        }
//				url_map.put(url, url+' '+url.hashCode()+' '+encoding+' '+mimetype);
//    			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true)));
//				bw.append(url+' '+url.hashCode()+' '+encoding+' '+mimetype+"\n");
//    			Log.d("liang/cc",url+' '+url.hashCode()+' '+encoding+' '+mimetype);
//    			bw.flush();
//    			bw.close();
//    			fout.close();
    			
    			/*
    			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    			String urlString = "";
    			String current;
    			while((current = in.readLine()) != null)
    			{
    			    urlString += current;
    			}
    			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("/sdcard/webview/"+url.hashCode()))));
    			bw.write(urlString);
    			bw.flush();
    			bw.close();
    			*/
    			
//            } catch (FileNotFoundException e) {
//    			// TODO Auto-generated catch block
//    			e.printStackTrace();
//    		} catch (IOException e) {
//    			// TODO Auto-generated catch block
//    			e.printStackTrace();
//    		}
//        }
		
//        WebResourceResponse response = null;
//        if(mimetype==null)
//        	return null;
//        if(!mimetype.toLowerCase().contains("html")){
//	    try {
//	    	Log.d("liang/out", url+' '+mimetype+' '+encoding);
//	        InputStream localCopy = new FileInputStream("/sdcard/webview/"+url.hashCode());
//	        if(encoding!=null){
//	        	response = new WebResourceResponse(mimetype, encoding, localCopy);
//	        }
//	        else{
//	        	response = new WebResourceResponse(mimetype, "UTF-8", localCopy);
//	        }
//	        } catch (IOException e) {
//	              e.printStackTrace();
//	    }
//		}
//	    return response;
    }
	
    @Deprecated
    public WebResourceResponse shouldInterceptRequest(WebView view,String url) {
    	Log.d("liang/opt", "shouldInterceptRequest1");
    	WebResourceResponse res = wvc.shouldInterceptRequest(view, url);
    	if(res==null){
    		res = afterInterceptRequest(view, url);
    	}
    	return res;
    }

    /*
    public WebResourceResponse shouldInterceptRequest(WebView view,
            WebResourceRequest request) {
    	Log.d("liang/opt", "shouldInterceptRequest2");
    	return wvc.shouldInterceptRequest(view, request);
    }
    */
	
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Log.d("liang/opt", "override url");
        return wvc.shouldOverrideUrlLoading(view, url);
    }
	
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		wvc.onPageStarted(view, url, favicon);
    }

    public void onPageFinished(WebView view, String url) {
		File file=new File("/sdcard/webview/url.txt");
//		File file=new File("/sdcard/url.txt");
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(url_map);
			out.flush();
			out.close();
//			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
//			bw.append(url + ' ' + url.hashCode() + ' ' + encoding + ' ' + mimetype + "\n");
//			Log.d("liang/cc", url + ' ' + url.hashCode() + ' ' + encoding + ' ' + mimetype);
//			bw.flush();
//			bw.close();
//			fout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		wvc.onPageFinished(view, url);
    }

    public void onLoadResource(WebView view, String url) {
    	wvc.onLoadResource(view, url);
    }

    @Deprecated
    public void onTooManyRedirects(WebView view, Message cancelMsg,
            Message continueMsg) {
        wvc.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    public void onReceivedError(WebView view, int errorCode,
            String description, String failingUrl) {
    	wvc.onReceivedError(view, errorCode, description, failingUrl);
    }

    public void onFormResubmission(WebView view, Message dontResend,
            Message resend) {
        wvc.onFormResubmission(view, dontResend, resend);
    }

    public void doUpdateVisitedHistory(WebView view, String url,
            boolean isReload) {
    	wvc.doUpdateVisitedHistory(view, url, isReload);
    }


    public void onReceivedSslError(WebView view, SslErrorHandler handler,
            SslError error) {
        wvc.onReceivedSslError(view, handler, error);
    }

    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        wvc.onReceivedClientCertRequest(view, request);
    }

    public void onReceivedHttpAuthRequest(WebView view,
            HttpAuthHandler handler, String host, String realm) {
        wvc.onReceivedHttpAuthRequest(view, handler, host, realm);
    }


    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return wvc.shouldOverrideKeyEvent(view, event);
    }


    @Deprecated
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        wvc.onUnhandledKeyEvent(view, event);
    }


    public void onUnhandledInputEvent(WebView view, InputEvent event) {
        wvc.onUnhandledInputEvent(view, event);
    }

    public void onScaleChanged(WebView view, float oldScale, float newScale) {
    	wvc.onScaleChanged(view, oldScale, newScale);
    }
    
    public void onReceivedLoginRequest(WebView view, String realm,
            String account, String args) {
    	wvc.onReceivedLoginRequest(view, realm, account, args);
    }
}
