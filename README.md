# CardView



![image](http://img.blog.csdn.net/20170608203305743?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaGFpX3FpbmdfeHVfa29uZw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


CardGroupView控件提供了以下方法：
```
    /**
     * 移除顶部卡片(无动画)
     */
    public void removeTopCard()

     /**
     * 移除顶部卡片（有动画）
     *
     * @param left 向左吗
     */
    public void removeTopCard(boolean left)

    /**
     * 当剩余卡片等于size时，加载更多
     */
    public void setloadSize(int size)

    /**
     * 加载更多监听
     *
     * @param listener {@link LoadMore}
     */
    public void setLoadMoreListener(LoadMore listener)

     /**
     * 左右滑动监听
     *
     * @param listener {@link LeftOrRight}
     */
    public void setLeftOrRightListener(LeftOrRight listener)


```


在Activity中设置CardGroupView并添加卡片：
```
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
        mCardGroupView.setloadSize(3);
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
```
