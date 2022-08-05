package com.project.shop.lemy.nhapdon;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lemy.library.UpdateChecker;
import com.project.shop.lemy.LoginActivity;
import com.project.shop.lemy.R;
import com.project.shop.lemy.chucnangkhac.SuaCodGhtkActivity;
import com.project.shop.lemy.common.SprSupport;
import com.project.shop.lemy.helper.PermissionSupport;
import com.telpoo.frame.utils.SPRSupport;
import com.telpoo.frame.widget.TextWatcherAdapter;

import java.io.IOException;
import java.lang.reflect.Method;


public class NhapDonHome extends AppCompatActivity {
    EditText codeNv,edTienHang,maDh;
    TextView tvInfo;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 20842;
    private Context context= NhapDonHome.this;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_don_home);



        tvInfo = findViewById(R.id.tvInfo);
        edTienHang= findViewById(R.id.edTienHang);
        maDh= findViewById(R.id.maDh);
        try {
            int version = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            tvInfo.setText("Phiên bản " + version);
            SPRSupport.save("version_code",""+version,getBaseContext());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            //initializeView();
        }

        findViewById(R.id.notify_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (codeNv.getText().toString().isEmpty()){
                    Toast.makeText(NhapDonHome.this,"Bạn chưa nhập mã",Toast.LENGTH_LONG).show();
                    return;
                }
                initializeView();

            }
        });

        codeNv= findViewById(R.id.codeNv);

        String code= SPRSupport.getString("codenv",this,"");
        codeNv.setText(code);

        codeNv.addTextChangedListener(new TextWatcherAdapter(codeNv,(view, text) -> {

            SPRSupport.save("codenv",text.trim(),this);
        }));

    }

    private void initializeView() {
        stopService(new Intent(getApplicationContext(), FloatingViewService.class));

        codeNv.postDelayed(() -> {
            startService(new Intent(getApplicationContext(), FloatingViewService.class));
            finish();
        },300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {

                //initializeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
         @Override
    protected void onResume() {
        super.onResume();
        String pass=""+ SprSupport.getPass(getBaseContext());
        if (!pass.contains("1080")) startActivity(new Intent(this, LoginActivity.class));

    }

    public void nhapTien(View view) {

    }

    public void clickCheckUpdate(View view) {
        if (PermissionSupport.getInstall(NhapDonHome.this).requestPermissionStore()){
            UpdateChecker.checkForDialog(NhapDonHome.this);

        }
    }

    public void clickUpdateGhtkCod(View view) {
            startActivity(new Intent(this, SuaCodGhtkActivity.class));
    }

    public void clickXiaomi(View view) {
        findViewById(R.id.tmp5).setVisibility(View.VISIBLE);
    }

    public static Intent toFloatWindowPermission(Context context, String packageName,String miuiversion) {

            Uri packageUri = Uri.parse("package:" + packageName);
            Intent detailsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri);
            detailsIntent.addCategory(Intent.CATEGORY_DEFAULT);
            if (MIUI_V5.equals(miuiversion)) {
                return detailsIntent;
            } else {
                Intent permIntent = toPermissionManager(context, packageName,miuiversion);
                return permIntent == null ? detailsIntent : permIntent;
            }


    }

    public static boolean isMIUIV5() {
        return getVersionName().equals(MIUI_V5);
    }

    public static boolean isMIUIV6() {
        return getVersionName().equals(MIUI_V6);
    }

    public static String getVersionName() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_NAME);
        } catch (IOException e) {
            return "";
        }
    }

    public static boolean isFloatWindowOptionAllowed(Context context) {
        AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        Class localClass = manager.getClass();
        Class[] arrayOfClass = new Class[3];
        arrayOfClass[0] = Integer.TYPE;
        arrayOfClass[1] = Integer.TYPE;
        arrayOfClass[2] = String.class;
        try {
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObjects = new Object[3];
            arrayOfObjects[0] = Integer.valueOf(24);
            arrayOfObjects[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObjects[2] = context.getPackageName();
            int m = ((Integer) method.invoke((Object) manager, arrayOfObjects)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            return false;
        }
    }


    private static final String MIUI_V5 = "V5";
    private static final String MIUI_V6 = "V6";
    private static final String MIUI_V8 = "V8";

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (IOException e) {
            return false;
        }
    }
    public static Intent toPermissionManager(Context context, String packageName,String miuiversion) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        String version = getVersionName();
        if (MIUI_V5.equals(miuiversion)) {
            PackageInfo pInfo;
            try {
                pInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException ignored) {
                return null;
            }
            intent.setClassName("com.android.settings", "com.miui.securitycenter.permission.AppPermissionsEditor");
            intent.putExtra("extra_package_uid", pInfo.applicationInfo.uid);
        } else { // MIUI_V6 and above
            final String PKG_SECURITY_CENTER = "com.miui.securitycenter";
            try {
                context.getPackageManager().getPackageInfo(PKG_SECURITY_CENTER, PackageManager.GET_ACTIVITIES);
            } catch (PackageManager.NameNotFoundException ignored) {
                return null;
            }
            String ssv9="com.miui.permcenter.permissions.PermissionsEditorActivity";
            if (MIUI_V6.equals(miuiversion))ssv9="com.miui.permcenter.permissions.AppPermissionsEditorActivity";
            intent.setClassName(PKG_SECURITY_CENTER, ssv9);
            intent.putExtra("extra_pkgname", packageName);
        }
        return intent;
    }


    public void clickmiui5(View view) {
        Intent it = toFloatWindowPermission(context, getPackageName(), MIUI_V5);
        if (it==null) return;
        try {
            startActivity(it);
        }
        catch (Exception e){
            Toast.makeText(context,"Lỗi "+e.getMessage(),Toast.LENGTH_LONG).show();

        }

    }

    public void clickmiui76(View view) {
        Intent it = toFloatWindowPermission(context, getPackageName(), MIUI_V6);
        try {
            startActivity(it);
        }
        catch (Exception e){
            Toast.makeText(context,"Lỗi "+e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }

    public void clickmiui8(View view) {
        Intent it = toFloatWindowPermission(context, getPackageName(), MIUI_V8);
        try {
            startActivity(it);
        }
        catch (Exception e){
            Toast.makeText(context,"Lỗi "+e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }

    public void clickAhoa(View view) {
        SPRSupport.save("codenv2","lehoa",this);
        startActivity(new Intent(this,NhapDonActivity.class));
    }
    public void clickMai(View view) {
        SPRSupport.save("codenv2","lemai",this);
        startActivity(new Intent(this,NhapDonActivity.class));
    }
}
