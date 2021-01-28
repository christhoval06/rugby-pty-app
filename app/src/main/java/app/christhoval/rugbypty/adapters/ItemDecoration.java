package app.christhoval.rugbypty.adapters;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import app.christhoval.rugbypty.R;

/**
 * Created by christhoval on 07/27/16.
 */
public class ItemDecoration extends StickyRecyclerHeadersDecoration {
    private int dividerPadding;
    private Drawable mDivider;

    public ItemDecoration(StickyRecyclerHeadersAdapter adapter, Resources resources) {
        super(adapter);
        this.mDivider = new ColorDrawable(Color.parseColor("#4CB2B2B2"));
        this.dividerPadding = resources.getDimensionPixelOffset(R.dimen.divider_padding);
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + this.dividerPadding;
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int top = child.getBottom() + ((RecyclerView.LayoutParams) child.getLayoutParams()).bottomMargin;
            this.mDivider.setBounds(left, top, right, top + 1);
            this.mDivider.draw(c);
        }
    }
}
