package com.project.shop.lemy.donhang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.project.shop.lemy.R;


public class PrintActivity extends AppCompatActivity {
    private WebView webviewPrint;
    private View close;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_print);
        webviewPrint = findViewById(R.id.webviewPrint);
        close = findViewById(R.id.close);

        url =getIntent().getStringExtra("url");

        webviewPrint.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("inDonHang", "url: " + url);
                if (url.contains("login")) {
                    webviewPrint.setVisibility(View.VISIBLE);
                    return;
                }


                PrintManager printManager = (PrintManager) getBaseContext()
                        .getSystemService(Context.PRINT_SERVICE);

                // Get a print adapter instance
                PrintDocumentAdapter printAdapter = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                    printAdapter = webviewPrint.createPrintDocumentAdapter("printcrm");
                else printAdapter = webviewPrint.createPrintDocumentAdapter();

                // Create a print job with name and adapter instance
                String jobName = getString(R.string.app_name) + " Document";
                PrintJob printJob = printManager.print(jobName, printAdapter,
                        new PrintAttributes.Builder().build());

            }
        });
        webviewPrint.loadUrl(url);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static void inDonHang(Context context, String url) {

        Intent intent = new Intent(context, PrintActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


}