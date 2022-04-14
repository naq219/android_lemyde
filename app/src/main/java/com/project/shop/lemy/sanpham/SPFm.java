package com.project.shop.lemy.sanpham;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.model.Model;
import com.lemy.telpoo2lib.model.Task;
import com.lemy.telpoo2lib.model.TaskParams;
import com.lemy.telpoo2lib.net.Dataget;
import com.project.shop.lemy.MainActivity;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskGeneral;
import com.project.shop.lemy.Task.TaskProductv2;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.common.Keyboard;
import com.project.shop.lemy.helper.FileUtilsV2;
import com.project.shop.lemy.helper.InputStreamRequestBody;
import com.project.shop.lemy.helper.StringHelper;
import com.telpoo.frame.utils.DialogSupport;
import com.telpoo.frame.utils.Mlog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SPFm extends SPFmView implements View.OnClickListener {



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        loadInfo();
        tvAddSp.setOnClickListener(view1 -> {
            long last= (long) tvAddSp.getTag();
            long time= Calendar.getInstance().getTimeInMillis()-last;
            tvAddSp.setTag(Calendar.getInstance().getTimeInMillis());
            if (time>500) return;
            addProduct();
        });

        tvAddSp.postDelayed(() -> {
            Keyboard.hideKeyboard(getActivity());
        },50);

        tvUpdateTimeDangBai.setOnClickListener(view1 -> {
            long last= (long) tvUpdateTimeDangBai.getTag();
            long time= Calendar.getInstance().getTimeInMillis()-last;
            tvUpdateTimeDangBai.setTag(Calendar.getInstance().getTimeInMillis());
            if (time>500) return;
            updateTimeDangBai();


        });


        uploadImage();

    }

    private void updateTimeDangBai() {
        if (productId==null){
            showToast("Chưa chọn Sản phẩm ");
            return;
        }
        String sql ="UPDATE ;::;.products SET post_at = UNIX_TIMESTAMP(NOW()) WHERE id="+productId;
        TaskGeneral.exeTaskInsertApi(getContext(),sql,1,new Model(){
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                showToast("Cập nhật thời gian đăng bài thành công");
            }

            @Override
            public void onFail(int taskType, String msg, Integer queue) {
                showToast("Lỗi cập nhật time đăng bài "+msg);

            }
        });
    }



    private void uploadImage() {
        btnUpload.setOnClickListener(view -> {
            MainActivity.currentFragmentRequestImage= this;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getActivity().startActivityForResult(Intent.createChooser(intent, "Chọn Ảnh"), 123);
        });
    }

    private void addProduct() {
        if (!isValidate()) return;

        BObject object = new BObject();
        String tenSp, giaNhap, giaBan, giaBuon, giaBuonSi, linkAnh, gioiThieu, pageLink, note;
        tenSp = edtTensp.getText().toString();
        giaNhap = edGiaNhap.getText().toString();
        giaBan = edGiaBan.getText().toString();
        giaBuon = edGiaBuon.getText().toString();
        giaBuonSi = edGiaBuonSi.getText().toString();

        giaNhap = StringHelper.formatStr(giaNhap);
        giaBuon = StringHelper.formatStr(giaBuon);
        giaBan = StringHelper.formatStr(giaBan);
        giaBuonSi = StringHelper.formatStr(giaBuonSi);

        object.set(ProductObj.name, tenSp);
        object.set(ProductObj.cost_price, giaNhap);
        object.set(ProductObj.wholesale_price, giaBuon);
        object.set(ProductObj.retail_price, giaBan);
        object.set(ProductObj.gia_buonsi, giaBuonSi);
        object.set(ProductObj.introduction, edtGioithieu.getText());
        object.set(ProductObj.ean, edEan.getText());
        int limit=0;
        if (cbChanBan.isChecked())limit=-1;
        object.set(ProductObj.limit0, limit);



        TaskProductv2 taskProductv2 = new TaskProductv2(new Model(){
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                showToast("ok"+data);
                closeProgressDialog();
                DialogSupport.simpleYesNo(getActivity(),"Vào xem SP","Đóng lại","Thêm SP thành công: (SP"+data+")",null,(value, where) -> {
                    if ((int)value==1){
                        DetailProductActivity.startActivity(getActivity(),String.valueOf(data));
                    }
                });


            }

            @Override
            public void onFail(int taskType, String msg, Integer queue) {
                closeProgressDialog();
                Mlog.D(msg);

               DialogSupport.Message(getActivity(),"lỗi rồi "+msg,null);

            }
        },TaskProductv2.CREATE,getContext());
        TaskParams param=new TaskParams();
        param.put("oj",object);

        if (productId!=null){
            taskProductv2.setTaskType(TaskProductv2.UPDATE);
            param.put("id",productId);
        }

        taskProductv2.execute(param);

        showProgressDialog();

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        List<Uri> uris = FileUtilsV2.getUriListFromIntent(data, getContext());


        Model model=new Model(){
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                super.onSuccess(taskType, data, msg, queue);
            }
        };
        Task task = new Task(model,123,getContext()){
            @Override
            protected Dataget doInBackground(TaskParams... params) {
                for (int i = 0; i < uris.size(); i++) {
                    OkHttpClient client = new OkHttpClient();

                    Bitmap bmp = FileUtilsV2.getBitmapFromUri(uris.get(i),getContext());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    bmp.recycle();

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", "SP"+productId+"_"+i+".jpg",
                                    RequestBody.create(byteArray, null))
                            .build();
                    Request request = new Request.Builder().url("http://file.lemyde.com:3003/ups").post(requestBody).build();
                    System.out.println("Request: "+request.body().toString());


                    try {
                        Response response = client.newCall(request).execute();
                        System.out.println("Response: " + response.body().string());

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error: "+e);
                    }
                }

                return super.doInBackground(params);
            }
        };
        task.exe(null);

    }
}
