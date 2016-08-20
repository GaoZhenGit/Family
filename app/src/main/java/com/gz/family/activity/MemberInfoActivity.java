package com.gz.family.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.gz.family.R;
import com.gz.family.model.User;
import com.squareup.picasso.Picasso;

/**
 * Created by host on 2016/2/5.
 */
public class MemberInfoActivity extends BasePageActivity {
    User selectMember;
    AQuery aQuery;
    @Override
    protected void initData() {
        if (getIntent().getExtras()!=null){
            selectMember = (User) getIntent().getExtras().getSerializable("selectMember");
        }else {
            finish();
        }
    }

    @Override
    protected void initLayoutView() {
        setContentView(R.layout.activity_member_info);
        aQuery = new AQuery(this);
    }

    @Override
    protected void initView() {
        aQuery.id(R.id.title_mid_text).text("详细资料");
        aQuery.id(R.id.title_left_img).visible();

        aQuery.id(R.id.me_name).text(selectMember.getName());
        aQuery.id(R.id.me_detail).text(selectMember.getDetail());
        Picasso.with(this).load(selectMember.getAvatar())
                .into((ImageView) findViewById(R.id.me_avatar));
        if (selectMember.getSex() == User.MALE) {
            aQuery.id(R.id.me_sex).image(R.mipmap.male);
        } else {
            aQuery.id(R.id.me_sex).image(R.mipmap.female);
        }
    }

    @Override
    protected void setListener() {
        aQuery.id(R.id.title_left_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        aQuery.id(R.id.btn_member_info_chat).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("chatMember",selectMember);
                Intent intent = new Intent(getBaseContext(),ChatActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}
