package com.example.assignmenttest3.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.assignmenttest3.R
import com.example.assignmenttest3.databinding.WebViewLayoutBinding


//WebViewActivity - web view screen for each recipes website
class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var url:String

    private lateinit var binding: WebViewLayoutBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WebViewLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //we get the recipe url from the intent sent to this activity and use a webViewClient to load the url in our web view

        url = intent.getStringExtra("EXTRA_URL").toString()


        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)


        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
    }





    //function is called when we call the back arrow on a device

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}