package com.project.shop.lemy.xuatnhapkho;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.project.shop.lemy.BuildConfig;
import com.project.shop.lemy.R;

public class EditProductXNKActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private WebView wView;
    String id;

    String val="<!DOCTYPE html>\n" +
            "<!-- saved from url=(0041)http://crm.lemyde.com/admin/order/in/2328 -->\n" +
            "<html lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
            "    \n" +
            "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "<meta name=\"csrf-token\" content=\"ytF3XCnQHSnIgoLH3iQzjidhfhezIQy7hJ8sGEQd\">\n" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "<title></title>\n" +
            "<!-- Tell the browser to be responsive to screen width -->\n" +
            "<meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\n" +
            "<!-- Bootstrap 3.3.7 -->\n" +
            "<link rel=\"stylesheet\" href=\"./2328_files/bootstrap.min.css\">\n" +
            "<!-- Font Awesome -->\n" +
            "<link rel=\"stylesheet\" href=\"./2328_files/font-awesome.min.css\">\n" +
            "<!-- Ionicons -->\n" +
            "<link rel=\"stylesheet\" href=\"./2328_files/ionicons.min.css\">\n" +
            "<!-- Theme style -->\n" +
            "<link rel=\"stylesheet\" href=\"./2328_files/AdminLTE.min.css\">\n" +
            "<!-- AdminLTE Skins. -->\n" +
            "<link rel=\"stylesheet\" href=\"./2328_files/skin-blue.min.css\">\n" +
            "\n" +
            "\n" +
            "\n" +
            "<link rel=\"stylesheet\" href=\"./2328_files/jquery.dataTables.min.css\">\n" +
            "\n" +
            "<link rel=\"stylesheet\" href=\"./2328_files/select2.min.css\">\n" +
            "\n" +
            "\n" +
            "\n" +
            "<link rel=\"stylesheet\" href=\"./2328_files/autocomplete.css\">\n" +
            "\n" +
            "<link rel=\"stylesheet\" href=\"./2328_files/datepicker.css\">\n" +
            "\n" +
            "<style>\n" +
            "    input, th span {\n" +
            "        cursor: pointer;\n" +
            "    }\n" +
            "</style>\n" +
            "<link rel=\"stylesheet\" href=\"./2328_files/custom_admin.css\">\n" +
            "<script src=\"./2328_files/html5shiv.min.js\"></script>\n" +
            "<script src=\"./2328_files/respond.min.js\"></script>    <style>\n" +
            "        .ui-autocomplete > li {\n" +
            "            line-height: 30px;\n" +
            "            padding-left: 11px;\n" +
            "            padding-top: 3px;\n" +
            "            padding-bottom: 3px;\n" +
            "            margin-bottom: 1px;\n" +
            "            background-color: #fbfbfb;\n" +
            "        }\n" +
            "\n" +
            "    }\n" +
            "    * {\n" +
            "        box-sizing: border-box;\n" +
            "        -moz-box-sizing: border-box;\n" +
            "    }\n" +
            "    .page {\n" +
            "        /*width: 100%;*/\n" +
            "        overflow:auto;\n" +
            "        height:auto;\n" +
            "        padding: 0cm 0.1cm 0 0.1cm ;\n" +
            "        margin-left:10em;\n" +
            "        margin-right:10em;\n" +
            "        background: white;\n" +
            "        box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);\n" +
            "        font-size: 15px;\n" +
            "    }\n" +
            "\n" +
            "    @page  {\n" +
            "         size: A6;\n" +
            "         margin: 0;\n" +
            "     }\n" +
            "    .title {\n" +
            "        text-align:center;\n" +
            "        position:relative;\n" +
            "        color:#000000;\n" +
            "        font-size: 15px;\n" +
            "        top:5px;\n" +
            "        font-weight: 700;\n" +
            "        font-family:  Verdana, Arial, Helvetica, sans-serif;\n" +
            "    }\n" +
            "    .tablee{\n" +
            "        font-size: 16px;\n" +
            "        width: 100%;\n" +
            "    }\n" +
            "    .footer-right {\n" +
            "        text-align:center;\n" +
            "        /*text-transform:uppercase;*/\n" +
            "        padding-top:24px;\n" +
            "        position:relative;\n" +
            "\n" +
            "        width:50%;\n" +
            "        color:#000;\n" +
            "        font-size: 15px;\n" +
            "        float:right;\n" +
            "        bottom:1px;\n" +
            "    }\n" +
            "    .TableData {\n" +
            "        background:#ffffff;\n" +
            "        font: 11px;\n" +
            "        width:100%;\n" +
            "        border-collapse:collapse;\n" +
            "        font-family:Verdana, Arial, Helvetica, sans-serif;\n" +
            "        font-size:15px;\n" +
            "        border:thin solid #d3d3d3;\n" +
            "\n" +
            "    }\n" +
            "    .TableData th {\n" +
            "        background: rgba(0,0,255,0.1);\n" +
            "        text-align: center;\n" +
            "        color: #000;\n" +
            "        border: solid 1px #ccc;\n" +
            "        /*height: 24px;*/\n" +
            "    }\n" +
            "    .TableData tr {\n" +
            "        /*height: 24px;*/\n" +
            "        border:thin solid #d3d3d3;\n" +
            "    }\n" +
            "    .TableData tr td {\n" +
            "        padding-right: 2px;\n" +
            "        padding-left: 2px;\n" +
            "        border:thin solid #d3d3d3;\n" +
            "    }\n" +
            "    .TableData tr:hover {\n" +
            "        background: rgba(0,0,0,0.05);\n" +
            "    }\n" +
            "    .TableData .cotSTT {\n" +
            "        text-align:center;\n" +
            "        width: 10%;\n" +
            "    }\n" +
            "    .TableData .cotTenSanPham {\n" +
            "        text-align:left;\n" +
            "        width: 40%;\n" +
            "    }\n" +
            "    .buttonin{\n" +
            "        float: right;margin-top: 2px\n" +
            "    }\n" +
            "\n" +
            "    .TableData .cotGia {\n" +
            "        text-align:right;\n" +
            "        width: 15%;\n" +
            "    }\n" +
            "    .TableData .cotSoLuong {\n" +
            "        text-align: center;\n" +
            "        width: 15%;\n" +
            "    }\n" +
            "    .TableData .cotSo {\n" +
            "        text-align: right;\n" +
            "        width: 15%;\n" +
            "    }\n" +
            "    .TableData .tong {\n" +
            "        text-align: center;\n" +
            "        text-transform:uppercase;\n" +
            "        padding-right: 4px;\n" +
            "    } .TableData .cotSoLuong input {\n" +
            "        text-align: center;\n" +
            "    }\n" +
            "    @media  print {\n" +
            "         @page  {\n" +
            "             margin: 0;\n" +
            "             border: initial;\n" +
            "             border-radius: initial;\n" +
            "             width: initial;\n" +
            "             min-height: initial;\n" +
            "             box-shadow: initial;\n" +
            "             background: initial;\n" +
            "             page-break-after: always;\n" +
            "         }\n" +
            "        .page{\n" +
            "            width: 148mm;\n" +
            "            margin-left: 9px\n" +
            "        }\n" +
            "        .tablee{\n" +
            "            font-size: 12px;margin-left: -5px;\n" +
            "        }\n" +
            "        .footer-right,.TableData , .title {\n" +
            "            font-size: 12px;\n" +
            "        }\n" +
            "        .buttonin{\n" +
            "            margin-top: 0px\n" +
            "        }\n" +
            "        .title {\n" +
            "            margin-top: -10px;padding-top: 1px\n" +
            "        }\n" +
            "\n" +
            "    }\n" +
            "    .main-footer{\n" +
            "        display: none;\n" +
            "    }\n" +
            "</style>\n" +
            "<style type=\"text/css\">/*\n" +
            " * contextMenu.js v 1.4.0\n" +
            " * Author: Sudhanshu Yadav\n" +
            " * s-yadav.github.com\n" +
            " * Copyright (c) 2013 Sudhanshu Yadav.\n" +
            " * Dual licensed under the MIT and GPL licenses\n" +
            "**/\n" +
            "\n" +
            ".iw-contextMenu {\n" +
            "    box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.10);\n" +
            "    border: 1px solid #c8c7cc;\n" +
            "    border-radius: 11px;\n" +
            "    display: none;\n" +
            "    z-index: 1000000132;\n" +
            "    max-width: 300px;\n" +
            "}\n" +
            "\n" +
            ".iw-cm-menu {\n" +
            "    background: #fff;\n" +
            "    color: #000;\n" +
            "    margin: 0px;\n" +
            "    padding: 0px;\n" +
            "}\n" +
            "\n" +
            ".iw-curMenu {\n" +
            "}\n" +
            "\n" +
            ".iw-cm-menu li {\n" +
            "    font-family: -apple-system, BlinkMacSystemFont, \"Helvetica Neue\", Helvetica, Arial, Ubuntu, sans-serif;\n" +
            "    list-style: none;\n" +
            "    padding: 10px;\n" +
            "    padding-right: 20px;\n" +
            "    border-bottom: 1px solid #c8c7cc;\n" +
            "    cursor: pointer;\n" +
            "    position: relative;\n" +
            "    font-size: 14px;\n" +
            "    margin: 0;\n" +
            "    line-height: inherit;\n" +
            "}\n" +
            "\n" +
            ".iw-cm-menu li:first-child {\n" +
            "    border-top-left-radius: 11px;\n" +
            "    border-top-right-radius: 11px;\n" +
            "}\n" +
            "\n" +
            ".iw-cm-menu li:last-child {\n" +
            "    border-bottom-left-radius: 11px;\n" +
            "    border-bottom-right-radius: 11px;\n" +
            "    border-bottom: none;\n" +
            "}\n" +
            "\n" +
            ".iw-mOverlay {\n" +
            "    position: absolute;\n" +
            "    width: 100%;\n" +
            "    height: 100%;\n" +
            "    top: 0px;\n" +
            "    left: 0px;\n" +
            "    background: #FFF;\n" +
            "    opacity: .5;\n" +
            "}\n" +
            "\n" +
            ".iw-contextMenu li.iw-mDisable {\n" +
            "    opacity: 0.3;\n" +
            "    cursor: default;\n" +
            "}\n" +
            "\n" +
            ".iw-mSelected {\n" +
            "    background-color: #F6F6F6;\n" +
            "}\n" +
            "\n" +
            ".iw-cm-arrow-right {\n" +
            "    width: 0;\n" +
            "    height: 0;\n" +
            "    border-top: 5px solid transparent;\n" +
            "    border-bottom: 5px solid transparent;\n" +
            "    border-left: 5px solid #000;\n" +
            "    position: absolute;\n" +
            "    right: 5px;\n" +
            "    top: 50%;\n" +
            "    margin-top: -5px;\n" +
            "}\n" +
            "\n" +
            ".iw-mSelected > .iw-cm-arrow-right {\n" +
            "}\n" +
            "\n" +
            "/*context menu css end */</style><style type=\"text/css\">@-webkit-keyframes load4 {\n" +
            "    0%,\n" +
            "    100% {\n" +
            "        box-shadow: 0 -3em 0 0.2em, 2em -2em 0 0em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 0;\n" +
            "    }\n" +
            "    12.5% {\n" +
            "        box-shadow: 0 -3em 0 0, 2em -2em 0 0.2em, 3em 0 0 0, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 -1em;\n" +
            "    }\n" +
            "    25% {\n" +
            "        box-shadow: 0 -3em 0 -0.5em, 2em -2em 0 0, 3em 0 0 0.2em, 2em 2em 0 0, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 -1em;\n" +
            "    }\n" +
            "    37.5% {\n" +
            "        box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0em 0 0, 2em 2em 0 0.2em, 0 3em 0 0em, -2em 2em 0 -1em, -3em 0em 0 -1em, -2em -2em 0 -1em;\n" +
            "    }\n" +
            "    50% {\n" +
            "        box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 0em, 0 3em 0 0.2em, -2em 2em 0 0, -3em 0em 0 -1em, -2em -2em 0 -1em;\n" +
            "    }\n" +
            "    62.5% {\n" +
            "        box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 0, -2em 2em 0 0.2em, -3em 0 0 0, -2em -2em 0 -1em;\n" +
            "    }\n" +
            "    75% {\n" +
            "        box-shadow: 0em -3em 0 -1em, 2em -2em 0 -1em, 3em 0em 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 0, -3em 0em 0 0.2em, -2em -2em 0 0;\n" +
            "    }\n" +
            "    87.5% {\n" +
            "        box-shadow: 0em -3em 0 0, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 0, -3em 0em 0 0, -2em -2em 0 0.2em;\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "@keyframes load4 {\n" +
            "    0%,\n" +
            "    100% {\n" +
            "        box-shadow: 0 -3em 0 0.2em, 2em -2em 0 0em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 0;\n" +
            "    }\n" +
            "    12.5% {\n" +
            "        box-shadow: 0 -3em 0 0, 2em -2em 0 0.2em, 3em 0 0 0, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 -1em;\n" +
            "    }\n" +
            "    25% {\n" +
            "        box-shadow: 0 -3em 0 -0.5em, 2em -2em 0 0, 3em 0 0 0.2em, 2em 2em 0 0, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 -1em;\n" +
            "    }\n" +
            "    37.5% {\n" +
            "        box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0em 0 0, 2em 2em 0 0.2em, 0 3em 0 0em, -2em 2em 0 -1em, -3em 0em 0 -1em, -2em -2em 0 -1em;\n" +
            "    }\n" +
            "    50% {\n" +
            "        box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 0em, 0 3em 0 0.2em, -2em 2em 0 0, -3em 0em 0 -1em, -2em -2em 0 -1em;\n" +
            "    }\n" +
            "    62.5% {\n" +
            "        box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 0, -2em 2em 0 0.2em, -3em 0 0 0, -2em -2em 0 -1em;\n" +
            "    }\n" +
            "    75% {\n" +
            "        box-shadow: 0em -3em 0 -1em, 2em -2em 0 -1em, 3em 0em 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 0, -3em 0em 0 0.2em, -2em -2em 0 0;\n" +
            "    }\n" +
            "    87.5% {\n" +
            "        box-shadow: 0em -3em 0 0, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 0, -3em 0em 0 0, -2em -2em 0 0.2em;\n" +
            "    }\n" +
            "}</style><style type=\"text/css\">/* This is not a zero-length file! */</style></head>\n" +
            "<body class=\"skin-blue sidebar-mini\">\n" +
            "    <div class=\"loading\">\n" +
            "        <img src=\"./2328_files/loading.gif\">\n" +
            "\n" +
            "        <p>Loadding...</p>\n" +
            "    </div>\n" +
            "\n" +
            "    <div id=\"page\" class=\"page\" style=\"float: center;font-size: 12px;padding: 10px 15px 15px 11px\">\n" +
            "        <div class=\"title\">\n" +
            "            Shop Nhung Nguyễn\n" +
            "            <br>\n" +
            "        </div>\n" +
            "\n" +
            "        <br>\n" +
            "        <div class=\"col-lg-4 col-md-4 col-sm-4 col-xs-4\" style=\"padding-left: 10px\">\n" +
            "\n" +
            "            <table class=\"tablee\">\n" +
            "                <tbody><tr>\n" +
            "                    <td> KH: Chị Dung   ( Hoạn Thư )  </td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td>  0913398968 </td>\n" +
            "                </tr>\n" +
            "\n" +
            "                <tr> \n" +
            "                    <td>  11 ngũ xã badinh hn ( ngã tư trúc bạch và ngũ xã ) ,  Quận Ba Đình , Hà Nội  </td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td>   Thu ship  </td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td>  Ghi chú:  </td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td>  PTTT:    Trả tiền mặt  </td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td> \n" +
            "                        <span style=\"font-size: 20px;word-break: break-all\"> DHM2328\n" +
            "                             \n" +
            "                        </span>\n" +
            "                    </td>\n" +
            "\n" +
            "                </tr></tbody></table>\n" +
            "            </div>\n" +
            "            <div class=\"col-lg-8 col-md-8 col-sm-8 col-xs-8\" style=\"padding-right: 40px;padding-left: 0px;\">\n" +
            "                <table class=\"TableData\">\n" +
            "                    <tbody><tr>\n" +
            "                      <th>STT</th>\n" +
            "                      <th>Tên</th>\n" +
            "                      <th>SL</th>\n" +
            "                      <th>Giá</th>\n" +
            "                      <th>Tổng</th>\n" +
            "                  </tr>\n" +
            "                  <tr><td class=\"cotSTT\">1</td><td class=\"cotTenSanPham\">Máy lau kính tự động Silvercrest</td><td class=\"cotSoLuong\" align=\"center\">1</td><td class=\"cotGia\">1,250,000</td><td class=\"cotSo\">1,250,000</td></tr>                <tr>\n" +
            "                    <td></td>\n" +
            "                    <td colspan=\"1\" class=\"tong\" style=\"text-align: left;text-transform: none;\">Thu thêm</td>\n" +
            "                    <td></td>\n" +
            "                    <td></td>\n" +
            "                    <td colspan=\"4\" class=\"cotSo\">0</td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td colspan=\"2\" class=\"tong\">Tổng cộng</td>\n" +
            "                    <td style=\"text-align: center;\">1</td>\n" +
            "                    <td></td>\n" +
            "                    <td class=\"cotSo\">1,250,000</td>\n" +
            "                </tr>\n" +
            "            </tbody></table>\n" +
            "        </div>\n" +
            "\n" +
            "    <br>\n" +
            "    <div class=\"footer-right\"> \n" +
            "        Ngày ...... tháng ...... năm 20......    <button style=\"margin-left: 15px\" id=\"btnprint\" title=\"Print\" onclick=\"print_page()\"><span class=\"fa fa-print\"></span></button><br>\n" +
            "        người bán :nhungnguyen\n" +
            "    </div>\n" +
            "    \n" +
            "</div>\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "</body></html>";

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
