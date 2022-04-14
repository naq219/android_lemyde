package com.project.shop.lemy.smskhachhang;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.adapter.SmsAdapter;
import com.project.shop.lemy.bean.SmsObj;
import com.project.shop.lemy.db.DbSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.telpoo.frame.object.BaseObject;


public class QlySmsFm extends BaseFragment {
    private RecyclerView recycleViewSms;
    final int ADD_SMS = 1;
    final int EDIT_SMS = 2;
    private ImageView imgAddSms;
    private SmsAdapter smsAdapter;

    public QlySmsFm() {
    }

    public static QlySmsFm newInstance() {
        return new QlySmsFm();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qly_sms_fm, container, false);
        imgAddSms = view.findViewById(R.id.imgAddSms);
        recycleViewSms = view.findViewById(R.id.recycleViewSms);
        recycleViewSms.setHasFixedSize(true);
        recycleViewSms.setLayoutManager(new LinearLayoutManager(getActivity()));
        smsAdapter = new SmsAdapter(getActivity());
        recycleViewSms.setAdapter(smsAdapter);
        imgAddSms.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddTypeSmsActivity.class);
            startActivityForResult(intent, ADD_SMS);
        });
        smsAdapter.setData(DbSupport.getChude());
        smsAdapter.setOnclickEdit(position -> {
            Intent intent = new Intent(getActivity(), AddTypeSmsActivity.class);
            intent.putExtra("data", smsAdapter.getItem(position));
            startActivityForResult(intent, EDIT_SMS);
        });
        smsAdapter.setOnclickDelete(position -> deleteSMS(position));
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                smsAdapter.setData(DbSupport.getChude());

    }

    void deleteSMS(int position) {
        DialogSupport.dialogXoaTypeSMS(getActivity(), () -> {
            BaseObject object = DbSupport.getChude().get(position);
            DbSupport.deleteChude(object.get(SmsObj.desc));
            smsAdapter.setData(DbSupport.getChude());
        });
    }
}
