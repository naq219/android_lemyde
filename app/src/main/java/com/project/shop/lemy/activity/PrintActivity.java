package com.project.shop.lemy.activity;

import android.content.Context;
import android.content.Intent;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.project.shop.lemy.BuildConfig;
import com.project.shop.lemy.R;

public class PrintActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private WebView wView;
    String id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        initView(getBaseContext());
    }

    private void initView(Context context) {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        toolbar = findViewById(R.id.toolbar);
        wView = findViewById(R.id.wView);

        wView.getSettings().setAllowContentAccess(true);
        wView.getSettings().setAllowFileAccess(true);
        wView.getSettings().setJavaScriptEnabled(true);
        wView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wView.getSettings().setLoadWithOverviewMode(true);
        wView.getSettings().setAppCacheEnabled(true);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("In đơn hàng");

        wView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                Toast.makeText(context,"loadxong",Toast.LENGTH_LONG).show();
                //createWebPrintJob(wView,getBaseContext());
            }
        });
        wView.loadUrl(BuildConfig.url_vc + "/" + id);

       // wView.loadDataWithBaseURL(null, val, "text/HTML", "UTF-8", null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void createWebPrintJob(WebView webView,Context context) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) context
                .getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            printAdapter = webView.createPrintDocumentAdapter("printcrm");
        }
        else  printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());


    }

    public void clickPrint(View view) {
        createWebPrintJob(wView,getBaseContext());
    }
}
