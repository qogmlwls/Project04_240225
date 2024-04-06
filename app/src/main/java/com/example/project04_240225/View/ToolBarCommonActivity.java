package com.example.project04_240225.View;

import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ToolBarCommonActivity  extends AppCompatActivity {


    protected void 툴바뒤로가기설정(Toolbar toolbar){

        setSupportActionBar(toolbar);

        // 뒤로 가기 버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }


    protected<T> void startActivity(Class<T> tClass){
        Intent intent = new Intent(ToolBarCommonActivity.this, tClass);
        startActivity(intent);
    }


    // Toolbar의 아이템 클릭 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // 뒤로 가기 동작
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


}
