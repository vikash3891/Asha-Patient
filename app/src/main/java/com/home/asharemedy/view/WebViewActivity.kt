package com.home.asharemedy.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.webkit.*
import com.home.asharemedy.AshaRemedyApp
import com.home.asharemedy.R
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.Utils

class WebViewActivity : BaseActivity() {

    internal var contentUrl = ""
    internal var contentTitle = ""

    //    @BindView(R.id.chat_button)
    lateinit var chat_button: FloatingActionButton
    //    @BindView(R.id.webView)
    lateinit var contentWebView: WebView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        contentTitle = intent.getStringExtra("contentTitle")

        contentWebView = findViewById(R.id.webView)
        contentUrl = intent.getStringExtra("contentUrl")
        contentWebView.settings.javaScriptEnabled = true
        contentWebView.addJavascriptInterface(WebAppInterface(this), "Android")
        contentWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (contentWebView != null) {
                    contentWebView.visibility = View.GONE
                    if (newProgress == 100) {
                        contentWebView.visibility = View.VISIBLE
                    }
                }
            }
        }

        contentWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (contentWebView != null) {
                    contentWebView.visibility = View.VISIBLE
                    view.clearCache(true)
                }
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {

            }

        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (contentWebView.canGoBack()) {
                        contentWebView.goBack()
                    } else {
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        if (Utils.isConnected(this)) {
            contentWebView.loadUrl(contentUrl)
        } else run {

            /*val retryDialog = RetryDialog(this, getString(R.string.no_internet_connection), getString(R.string.no_internet_connection_message))
            retryDialog.setOnRetryButtonClickListener {
                retryDialog.dismiss()
                contentWebView.loadUrl(AppConstants.WEB_PAGE_URL + contentUrl)
            }
            retryDialog.show()*/
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    internal inner class WebAppInterface
    /**
     * Instantiate the interface and set the context
     */
        (var mContext: Context) {


        @JavascriptInterface
        fun gotoDaftarLink() {
            val intent = Intent(this@WebViewActivity, AshaRemedyApp::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

}
