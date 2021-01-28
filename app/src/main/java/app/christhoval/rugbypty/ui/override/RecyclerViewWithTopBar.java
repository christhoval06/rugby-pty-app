package app.christhoval.rugbypty.ui.override;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import app.christhoval.rugbypty.utilities.Debug;

public class RecyclerViewWithTopBar extends RecyclerView {

    private View mReturningView;
    private static final int STATE_ONSCREEN = 0;
    private static final int STATE_OFFSCREEN = 1;
    private static final int STATE_RETURNING = 2;
    private int mState = STATE_ONSCREEN;
    private int mMinRawY = 0;
    private int mReturningViewHeight;
    private int mGravity = Gravity.BOTTOM;

    public RecyclerViewWithTopBar(Context context) {
        super(context);
        init();
    }

    public RecyclerViewWithTopBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecyclerViewWithTopBar(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
    }

    public void setTopBar(View view) {
        mReturningView = view;

        try {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mReturningView.getLayoutParams();
            mGravity = params.gravity;
        } catch (ClassCastException e) {
            throw new RuntimeException("The return view need to be put in a FrameLayout");
        }

        measureView(mReturningView);
        mReturningViewHeight = mReturningView.getMeasuredHeight();
        addOnScrollListener(new RecyclerScrollListener());
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private class RecyclerScrollListener extends OnScrollListener {
        private int mScrolledY;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            if (mGravity == Gravity.BOTTOM)
                mScrolledY += dy;
            else if (mGravity == Gravity.TOP)
                mScrolledY -= dy;

            if (mReturningView == null)
                return;

            int translationY = 0;
            int rawY = mScrolledY;

            switch (mState) {
                case STATE_OFFSCREEN:
                    if (mGravity == Gravity.BOTTOM) {
                        if (rawY >= mMinRawY) {
                            mMinRawY = rawY;
                        } else {
                            mState = STATE_RETURNING;
                        }
                    } else if (mGravity == Gravity.TOP) {
                        if (rawY <= mMinRawY) {
                            mMinRawY = rawY;
                        } else {
                            mState = STATE_RETURNING;
                        }
                    }

                    translationY = rawY;
                    break;

                case STATE_ONSCREEN:
                    if (mGravity == Gravity.BOTTOM) {

                        if (rawY > mReturningViewHeight) {
                            mState = STATE_OFFSCREEN;
                            mMinRawY = rawY;
                        }
                    } else if (mGravity == Gravity.TOP) {

                        if (rawY < -mReturningViewHeight) {
                            mState = STATE_OFFSCREEN;
                            mMinRawY = rawY;
                        }
                    }
                    translationY = rawY;
                    break;

                case STATE_RETURNING:
                    if (mGravity == Gravity.BOTTOM) {
                        translationY = (rawY - mMinRawY) + mReturningViewHeight;

                        if (translationY < 0) {
                            translationY = 0;
                            mMinRawY = rawY + mReturningViewHeight;
                        }

                        if (rawY == 0) {
                            mState = STATE_ONSCREEN;
                            translationY = 0;
                        }

                        if (translationY > mReturningViewHeight) {
                            mState = STATE_OFFSCREEN;
                            mMinRawY = rawY;
                        }
                    } else if (mGravity == Gravity.TOP) {
                        translationY = (rawY + Math.abs(mMinRawY)) - mReturningViewHeight;

                        if (translationY > 0) {
                            translationY = 0;
                            mMinRawY = rawY - mReturningViewHeight;
                        }

                        if (rawY == 0) {
                            mState = STATE_ONSCREEN;
                            translationY = 0;
                        }

                        if (translationY < -mReturningViewHeight) {
                            mState = STATE_OFFSCREEN;
                            mMinRawY = rawY;
                        }
                    }
                    break;
            }

            ValueAnimator animator = ValueAnimator.ofFloat(mReturningView.getTranslationY(), translationY).setDuration(0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation) {
                    float translationY = (float) animation.getAnimatedValue();
                    mReturningView.setTranslationY(translationY);
                    final FrameLayout.LayoutParams clp = (FrameLayout.LayoutParams) getLayoutParams();
                    if (clp.topMargin > 0) {
                        if (Math.abs(translationY) <= mReturningViewHeight) {
                            ValueAnimator rc = ValueAnimator.ofInt(clp.topMargin, mReturningViewHeight + (int) translationY).setDuration(0);
                            rc.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    int margin = (int) valueAnimator.getAnimatedValue();
                                    clp.topMargin = margin;
                                    setLayoutParams(clp);
                                }
                            });
                            rc.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    clearAnimation();
                                }
                            });
                            rc.start();
                        }
                    }

                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mReturningView.clearAnimation();
                }
            });
            animator.start();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    break;

            }
        }
    }
}
