package com.glh.cardview.card;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.glh.cardview.R;
import com.glh.cardview.util.DensityUtil;
import com.glh.cardview.util.ListUtil;

import java.util.ArrayList;

/**
 * 卡片容器
 * Created by glh on 2017-06-08.
 */
public class CardGroupView extends RelativeLayout {

    private Context mContext;

    //指定剩余卡片还剩下多少时加载更多
    private int mLoadSize = 2;
    //是否执行加载更多，加载更多时，卡片依次添加在后面的；而添加卡片时，卡片是依次添加在上面
    private boolean isLoadMore = false;
    //保存当前容器中的卡片
    private ArrayList<View> mCardList = new ArrayList<>();
    //加载更多监听器
    private LoadMore mLoadMore;
    //左右滑动监听器
    private LeftOrRight mLeftOrRight;
    private double margin = 0.10;

    public CardGroupView(Context context) {
        this(context, null);
    }

    public CardGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    public void addView(View card) {
        if (isLoadMore) {
            this.mCardList.add(ListUtil.getSize(mCardList), card);
        } else {
            this.mCardList.add(card);
        }
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        this.addView(card, 0, layoutParams);
        card.setOnTouchListener(onTouchListener);
        if (!isLoadMore) {
            this.setLayoutParams(card, mCardList.size());
        }

    }

    /**
     * 设置卡片LayoutParams
     *
     * @param card 卡片
     */
    private void setLayoutParams(View card, int index) {
        LayoutParams params = new LayoutParams(card.getLayoutParams());
        params.topMargin = (int) (DensityUtil.getDisplayMetrics(mContext).heightPixels * margin) + getResources().getDimensionPixelSize(
                R.dimen.card_item_margin) * index;
        params.bottomMargin = (int) (DensityUtil.getDisplayMetrics(mContext).heightPixels * margin) - getResources().getDimensionPixelSize(
                R.dimen.card_item_margin) * index;
        params.leftMargin = (int) (DensityUtil.getDisplayMetrics(mContext).widthPixels * margin);
        params.rightMargin = (int) (DensityUtil.getDisplayMetrics(mContext).widthPixels * margin);
        card.setLayoutParams(params);
    }

    /**
     * 每次移除时需要重置剩余卡片的位置
     */
    private void resetLayoutParams() {
        for (int i = 0; i < mCardList.size(); i++) {
            setLayoutParams(mCardList.get(i), i);
        }
    }

