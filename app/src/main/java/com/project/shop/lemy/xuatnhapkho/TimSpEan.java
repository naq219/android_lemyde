package com.project.shop.lemy.xuatnhapkho;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.widget.EditText;

import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.sanpham.ChiTietSanPhamActivity;
import com.project.shop.lemy.search.SearchSugestAdapter;


public class TimSpEan extends AppCompatActivity {
    protected SearchView searchChonSanPhamNh;
    protected SearchSugestAdapter searchSugestAdapter;
    private String intentEan=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_sp_ean);
        intentEan = getIntent().getStringExtra("ean");
        searchChonSanPhamNh = findViewById(R.id.searchChonSanPhamNh);
        EditText searchSanPhamNh = searchChonSanPhamNh.findViewById(androidx.appcompat.R.id.search_src_text);
        searchSanPhamNh.setTextColor(ResourcesCompat.getColor(getResources(), R.color.mau_nen_chung, null));
        searchSanPhamNh.setTextSize(14);
        searchChonSanPhamNh.setQueryHint("Tìm tên sản phẩm");
        searchChonSanPhamNh.setIconifiedByDefault(false);

        searchSugestAdapter = new SearchSugestAdapter(TimSpEan.this);

        searchChonSanPhamNh.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                searchSugestAdapter.textChangeProduct(newText);
                searchChonSanPhamNh.setSuggestionsAdapter(searchSugestAdapter);

                return false;
            }
        });



        searchSugestAdapter.setOnItemClickListerer((object, position) -> {
            if (position == TaskType.SEARCH_PRODUCT_V2) {

                searchChonSanPhamNh.clearFocus();
                searchChonSanPhamNh.setQuery("", false);

                String ProductId=object.get("id");

                Intent it=new Intent(TimSpEan.this, ChiTietSanPhamActivity.class);
                it.putExtra("ean",intentEan);
                it.putExtra("id",ProductId);
                startActivityForResult(it,123);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
