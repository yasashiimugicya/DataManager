package com.example.datamanager01;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DeleteActivity extends Activity implements OnClickListener {
    private DBHelper helper; //DBHelperクラス
    private Product product; //Productクラス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //activity_delete.xmlに作成した内容を画面に表示
        setContentView(R.layout.activity_delete);

        //インテントを取得
        Intent intent = getIntent();

        //インテントからBundleを取得
        Bundle bundle = intent.getExtras();

        //Bundleから「PRODUCT」というキーで設定しているProductオブジェクトを取得
        product = (Product)bundle.getSerializable("PRODUCT");

        //DBHelperクラスのオブジェクトを生成
        helper = new DBHelper(this);

        //「削除」ボタンのオブジェクトを取得とイベントリスナ登録
        Button deleteButton = (Button)findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        //「中止」ボタンのオブジェクトを取得とイベントリスナ登録
        Button cancelButton = (Button)findViewById(R.id.deleteCancelButton);
        cancelButton.setOnClickListener(this);

        //名前表示用TextViewのオブジェクトを取得し
        //Productオブジェクトのメンバ変数nameの値を表示
        TextView deleteNameText = (TextView)findViewById(R.id.deleteNameText);
        deleteNameText.setText(product.getName());

        //単価入力用TextViewのオブジェクトを取得し
        //Productオブジェクトのメンバ変数priceの値を表示
        TextView deletePriceText = (TextView)findViewById(R.id.deletePriceText);
        deletePriceText.setText(Integer.toString(product.getPrice()));
    }

    @Override
    public void onClick(View v) {
        //Intentクラスのオブジェクトを生成
        Intent intent = new Intent();

        //押されたボタンのIDを取得
        int id = v.getId();

        switch (id) {
            case R.id.deleteButton : //「削除」ボタンを押した場合
                if (deleteRecord()) { //削除成功
                    //インテントに文字列「削除しました」を設定
                    intent.putExtra("SUCCESS_MESSAGE", "削除しました");

                    //結果（RESULT_OK、Intent）を設定
                    setResult(RESULT_OK, intent);
                } else { //削除失敗
                    //インテントに文字列「削除に失敗しました」を設定
                    intent.putExtra("CANCELED_MESSAGE", "削除に失敗しました");

                    //結果（RESULT_CANCELED、Intent）を設定
                    setResult(RESULT_CANCELED, intent);
                }

                break;
            case R.id.deleteCancelButton : //「中止」ボタンを押した場合
                //インテントに文字列「削除をキャンセルしました」を設定
                intent.putExtra("CANCELED_MESSAGE", "削除をキャンセルしました");

                //結果（RESULT_CANCELED、Intent）を設定
                setResult(RESULT_CANCELED, intent);

                break;
        }

        //DeleteActivityを終了
        finish();
    }

    private boolean deleteRecord() {
        //SQLiteDatabaseクラスを取得（書込み用）
        SQLiteDatabase db = helper.getWritableDatabase();

        //Productオブジェクトのメンバ変数idを取得
        int id = product.getId();

        //レコードを変更の条件指定
        String condition = "id=" + id;

        //Productsテーブルからレコードを削除
        boolean judge = db.delete("Products", condition, null) != -1 ? true : false;

        //データベースをクローズ
        db.close();

        //削除結果情報を返す
        return judge;
    }
}
