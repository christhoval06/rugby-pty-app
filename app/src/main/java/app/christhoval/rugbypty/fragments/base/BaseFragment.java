package app.christhoval.rugbypty.fragments.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.RugbyPTY;
import app.christhoval.rugbypty.activities.Main;
import app.christhoval.rugbypty.utilities.Constant;
import app.christhoval.rugbypty.utilities.Debug;
import app.christhoval.rugbypty.utilities.SharedPreferences;


/**
 * Created by christhoval
 * on 08/23/16.
 */
public class BaseFragment extends Fragment {

    private SharedPreferences preferences;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        RugbyPTY app = (RugbyPTY) getActivity().getApplication();
        preferences = app.getPreferences();
    }

    protected void updateTitle(String title) {
        AppCompatActivity mActivity = (AppCompatActivity) getActivity();
        if (mActivity != null) {
            mActivity.setTitle(title);
            if (mActivity.getSupportActionBar() != null)
                mActivity.getSupportActionBar().setTitle(title);
        }
    }

    protected void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }


    protected void hideToolBar() {
        Main main = (Main) getActivity();
        ActionBar bar = main.getSupportActionBar();
        if (null != bar) {
            bar.hide();
        }
    }

    protected void setupToolBarNavigationIcon(boolean enabled) {
        Main main = (Main) getActivity();
        ActionBar bar = main.getSupportActionBar();
        if (null != bar) {
            bar.setHomeButtonEnabled(enabled);
            bar.setDisplayHomeAsUpEnabled(enabled);
            bar.setDisplayShowHomeEnabled(enabled);
        }
    }

    public void setDrawerEnabled(boolean enabled) {
        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawerLayout.setDrawerLockMode(lockMode);
    }

    protected void showToolBar() {
        Main main = (Main) getActivity();
        ActionBar bar = main.getSupportActionBar();
        if (null != bar) {
            bar.show();
        }
    }

    protected void viewShake(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
    }

    protected Drawable getResDrawable(@DrawableRes int icon) {
        return ContextCompat.getDrawable(getContext(), icon);
    }

    protected Drawable getDrawable(@DrawableRes int icon) {
        return ContextCompat.getDrawable(getContext(), icon);
    }

    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
    }


    protected boolean isVisible(@IdRes int id) {
        if (null != getView()) {
            final View view = findViewById(id);
            if (null != view) {
                return view.getVisibility() == View.VISIBLE;
            }
        }
        return false;
    }

    protected void setVisibility(@IdRes int id, int visibility) {
        if (null != getView()) {
            final View view = findViewById(id);
            if (null != view) {
                view.setVisibility(visibility);
            }
        }
    }

    protected void toggleView(@IdRes int id) {
        if (null != getView()) {
            final View view = findViewById(id);
            if (null != view) {
                view.setVisibility(isVisible(id) ? View.GONE : View.VISIBLE);
            }
        }
    }

    protected void initViewState(@IdRes int id, int height, int duration) {
        if (null != getView()) {
            final View expandView = getView().findViewById(id);
            if (null != expandView) {
                expandView.setTag(R.string.original_height, height);

                if (null == expandView.getTag(R.string.expanded)) {
                    expandView.setTag(R.string.expanded, false);
                }

                boolean expanded = (boolean) expandView.getTag(R.string.expanded);

                AnimatorSet set = new AnimatorSet();
                if (expanded) {
                    expandView.setTag(R.string.expanded, true);
                    expandView.getLayoutParams().height = height;
                    set.play(ObjectAnimator.ofFloat(expandView, View.ALPHA, 0.0f, 1.0f));
                    expandView.requestLayout();
                } else {
                    expandView.setTag(R.string.expanded, false);
                    set.play(ObjectAnimator.ofFloat(expandView, View.ALPHA, 1.0f, 0.0f));
                    expandView.getLayoutParams().height = 0;
                    expandView.requestLayout();
                }

                if (duration > 0)
                    set.setDuration(duration);

                set.start();
            }
        }
    }

    protected void toggleAnimationView(@IdRes int id, @Nullable int height) {
        Debug.i("expandView");

        if (null != getView()) {
            final View expandView = getView().findViewById(id);

            if (null != expandView) {
                if (null == expandView.getTag(R.string.expanded)) {
                    expandView.setTag(R.string.expanded, false);
                }

                height = height > 0 ? height : (int) expandView.getTag(R.string.original_height);
                boolean expanded = (boolean) expandView.getTag(R.string.expanded);

                Debug.i("expanded: " + String.valueOf(expanded));

                ValueAnimator expandAnimator;
                AnimatorSet set = new AnimatorSet();
                if (expanded) {
                    expandView.setTag(R.string.expanded, false);
                    expandAnimator = ValueAnimator.ofInt(height, 0);
                    set.play(ObjectAnimator.ofFloat(expandView, View.ALPHA, 1.0f, 0.0f));
                } else {
                    expandView.setTag(R.string.expanded, true);
                    expandAnimator = ValueAnimator.ofInt(0, height);
                    set.play(ObjectAnimator.ofFloat(expandView, View.ALPHA, 0.0f, 1.0f));
                }
                expandAnimator.setDuration(250);
                expandAnimator.setInterpolator(new LinearInterpolator());
                expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        expandView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                        expandView.requestLayout();
                    }
                });
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
                set.start();
                expandAnimator.start();
            }
        }
    }

    protected View findViewById(@IdRes int id) {
        if (null != getView()) {
            return getView().findViewById(id);
        }
        return null;
    }
}
