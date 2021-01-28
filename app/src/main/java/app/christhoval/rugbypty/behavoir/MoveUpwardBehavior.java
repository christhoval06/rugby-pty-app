package app.christhoval.rugbypty.behavoir;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by christhoval on 11/14/16.
 */

public class MoveUpwardBehavior extends CoordinatorLayout.Behavior<View> {

    private int distance;

    public MoveUpwardBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        //return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {

            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int viewBottomMargin = layoutParams.bottomMargin;
            child.animate().translationY(child.getHeight() + viewBottomMargin).setInterpolator(new LinearInterpolator()).start();

        } else if (dy < 0) {

            child.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();

        }
    }

    private void hide(View view) {
        view.setVisibility(View.GONE);// use animate.translateY(height); instead
    }

    private void show(View view) {
        view.setVisibility(View.VISIBLE);// use animate.translateY(-height); instead
    }
}

