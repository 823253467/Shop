package com.bwei.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private List<ShopBean.OrderDataBean.CartlistBean> mAllOrderList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView text_qujieshuan;
    private TextView text_shangpingeshu;
    private TextView text_zongjia;
    private CheckBox checkbox_quanxuan;
    private ShopAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewid);
        text_qujieshuan = (TextView) findViewById(R.id.text_qujieshuan);
        text_shangpingeshu = (TextView) findViewById(R.id.text_shangpingeshu);
        text_zongjia = (TextView) findViewById(R.id.text_zongjia);
        checkbox_quanxuan = (CheckBox) findViewById(R.id.checkbox_quanxuan);
        adapter = new ShopAdapter(this);
        getData();

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setListener(new ShopAdapter.UpdateUiListener() {
            @Override
            public void setTotal(String total, String num,boolean allCheck) {

                checkbox_quanxuan.setChecked(allCheck);
                text_shangpingeshu.setText(num);
                text_zongjia.setText(total);
            }
        });
        //全选点击事件
        checkbox_quanxuan.setOnClickListener(this);

    }
    public void getData() {
        try {
            //模拟网络请求
            InputStream inputStream = getAssets().open("shop.json");
            String data = convertStreamToString(inputStream);
            Gson gson = new Gson();
            ShopBean shopBean = gson.fromJson(data, ShopBean.class);
            Toast.makeText(this, shopBean.getOrderData().get(1).getShopName(), Toast.LENGTH_SHORT).show();
            adapter.add(shopBean);

            for (int i = 0; i < shopBean.getOrderData().size(); i++) {
                int length = shopBean.getOrderData().get(i).getCartlist().size();
                for (int j = 0; j < length; j++) {
                    mAllOrderList.add(shopBean.getOrderData().get(i).getCartlist().get(j));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String convertStreamToString(InputStream is) {
        /*
          * To convert the InputStream to String we use the BufferedReader.readLine()
          * method. We iterate until the BufferedReader return null which means
          * there's no more data to read. Each line will appended to a StringBuilder
          * and returned as String.
          */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    @Override
    public void onClick(View view) {
        adapter.selectAll(checkbox_quanxuan.isChecked());
    }
}

