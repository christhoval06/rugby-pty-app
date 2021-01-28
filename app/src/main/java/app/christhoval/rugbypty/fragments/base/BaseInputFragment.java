package app.christhoval.rugbypty.fragments.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import app.christhoval.rugbypty.utilities.Debug;
import app.christhoval.rugbypty.utilities.SharedPreferences;


/**
 * Created by christhoval
 * on 08/23/16.
 */
public class BaseInputFragment extends BaseFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
                            if (textInputLayout.getError() == null)
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
                        if (textInputLayout.getError() == null)
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
        View view = getActivity().getWindow().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
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

    protected void textInputLayoutFocus(TextInputLayout textInputLayout) {
        if (null != textInputLayout.getEditText()) {
            textInputLayout.getEditText().clearFocus();
            textInputLayout.getEditText().requestFocus();
        }
    }
}
