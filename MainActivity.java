package com.example.datamanager01;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
    private static final int REGIST_REQUEST_CODE = 1; //リクエストコード（登録用）
    private static final int UPDATE_REQUEST_CODE = 2; //リクエストコード（編集用）
    private static final int DELETE_REQUEST_CODE = 3; //リクエストコード（削除用）

    private DBHelper helper; //DBHelperクラス
    private ListView listView; //ListViewクラス
    private ArrayList<Product> products; //可変長配列ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DBHelperクラスのオブジェクトを生成する。
        helper = new DBHelper(this);

        //「登録」ボタンのオブジェクトを取得とイベントリスナ登録
        Button registButton = (Button)findViewById(R.id.registMainButton);
        registButton.setOnClickListener(this);

        //「編集」ボタンのオブジェクトを取得とイベントリスナ登録
        Button updateButton = (Button)findViewById(R.id.updateMainButton);
        updateButton.setOnClickListener(this);

        //「削除」ボタンのオブジェクトを取得とイベントリスナ登録
        Button deleteButton = (Button)findViewById(R.id.deleteMainButton);
        deleteButton.setOnClickListener(this);

        //ListViewのオブジェクトを取得する。
        listView = (ListView)findViewById(R.id.listView1);

        //ListViewの単一選択モード（ListView.CHOICE_MODE_SINGLE）に設定
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //商品情報格納用配列productsを生成
        products = new ArrayList<Product>();

        //SQLiteDatabaseクラスを取得（読込み用）
        SQLiteDatabase db = helper.getReadableDatabase();

        //Productsテーブルからデータを取得する列を設定
        String[] columns = {"id", "name", "price"};

        //Productsテーブルからレコードを取得
        Cursor cursor = db.query("Products", columns, null, null, null, null, null);

        //レコードがある間繰り返し処理
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0); //IDを取得
            String name = cursor.getString(1); //商品名を取得
            int price = cursor.getInt(2); //単価を取得

            //配列に商品情報を格納
            products.add(
                    new Product(id, name, price)
            );
        }

        //ListViewに表示する商品名の取得と設定
        String[] items = new String[products.size()];

        for(int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            items[i] = product.getName();
        }

        //ArrayAdapterのオブジェクトを生成
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_single_choice, items);

        //ListViewにArrayAdapterのオブジェクトを設定
        listView.setAdapter(adapter);

        //ListViewの先頭項目を選択状態に設定
        listView.setItemChecked(0, true);

        //データベースをクローズ
        db.close();
    }

    @Override
    public void onClick(View v) {
        //押されたボタンのIDを取得
        int id = v.getId();

        Intent intent = null; //インテント
        Product product = null; //商品情報
        int requestCode = 0; //リクエストコード

        switch (id) {
            case R.id.registMainButton : //「登録」ボタンを押した場合
                //変数requestCodeにREGIST_REQUEST_CODEの値を設定
                requestCode = REGIST_REQUEST_CODE;

                //「RegistActivity.class」を呼び出せるようにインテントを生成
                intent = new Intent(this, RegistActivity.class);

                break;
            case R.id.updateMainButton : //「編集」ボタンを押した場合
                //変数requestCodeにUPDATE_REQUEST_CODEの値を設定
                requestCode = UPDATE_REQUEST_CODE;

                //選択したリストのProduct情報を取得
                product = products.get(listView.getCheckedItemPosition());

                //「UpdateActivity.class」を呼び出せるようにインテントを生成
                intent = new Intent(this, UpdateActivity.class);

                //取得したProductをインテントに「PRODUCT」というキーで設定
                intent.putExtra("PRODUCT", product);

                break;
            case R.id.deleteMainButton : //「削除」ボタンを押した場合
                //変数requestCodeにDELETE_REQUEST_CODEの値を設定
                requestCode = DELETE_REQUEST_CODE;

                //選択したリストのProduct情報を取得
                product = products.get(listView.getCheckedItemPosition());

                //「DeleteActivity.class」を呼び出せるようにインテントを生成
                intent = new Intent(this, DeleteActivity.class);

                //取得したProductをインテントに「PRODUCT」というキーで設定
                intent.putExtra("PRODUCT", product);

                break;
        }

        //戻り値となるインテントを取得できる形でアクティビティを起動
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        String s = ""; //トースト表示用文字列を格納する変数

        //第三引数に渡されたインテントに設定した情報を取得
        Bundle bundle = intent.getExtras();

        switch (requestCode) {
            case REGIST_REQUEST_CODE : //リクエストコード（登録用）
                if (resultCode == RESULT_OK) { //結果コードが「RESULT_OK」の場合
                    s = bundle.getString("SUCCESS_MESSAGE"); //「SUCCESS_MESSAGE」を取得
                } else {  //結果コードが「RESULT_CANCELED」の場合
                    s = bundle.getString("CANCELED_MESSAGE"); //「CANCELED_MESSAGE」を取得
                }

                break;
            case UPDATE_REQUEST_CODE : //リクエストコード（編集用）
                if (resultCode == RESULT_OK) { //結果コードが「RESULT_OK」の場合
                    s = bundle.getString("SUCCESS_MESSAGE"); //「SUCCESS_MESSAGE」を取得
                } else {  //結果コードが「RESULT_CANCELED」の場合
                    s = bundle.getString("CANCELED_MESSAGE"); //「CANCELED_MESSAGE」を取得
                }

                break;
            case DELETE_REQUEST_CODE : //リクエストコード（削除用）
                if (resultCode == RESULT_OK) { //結果コードが「RESULT_OK」の場合
                    s = bundle.getString("SUCCESS_MESSAGE"); //「SUCCESS_MESSAGE」を取得
                } else {  //結果コードが「RESULT_CANCELED」の場合
                    s = bundle.getString("CANCELED_MESSAGE"); //「CANCELED_MESSAGE」を取得
                }

                break;
        }

        //トーストでメッセージを表示
        Toast t = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        t.show();
    }
}