    private int mLastY = 0;
    private int mLastX = 0;
    private int mCardLeft;
    private int mCardTop;
    private int mCardRight;
    private int mCardBottom;
    private boolean mLeftOut = false;
    private boolean mRightOut = false;
    private boolean mOnTouch = true;

    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mOnTouch && v.equals(mCardList.get(0))) {
                int rawY = (int) event.getRawY();
                int rawX = (int) event.getRawX();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_DOWN:
                        getLayout();
                        mLastY = (int) event.getRawY();
                        mLastX = (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int offsetY = rawY - mLastY;
                        int offsetX = rawX - mLastX;
                        mCardList.get(0).layout(mCardList.get(0).getLeft() + offsetX, mCardList.get(0).getTop() + offsetY, mCardList.get(0).getRight() + offsetX, mCardList.get(0).getBottom() + offsetY);
                        mRightOut = mCardList.get(0).getLeft() > DensityUtil.getDisplayMetrics(mContext).widthPixels / 2;
                        mLeftOut = mCardList.get(0).getRight() < DensityUtil.getDisplayMetrics(mContext).widthPixels / 2;
                        mLastY = rawY;
                        mLastX = rawX;
                        break;
                    case MotionEvent.ACTION_UP:
                        change();
                        break;

                }
            }
            return true;
        }
    };

    private void getLayout() {
        mCardLeft = mCardList.get(0).getLeft();
        mCardTop = mCardList.get(0).getTop();
        mCardRight = mCardList.get(0).getRight();
        mCardBottom = mCardList.get(0).getBottom();
    }

    private void change() {
        if (mLeftOut) {
           /*
            往左边滑出
             */
            out(true);
        } else if (mRightOut) {
             /*
            往右边滑出
             */
            out(false);

        } else {
            //复位
            reset();
        }
    }

    class CardIndex {
        int left;
        int top;
        int right;
        int bottom;

        CardIndex(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        int getLeft() {
            return left;
        }

        int getTop() {
            return top;
        }

        int getRight() {
            return right;
        }

        int getBottom() {
            return bottom;
        }
    }

    class PointEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            CardIndex startPoint = (CardIndex) startValue;
            CardIndex endPoint = (CardIndex) endValue;
            int left = (int) (startPoint.getLeft() + fraction * (endPoint.getLeft() - startPoint.getLeft()));
            int top = (int) (startPoint.getTop() + fraction * (endPoint.getTop() - startPoint.getTop()));
            int right = (int) (startPoint.getRight() + fraction * (endPoint.getRight() - startPoint.getRight()));
            int bottom = (int) (startPoint.getBottom() + fraction * (endPoint.getBottom() - startPoint.getBottom()));
            return new CardIndex(left, top, right, bottom);
        }

    }

    /**
     * 卡片复位
     */
    private void reset() {
        CardIndex oldCardIndex = new CardIndex(mCardLeft, mCardTop, mCardRight, mCardBottom);
        CardIndex newCardIndex = new CardIndex(mCardList.get(0).getLeft(), mCardList.get(0).getTop(), mCardList.get(0).getRight(), mCardList.get(0).getBottom());
        animator(newCardIndex, oldCardIndex);
    }

    /**
     * 卡片滑出
     *
     * @param left 是否向左滑出
     */
    private void out(boolean left) {
        CardIndex oldCardIndex;
        CardIndex newCardIndex;
        if (left) {
            /*
            向左滑出
             */
            oldCardIndex = new CardIndex(-mCardRight, mCardTop, 0, mCardBottom);
            newCardIndex = new CardIndex(mCardList.get(0).getLeft(), mCardList.get(0).getTop(), mCardList.get(0).getRight(), mCardList.get(0).getBottom());
        } else {
            /*
            向右滑出
             */
            oldCardIndex = new CardIndex(DensityUtil.getDisplayMetrics(mContext).widthPixels, mCardTop, DensityUtil.getDisplayMetrics(mContext).widthPixels + (mCardRight - mCardLeft), mCardBottom);
            newCardIndex = new CardIndex(mCardList.get(0).getLeft(), mCardList.get(0).getTop(), mCardList.get(0).getRight(), mCardList.get(0).getBottom());
        }

        animator(newCardIndex, oldCardIndex);
    }

    private void animator(CardIndex newCard, CardIndex oldCard) {

        ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(), newCard, oldCard);
        animator.setDuration(200);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mOnTouch = false;
                        CardIndex value = (CardIndex) animation.getAnimatedValue();
                        mCardList.get(0).layout(value.left, value.top, value.right, value.bottom);
                    }
                });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mRightOut || mLeftOut) {
                    removeTopCard();
                    if (mLeftOrRight != null) {
                        mLeftOrRight.leftOrRight(mLeftOut);
                    }
                }
                mOnTouch = true;
            }
        });
        animator.start();
    }


    /**
     * 移除顶部卡片(无动画)
     */
    public void removeTopCard() {
        if (!ListUtil.isEmpty(this.mCardList)) {
            removeView(this.mCardList.remove(0));
            if (mCardList.size() == mLoadSize) {
                if (mLoadMore != null) {
                    this.isLoadMore = true;
                    this.mLoadMore.load();
                    this.isLoadMore = false;
                    this.resetLayoutParams();
                }
            }

        }
    }

    /**
     * 移除顶部卡片（有动画）
     *
     * @param left 向左吗
     */
    public void removeTopCard(boolean left) {
        if (this.mOnTouch) {
            this.mLeftOut = left;
            this.mRightOut = !this.mLeftOut;
            this.getLayout();
            this.out(left);
        }
    }

    /**
     * 当剩余卡片等于size时，加载更多
     */
    public void setLoadSize(int size) {
        this.mLoadSize = size;
    }

    /**
     * 距离左右上下边距的边距（屏幕宽度的百分比）
     *
     * @param margin 屏幕宽度的百分比
     */
    public void setMargin(double margin) {
        this.margin = margin;
    }

    /**
     * 加载更多监听
     *
     * @param listener {@link LoadMore}
     */
    public void setLoadMoreListener(LoadMore listener) {
        this.mLoadMore = listener;
    }

    /**
     * 左右滑动监听
     *
     * @param listener {@link LeftOrRight}
     */
    public void setLeftOrRightListener(LeftOrRight listener) {
        this.mLeftOrRight = listener;
    }

    public interface LoadMore {
        void load();
    }

    public interface LeftOrRight {
        void leftOrRight(boolean left);
    }

}
