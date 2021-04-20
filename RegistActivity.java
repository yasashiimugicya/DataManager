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

public class RegistActivity extends Activity implements OnClickListener {
    private DBHelper helper; //DBHelperクラス
    private EditText registNameEdit; //名前入力用EditText
    private EditText registPriceEdit; //単価入力用EditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //activity_regist.xmlに作成した内容を画面に表示
        setContentView(R.layout.activity_regist);

        //DBHelperクラスのオブジェクトを生成
        helper = new DBHelper(this);

        //「登録」ボタンのオブジェクトを取得とイベントリスナ登録
        Button registButton = (Button)findViewById(R.id.registButton);
        registButton.setOnClickListener(this);

        //「中止」ボタンのオブジェクトを取得とイベントリスナ登録
        Button cancelButton = (Button)findViewById(R.id.registCancelButton);
        cancelButton.setOnClickListener(this);

        //名前入力用EditTextのオブジェクトを取得
        registNameEdit = (EditText)findViewById(R.id.registNameEdit);

        //単価入力用EditTextのオブジェクトを取得
        registPriceEdit = (EditText)findViewById(R.id.registPriceEdit);
    }

    @Override
    public void onClick(View v) {
        //Intentクラスのオブジェクトを生成
        Intent intent = new Intent();

        //押されたボタンのIDを取得
        int id = v.getId();

        switch (id) {
            case R.id.registButton : //「登録」ボタンを押した場合
                if (registRecord()) { //登録成功
                    //インテントに文字列「登録しました」を設定
                    intent.putExtra("SUCCESS_MESSAGE", "登録しました");

                    //結果（RESULT_OK、Intent）を設定
                    setResult(RESULT_OK, intent);
                } else { //登録失敗
                    //インテントに文字列「登録に失敗しました」を設定
                    intent.putExtra("CANCELED_MESSAGE", "登録に失敗しました");

                    //結果（RESULT_CANCELED、Intent）を設定
                    setResult(RESULT_CANCELED, intent);
                }

                break;
            case R.id.registCancelButton : //「中止」ボタンを押した場合
                //インテントに文字列「登録をキャンセルしました」を設定
                intent.putExtra("CANCELED_MESSAGE", "登録をキャンセルしました");

                //結果（RESULT_CANCELED、Intent）を設定
                setResult(RESULT_CANCELED, intent);

                break;
        }

        //RegistActivityを終了
        finish();
    }

    private boolean registRecord() {
        //SQLiteDatabaseクラスを取得（書込み用）
        SQLiteDatabase db = helper.getWritableDatabase();

        //名前入力用EditTextから商品名を取得
        String name = registNameEdit.getText().toString();

        //単価入力用EditTextから単価を取得
        int price = Integer.parseInt(registPriceEdit.getText().toString());

        //Productsテーブルに登録するレコードの設定準備
        ContentValues value = new ContentValues();
        value.put("name", name);
        value.put("price", price);

        //Productsテーブルに登録
        boolean judge = db.insert("Products", null, value) != -1 ? true : false;

        //データベースをクローズ
        db.close();

        //登録結果情報を返す
        return judge;
    }
}
