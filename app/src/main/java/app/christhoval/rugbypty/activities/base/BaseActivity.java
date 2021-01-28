package app.christhoval.rugbypty.activities.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.RugbyPTY;
import app.christhoval.rugbypty.utilities.Debug;
import app.christhoval.rugbypty.utilities.SharedPreferences;

/**
 * Created by christhoval
 * on 09/26/16.
 */
public class BaseActivity extends AppCompatActivity {

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        RugbyPTY rugbyPTY = (RugbyPTY) getApplication();

        preferences = rugbyPTY.getPreferences();

        super.onCreate(savedInstanceState);
    }

    protected SharedPreferences getPreferences() {
        return preferences;
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void changeDrawable(TextInputLayout textInputLayout, Drawable drawable) {
        if (null != textInputLayout.getEditText()) {
            textInputLayout.getEditText().setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
    }

    protected void addWatch(final TextInputLayout textInputLayout, final String error, @Nullable final Drawable default_drawable, @Nullable final Drawable focus_icon, @Nullable final Drawable error_icon) {
        if (null != textInputLayout.getEditText()) {

            textInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        if (textInputLayout.isErrorEnabled()) {
                            textInputLayout.setError(error);
                            changeDrawable(textInputLayout, error_icon);
                        } else
                            changeDrawable(textInputLayout, focus_icon);
                    } else {
                        changeDrawable(textInputLayout, default_drawable);
                    }
                }
            });

            textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                public void afterTextChanged(Editable edt) {
                    if (edt.length() < 1) {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError(error);
                        changeDrawable(textInputLayout, error_icon);
                    }
                    if (edt.length() > 0) {
                        textInputLayout.setErrorEnabled(false);
                        textInputLayout.setError(null);
                        changeDrawable(textInputLayout, focus_icon);
                    }
                }
            });
        }
    }

    protected void hideKeyboard() {
        View view = getWindow().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected String textInputLayoutGetText(TextInputLayout textInputLayout) {
        if (null != textInputLayout.getEditText()) {
            return textInputLayout.getEditText().getText().toString().trim();
        }
        return "";
    }

    protected void textInputLayoutClear(TextInputLayout textInputLayout) {
        if (null != textInputLayout.getEditText()) {
            textInputLayout.getEditText().setText("");
            textInputLayout.setErrorEnabled(false);
            textInputLayout.getEditText().clearFocus();
        }
    }

    protected void textInputLayoutShake(TextInputLayout textInputLayout) {
        if (null != textInputLayout.getEditText()) {
            viewShake(textInputLayout.getEditText());
        }
    }

    protected void viewShake(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    protected void textInputLayoutFocus(TextInputLayout textInputLayout) {
        if (null != textInputLayout.getEditText()) {
            textInputLayout.getEditText().clearFocus();
            textInputLayout.getEditText().requestFocus();
        }
    }

    protected Drawable getResDrawable(@DrawableRes int icon) {
        return ContextCompat.getDrawable(this, icon);
    }

    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return ContextCompat.getColor(this, resId);
    }

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
    }

    protected void toggleView(@IdRes int id) {
        final View view = findViewById(id);
        if (null != view) {
            view.setVisibility(view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }
    }

    protected void initViewState(@IdRes int id, int height, int duration) {
        final View expandView = findViewById(id);
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

    protected void toggleAnimationView(@IdRes int id, @Nullable int height) {
        Debug.i("expandView");

        final View expandView = findViewById(id);

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

    protected Fragment getFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;
    }
}
