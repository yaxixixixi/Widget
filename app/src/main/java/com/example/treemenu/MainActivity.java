package com.example.treemenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockApplication;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import com.example.treemenu.dao.DataDao;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    private Button btn_ShowPopupWindow;
    private LinearLayout mRootView;
    private App app;
    private PopupTreeMenu mPopupTreeMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootView = findViewById(R.id.root_view);

        btn_ShowPopupWindow = (Button) findViewById(R.id.show_popup);

        btn_ShowPopupWindow.setOnClickListener(this);


        List<Data> datas = new ArrayList<>();

        app = (App) getApplication();
        DataDao dataDao = app.getDaoSession().getDataDao();
        dataDao.deleteAll();

        for (int i = 1; i < 10; i++) {
            Data data = new Data((long) i, "层级1--" + i, -123456D, -123456L);
            datas.add(data);
            for (int j = 1; j < 10; j++) {
                Data data1 = new Data((long) i * 10 + j, "层级2--" +i+ j, -123456L, (long) i);
                datas.add(data1);
                for (int k = 1; k < 10; k++) {
                    Data data2 = new Data((long) i*100+j * 10 + k, "层级3--" +i+ j+ k, -123456L, (long) i * 10 + j);
                    datas.add(data2);
                    for (int l = 1; l < 10; l++) {
                        Data data3 = new Data((long) i * 1000 + j * 100 + k * 10 + l, "层级4--" +i+ j+ k+ l, i * j * k * l, (long)  i*100+j * 10 + k);
                        datas.add(data3);
                    }
                }
            }
        }


        dataDao.insertOrReplaceInTx(datas);



//        for (Data datum : data) {
//            System.out.println(datum);
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_popup:
                showPopupWindow();
                break;
            default:
                break;
        }
    }

    private void showPopupWindow() {

        mPopupTreeMenu = new PopupTreeMenu(this, app).buildView();
        mPopupTreeMenu.show(mRootView);


    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart" + btn_ShowPopupWindow.getMeasuredWidth());

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume" + btn_ShowPopupWindow.getMeasuredWidth());
    }

}
