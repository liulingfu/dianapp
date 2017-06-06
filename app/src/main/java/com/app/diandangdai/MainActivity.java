package com.app.diandangdai;






import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	WebView webView;
	ProgressBar pro;
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		webView = (WebView)findViewById(R.id.webView);
		pro = (ProgressBar)findViewById(R.id.progressBar);

		//设置支持js
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setAppCacheMaxSize(1024*1024 * 8);
		String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
		webView.getSettings().setAppCachePath(appCachePath);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAppCacheEnabled(true);
		// 设置允许JS弹窗
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				//网页加载开始时调用，显示姐啊在提示旋转进度条
				super.onPageStarted(view, url, favicon);
				pro.setVisibility(android.view.View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				//网页加载完成时调用，显示姐啊在提示旋转进度条
				super.onPageFinished(view, url);
				pro.setVisibility(android.view.View.GONE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				//网页加载失败时调用，显示姐啊在提示旋转进度条
				super.onReceivedError(view, errorCode, description, failingUrl);
				pro.setVisibility(android.view.View.GONE);
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {
			// The undocumented magic method override
			// Eclipse will swear at you if you try to put @Override here
			// For Android 3.0+
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {

				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				MainActivity.this.startActivityForResult(
						Intent.createChooser(i, "File Chooser"),
						FILECHOOSER_RESULTCODE);

			}

			// For Android 3.0+
			public void openFileChooser(ValueCallback uploadMsg,
										String acceptType) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				MainActivity.this.startActivityForResult(
						Intent.createChooser(i, "File Browser"),
						FILECHOOSER_RESULTCODE);
			}

			// For Android 4.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
										String acceptType, String capture) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				MainActivity.this.startActivityForResult(
						Intent.createChooser(i, "File Chooser"),
						MainActivity.FILECHOOSER_RESULTCODE);

			}

		});
		webView.loadUrl("http://www.diandangdai.com/wechat/home/home");
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case SCANNIN_GREQUEST_CODE:
				if(resultCode == RESULT_OK){
					Bundle bundle = data.getExtras();
					//显示扫描到的内容
					//mTextView.setText(bundle.getString("result"));
					webView.loadUrl(bundle.getString("result"));
					//显示
					//mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
				}
				break;
		}
	}

	// flipscreen not loading again
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	// To handle "Back" key press event for WebView to go back to previous
	// screen.
    /*
     * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
     * ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) { web.goBack();
     * return true; } return super.onKeyDown(keyCode, event); }
     */
}

