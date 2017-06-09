package com.glh.cardview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.glh.cardview.card.CardGroupView;

public class MainActivity extends AppCompatActivity {

    private CardGroupView mCardGroupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        addCard();
    }


    private void initView() {
        mCardGroupView = (CardGroupView) findViewById(R.id.card);
        mCardGroupView.setLoadSize(3);
        mCardGroupView.setMargin(0.15);
    }

    private void initEvent() {
        mCardGroupView.setLoadMoreListener(new CardGroupView.LoadMore() {
            @Override
            public void load() {
                mCardGroupView.addView(getCard());
                mCardGroupView.addView(getCard());
                mCardGroupView.addView(getCard());
                mCardGroupView.addView(getCard());
                mCardGroupView.addView(getCard());
                mCardGroupView.addView(getCard());
                mCardGroupView.addView(getCard());
                mCardGroupView.addView(getCard());
            }
        });
        mCardGroupView.setLeftOrRightListener(new CardGroupView.LeftOrRight() {
            @Override
            public void leftOrRight(boolean left) {
                if (left) {
                    Toast.makeText(MainActivity.this, "向左滑喜欢！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "向右滑不喜欢！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addCard() {
        mCardGroupView.addView(getCard());
        mCardGroupView.addView(getCard());
        mCardGroupView.addView(getCard());
        mCardGroupView.addView(getCard());
        mCardGroupView.addView(getCard());
        mCardGroupView.addView(getCard());
        mCardGroupView.addView(getCard());
        mCardGroupView.addView(getCard());
    }

    private View getCard() {
        View card = LayoutInflater.from(this).inflate(R.layout.layout_card, null);
        View view = card.findViewById(R.id.remove);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardGroupView.removeTopCard(true);
            }
        });
        return card;
    }
}
