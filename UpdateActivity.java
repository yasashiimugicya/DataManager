package com.example.datamanager01;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UpdateActivity extends Activity implements OnClickListener {
    private DBHelper helper; //DBHelperクラス
    private EditText updateNameEdit; //名前入力用EditText
    private EditText updatePriceEdit; //単価入力用EditText
    private Product product; //Productクラス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //activity_update.xmlに作成した内容を画面に表示
        setContentView(R.layout.activity_update);

        //インテントを取得
        Intent intent = getIntent();

        //インテントからBundleを取得
        Bundle bundle = intent.getExtras();

        //Bundleから「PRODUCT」というキーで設定しているProductオブジェクトを取得
        product = (Product)bundle.getSerializable("PRODUCT");

        //DBHelperクラスのオブジェクトを生成
        helper = new DBHelper(this);

        //「編集」ボタンのオブジェクトを取得とイベントリスナ登録
        Button updateButton = (Button)findViewById(R.id.updateButton);
        updateButton.setOnClickListener(this);

        //「中止」ボタンのオブジェクトを取得とイベントリスナ登録
        Button cancelButton = (Button)findViewById(R.id.updateCancelButton);
        cancelButton.setOnClickListener(this);

        //名前入力用EditTextのオブジェクトを取得し
        //Productオブジェクトのメンバ変数nameの値を表示
        updateNameEdit = (EditText)findViewById(R.id.updateNameEdit);
        updateNameEdit.setText(product.getName());

        //単価入力用EditTextのオブジェクトを取得し
        //Productオブジェクトのメンバ変数priceの値を表示
        updatePriceEdit = (EditText)findViewById(R.id.updatePriceEdit);
        updatePriceEdit.setText(Integer.toString(product.getPrice()));
    }

    @Override
    public void onClick(View v) {
        //Intentクラスのオブジェクトを生成
        Intent intent = new Intent();

        //押されたボタンのIDを取得
        int id = v.getId();

        switch (id) {
            case R.id.updateButton : //「編集」ボタンを押した場合
                if (updateRecord()) { //変更成功
                    //インテントに文字列「変更しました」を設定
                    intent.putExtra("SUCCESS_MESSAGE", "変更しました");

                    //結果（RESULT_OK、Intent）を設定
                    setResult(RESULT_OK, intent);
                } else { //変更失敗
                    //インテントに文字列「変更に失敗しました」を設定
                    intent.putExtra("CANCELED_MESSAGE", "変更に失敗しました");

                    //結果（RESULT_CANCELED、Intent）を設定
                    setResult(RESULT_CANCELED, intent);
                }

                break;
            case R.id.updateCancelButton : //「中止」ボタンを押した場合
                //インテントに文字列「変更をキャンセルしました」を設定
                intent.putExtra("CANCELED_MESSAGE", "変更をキャンセルしました");

                //結果（RESULT_CANCELED、Intent）を設定
                setResult(RESULT_CANCELED, intent);

                break;
        }

        //UpdateActivityを終了
        finish();
    }

    private boolean updateRecord() {
        //SQLiteDatabaseクラスを取得（書込み用）
        SQLiteDatabase db = helper.getWritableDatabase();

        //Productオブジェクトのメンバ変数idを取得
        int id = product.getId();

        //名前入力用EditTextから商品名を取得
        String name = updateNameEdit.getText().toString();

        //単価入力用EditTextから単価を取得
        int price = Integer.parseInt(updatePriceEdit.getText().toString());

        //Productsテーブルに変更するレコードの設定準備
        ContentValues value = new ContentValues();
        value.put("name", name);
        value.put("price", price);

        //レコードを変更の条件指定
        String condition = "id=" + id;

        //Productsテーブルからレコードを変更
        boolean judge = db.update("Products", value, condition, null) != -1 ? true : false;

        //データベースをクローズ
        db.close();

        //変更結果情報を返す
        return judge;
    }
}
