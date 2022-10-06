package com.project.shop.lemy.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.shop.lemy.R;
import com.project.shop.lemy.adapter.SimpleAdapter;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.common.DateSuppost;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.project.shop.lemy.helper.StringHelper;
import com.project.shop.lemy.listener.ListenBack;
import com.project.shop.lemy.listener.Listerner;
import com.project.shop.lemy.xuatnhapkho.NhapKhoSupport;
import com.telpoo.frame.delegate.Idelegate;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.Mlog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DialogSupport {

    public static void dialogXoaTypeSMS(final Context context, final Listerner.OnClick clickDelete) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle("Xóa tin nhắn mẫu")
                .setMessage("Bạn chắc chắn muốn xóa tin nhắn mẫu?")
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    clickDelete.clickView();
                    dialogInterface.dismiss();
                    Toast.makeText(context, "Đã xóa tin nhắn mẫu", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    public static void dialogXoasp(final Context context, final Listerner.OnClick clickDelete) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle("Bạn có chắc chắn xóa sản phẩm")
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    clickDelete.clickView();
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    public static void dialogXoaShop(final Context context, final Listerner.OnClick clickDelete) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle("Xóa shop")
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    clickDelete.clickView();
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    public static void dialogXoaDvvc(final Context context, final Listerner.OnClick clickDelete) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle("Xóa Đơn vị vận chuyển")
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    clickDelete.clickView();
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    public static void dialogXoaKhachHang(final Context context, final Listerner.OnClick clickDelete) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle("Xóa khách hàng")

                .setPositiveButton("Có", (dialogInterface, i) -> {
                    clickDelete.clickView();
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    /** NAQ
     * dùng 1 cái đủ rồi các má ơi :D
     * @param
     * @param context

     */

    public static void dialogYesNoWithView(String title,String message,View v,final Context context, final Listerner.YesNoDialog ListenClick) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(title)
                .setMessage(message)
                .setView(v)
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    ListenClick.onClickYesNo(true);
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    ListenClick.onClickYesNo(false);
                });
        builder.show();
    }

    public static void dialogYesNo(String content,final Context context, final Listerner.YesNoDialog ListenClick) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setMessage(content)

                .setPositiveButton("Có", (dialogInterface, i) -> {
                    ListenClick.onClickYesNo(true);
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    ListenClick.onClickYesNo(false);
                });
        builder.show();
    }

    public static void dialogYesNo(boolean isDontshow,String title,final Context context, final Listerner.YesNoDialog ListenClick) {
        if (isDontshow){
            ListenClick.onClickYesNo(true);
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(title)
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    ListenClick.onClickYesNo(true);
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    ListenClick.onClickYesNo(false);
                });
        builder.show();
    }

    public static void dialogThongBao(String title,final Context context, final ListenBack action) {
        Mlog.D("ahihi "+title);
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_thongbao, null);
        ((TextView)view.findViewById(R.id.tv)).setText(title);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setMessage("Thông báo")
                .setView(view)
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    if (action!=null)
                    action.OnListenBack("yes");
                    dialogInterface.dismiss();
                });

        builder.show();
    }

    /**
     * addtelpoo

     * @param context
     * @return
     */

    public static  AlertDialog inputText(Context context,String s, Idelegate idelegate){
        if (idelegate==null) idelegate= new Idelegate() {
            @Override
            public void callBack(Object value, int where) {

            }
        };
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setText(s);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(input);
        builder.setCancelable(true);
        Idelegate finalIdelegate = idelegate;
        builder.setNegativeButton("OK",(dialogInterface, i) -> {
            finalIdelegate.callBack(input.getText().toString(),1);
        });
        builder.show();
        return builder.create();
    }

    public static AlertDialog dialogCustom(View v, final Context context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);

        AlertDialog dl = builder.create();
        dl.show();
        return dl;
    }

    public static AlertDialog manyButton(String title,String ct1,String ct2,String ct3,String ct4,  Context context, ListenBack listenBack) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_thongbao_nhieubutton, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        AlertDialog dl = builder.create();
        ((TextView)view.findViewById(R.id.title)).setText(title);

        if (ct1!=null){
            ((Button)view.findViewById(R.id.btn1)).setText(ct1);
            ((Button)view.findViewById(R.id.btn1)).setVisibility(View.VISIBLE);
        }
        if (ct2!=null){
            ((Button)view.findViewById(R.id.btn2)).setText(ct2);
            ((Button)view.findViewById(R.id.btn2)).setVisibility(View.VISIBLE);
        }
        if (ct3!=null){
            ((Button)view.findViewById(R.id.btn3)).setText(ct3);
            ((Button)view.findViewById(R.id.btn3)).setVisibility(View.VISIBLE);
        }
        if (ct4!=null){
            ((Button)view.findViewById(R.id.btn4)).setText(ct4);
            ((Button)view.findViewById(R.id.btn4)).setVisibility(View.VISIBLE);
        }


        ((Button)view.findViewById(R.id.btn1)).setOnClickListener(v -> {
            listenBack.OnListenBack(1);
            dl.dismiss();
        });

        ((Button)view.findViewById(R.id.btn2)).setOnClickListener(v -> {
            listenBack.OnListenBack(2);
            dl.dismiss();
        });

        ((Button)view.findViewById(R.id.btn3)).setOnClickListener(v -> {
            listenBack.OnListenBack(3);
            dl.dismiss();
        });


        ((Button)view.findViewById(R.id.btn4)).setOnClickListener(v -> {
            listenBack.OnListenBack(4);
            dl.dismiss();
        });


        dl.show();
        return dl;
    }


    /**
     * dialog cho chọn khoang và số lượng để nhập kho hoặc xuất

     */
    public  void dialogXNKChonKhoangSL( List<BaseObject> ojsKhoangSL, BaseObject ojProductXN, Context context, ListenBack listenBack) {



        View vEdit = LayoutInflater.from(context).inflate(R.layout.view_edit_row_nhaphang,null,false);
        TextView tvNameSp = vEdit.findViewById(R.id.tvNameSp);
        SearchableSpinner searchKhoang = vEdit.findViewById(R.id.searchKhoang);
        EditText tvSl = vEdit.findViewById(R.id.tvSl);
        View tru = vEdit.findViewById(R.id.tru);
        View cong = vEdit.findViewById(R.id.cong);
        View addThem =vEdit.findViewById(R.id.addThem);
        RecyclerView lv = vEdit.findViewById(R.id.lv);
        TextView tvTTChon =vEdit.findViewById(R.id.tvTTChon);
        tvNameSp.setText(ojProductXN.get(ProductObj.name));
        List<BaseObject> ojChon = new ArrayList<>();
        SimpleAdapter arr= new SimpleAdapter(context,new String[0], R.layout.item_dialogxkn1);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(context));
        lv.setAdapter(arr);
        arr.setOnclick(position -> {
            ojChon.remove(position);

            arr.setData(tmp1(ojChon));
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setView(vEdit)
                .setPositiveButton("Lưu", (dialogInterface, i) -> {
                    listenBack.OnListenBack(NhapKhoSupport.setKhoangChon(ojProductXN,ojChon));
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());

        builder.show();

        ojChon.addAll(NhapKhoSupport.getDataKhoangFromOj(ojProductXN));
        arr.setData(tmp1(ojChon));
        BaseObject ojKhoangSelected= new BaseObject();

        MenuFromApiSupport.showKhoangNhap(ojsKhoangSL,context,searchKhoang,khoang_id -> {
            ojKhoangSelected.set("khoang_id",khoang_id);
            tvTTChon.setText("Khoang đã chọn: "+ojKhoangSelected.get("khoang_id")+"--Số lượng: "+tvSl.getText().toString());
        });


        addThem.setOnClickListener(view -> {

            int vl= 0;
            try {
                vl=Integer.parseInt(tvSl.getText().toString());
            } catch (Exception e){
                e.printStackTrace();
            }

            if (vl<=0){
                showToast("Số lượng nhập không đúng",context);
                return;
            }

            for (int i = 0; i < ojChon.size(); i++) {
                if (ojKhoangSelected.get("khoang_id").equals(ojChon.get(i).get("khoang_id"))){
                    showToast("Khoang này đã chọn rồi!",context); return;
                }

            }

            BaseObject ojThem1= new BaseObject();
            ojThem1.set("khoang_id",ojKhoangSelected.get("khoang_id"));
            ojThem1.set("sl",vl);
            ojChon.add(ojThem1);

            arr.setData(tmp1(ojChon));

        });

        View.OnClickListener tanggiam= view -> {

                int vl= 0;
                try {
                    vl=Integer.parseInt(tvSl.getText().toString());
                } catch (Exception e){
                    e.printStackTrace();
                };

                if (view.getId()==R.id.tru) vl=vl-1;
                else  vl=vl+1;

                if (vl<0) vl=0;

                tvSl.setText(String.valueOf(vl));

        };

        tru.setOnClickListener(tanggiam);
        cong.setOnClickListener(tanggiam);
        tvSl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvTTChon.setText("Khoang đã chọn: "+ojKhoangSelected.get("khoang_id")+"--Số lượng: "+tvSl.getText().toString());
            }
        });
    }

    private String[] tmp1(List<BaseObject> ojChon){
        String[] ss= new String[ojChon.size()];
        for (int i = 0; i < ojChon.size(); i++) {
            ss[i]="Khoang: "+ ojChon.get(i).get("khoang_id")+"     --     Số lượng:"+ojChon.get(i).get("sl");
        }
        return ss;
    }


    private void showToast(String text,Context context){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }



    public static void dialogUpdateTrangThai(final Context context, final Listerner.OnClick clickUpdate) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle("Bạn muốn thay đổi thông tin đơn hàng?")
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    clickUpdate.clickView();
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    public static void setupSpinner(Spinner spinner, Context context, int resource, String[] hmData, ListenBack listenBack) {

        if (spinner==null||context==null||hmData==null) return;


        spinner.setAdapter(new ArrayAdapter<>(context, resource, hmData));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String type = spinner.getSelectedItem().toString();
                if (listenBack != null) listenBack.OnListenBack(type);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public static void setupSpinnerThoiGian(Spinner spinner, Context context, int resource, String[] hmData, Listerner.OnOkDialogClick onOkDialogClick) {

        spinner.setAdapter(new ArrayAdapter<>(context, resource, hmData));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String type = spinner.getSelectedItem().toString();
                BaseObject object = new BaseObject();
                if (type.equals("Tùy chọn")) {
                    dialogChonDate(context, onOkDialogClick);
                }
                if (onOkDialogClick != null) onOkDialogClick.onOkDialogClick(object);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public static void dialogChonDate(Context context, Listerner.OnOkDialogClick onOkDialogClick) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_date);
        dialog.setCancelable(false);
        dialog.setTitle("Chọn thời gian:");
        TextView tvDateBatDau = dialog.findViewById(R.id.tvDateBatDau);
        TextView tvDateCuoi = dialog.findViewById(R.id.tvDateCuoi);
        TextView btnOk = dialog.findViewById(R.id.btnOk);
        TextView btnHuy = dialog.findViewById(R.id.btnHuy);

        tvDateBatDau.setText(DateSuppost.setDate());
        tvDateCuoi.setText(DateSuppost.setDate());

        tvDateBatDau.setOnClickListener(view -> DateSuppost.chonDate(context, tvDateBatDau));
        tvDateCuoi.setOnClickListener(view -> DateSuppost.chonDate(context, tvDateCuoi));
        btnHuy.setOnClickListener(view -> dialog.dismiss());

        btnOk.setOnClickListener(view -> {
            String ngayBatDau = tvDateBatDau.getText().toString();
            String ngayCuoi = tvDateCuoi.getText().toString();
            BaseObject object = new BaseObject();
            object.set("start_date", ngayBatDau);
            object.set("end_date", ngayCuoi);
            onOkDialogClick.onOkDialogClick(object);
            dialog.dismiss();
        });
        dialog.show();
    }

    public static void dialogCOD(final Context context, String sms, final Listerner.OnClick clickCod) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(sms)
                .setCancelable(false)
                .setPositiveButton("Tiếp tục", (dialogInterface, i) -> {
                    clickCod.clickView();
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    public static void dialogPaymentCod(final Context context, final Listerner.OnClick clickCod) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle("Khách chưa chuyển khoản đủ tiền, có cho gửi đi không?")
                .setCancelable(false)
                .setPositiveButton("Tiếp tục", (dialogInterface, i) -> {
                    clickCod.clickView();
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    /**
     * Thông báo nhắc nhở quan trọng và bắt nhập chữ để khỏi bấm mà ko nhìn :D
     */

   public static void dialogThongBaoKemXacNhanBangChu(String title,String textXacNhan,final Context context, final Listerner.YesNoDialog isOk) {
       if (isOk!=null&&(textXacNhan==null||textXacNhan.length()==0))
           isOk.onClickYesNo(true);
       long lastTimeShow= Calendar.getInstance().getTimeInMillis();
       String title1=title+": "+textXacNhan ;

       String textXacnNhanNew= StringHelper.deAccent(textXacNhan).replaceAll("\\s","").toLowerCase();

       final EditText input = new EditText(context);
       LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
               LinearLayout.LayoutParams.MATCH_PARENT,
               LinearLayout.LayoutParams.MATCH_PARENT);
       input.setLayoutParams(lp);


            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder .setCancelable(false)
                    .setTitle(title1)
                    .setView(input)
                    .setPositiveButton("Đã đọc Kỹ", (dialogInterface, i) -> {
                        long curT= Calendar.getInstance().getTimeInMillis();
                        if (curT-lastTimeShow<7*1000){
                            Toast.makeText(context, "Hãy đọc kỹ ghi chú", Toast.LENGTH_LONG).show();
                            dialogThongBaoKemXacNhanBangChu(title,textXacNhan,context,isOk);
                            return;
                        }
                        if (input.getText().toString().length()<3&&textXacnNhanNew.length()>3) {
                            Toast.makeText(context, "Phải nhập nhiều hơn 3 ký tự", Toast.LENGTH_LONG).show();
                            dialogThongBaoKemXacNhanBangChu(title,textXacNhan,context,isOk);
                            return;
                        }

                        String traloi= StringHelper.deAccent(input.getText().toString()).replaceAll("\\s","").toLowerCase();

                        if (!textXacnNhanNew.contains(traloi)) {

                            Toast.makeText(context, "Hãy Nhập 1 từ có trong thông báo!", Toast.LENGTH_LONG).show();
                            dialogThongBaoKemXacNhanBangChu(title,textXacNhan,context,isOk);
                            return;
                        }

                        if (isOk!=null)
                            isOk.onClickYesNo(true);

                    });

            builder.show();
        }

}
