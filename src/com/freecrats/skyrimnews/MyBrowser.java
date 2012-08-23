package com.freecrats.skyrimnews;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MyBrowser extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Looking here: http://stackoverflow.com/questions/3092291/android-the-progress-bar-in-the-windows-title-does-not-display
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    requestWindowFeature(Window.FEATURE_PROGRESS);
	    setContentView(R.layout.browser);
	    
	    setProgressBarIndeterminateVisibility(true);
	    setProgressBarVisibility(true);

	    String myURL = this.getIntent().getExtras().get("URL").toString();
	    Log.d("MYWEBBROWSER",myURL);
	    WebView myBrowser=(WebView)findViewById(R.id.mybrowser);                

	    myBrowser.getSettings().setJavaScriptEnabled(true);
	    final Activity activity = this;
	    myBrowser.setWebChromeClient(new WebChromeClient() {
	    	   public void onProgressChanged(WebView view, int progress) {
	    		     // Activities and WebViews measure progress with different scales.
	    		     // The progress meter will automatically disappear when we reach 100%
	    		     activity.setProgress(progress * 1000);
	    		   }
	    		 });
	    
	    myBrowser.setWebViewClient(new WebViewClient() {
	    	   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
	    	     Toast.makeText(activity, "Oh no, error! " + description, Toast.LENGTH_SHORT).show();
	    	   }
	    	 });
	    
	    myBrowser.loadUrl(myURL);
	}
	
}
