package app.christhoval.rugbypty.utilities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.ui.BadgeDrawable;

/**
 * Created by christhoval
 * Date 06/06/16.
 */
public class Utils {
    public static void openSocialLink(AppCompatActivity activity, String social) {
        String uri = "", url = "", pkg = "";
        int identifier, profile;
        if ((identifier = activity.getResources().getIdentifier(String.format("%s_uri", social), "string", activity.getPackageName())) > 0) {
            if ((profile = activity.getResources().getIdentifier(String.format("%s_profile_id", social), "string", activity.getPackageName())) > 0) {
                uri = String.format(activity.getString(identifier), activity.getString(profile));
            }
        }
        if ((identifier = activity.getResources().getIdentifier(String.format("%s_url", social), "string", activity.getPackageName())) > 0) {
            if ((profile = activity.getResources().getIdentifier(String.format("%s_username", social), "string", activity.getPackageName())) > 0) {
                url = String.format(activity.getString(identifier), activity.getString(profile));
            }
        }
        if ((identifier = activity.getResources().getIdentifier(String.format("%s_pkg", social), "string", activity.getPackageName())) > 0) {
            pkg = activity.getString(identifier);
        }

        if (!uri.isEmpty() || !url.isEmpty()) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (!pkg.isEmpty()) {
                try {
                    if (activity.getPackageManager().getPackageInfo(pkg, 0) != null) {
                        intent.setPackage(pkg);
                    }
                } catch (PackageManager.NameNotFoundException ignored) {
                }
            }
            if (!uri.isEmpty()) {
                intent.setData(Uri.parse(url));
            }
            if (!url.isEmpty()) {
                intent.setData(Uri.parse(url));
            }
        }

        if (!uri.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.isEmpty()) {
                if (!url.isEmpty()) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                }
            }
            activity.startActivity(intent);
        } else if (!url.isEmpty()) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    private static String getEmiailID(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    public static String formatDate(String date) {
        return formatDate(date, "dd/MM/yyyy");
    }

    public static String formatDate(String date, String format) {
        if (date != null) {
            SimpleDateFormat original = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()); //"yyyy-MM-dd HH:mm:ss", Locale.US);
            original.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat output = new SimpleDateFormat(format, Locale.getDefault());
            output.setTimeZone(TimeZone.getTimeZone("America/Panama"));
            try {
                Date d = original.parse(date);
                return output.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static boolean isDateGreatThanNow(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            Date date1 = sdf.parse(date);
            Date date2 = new Date();


            return date1.after(date2);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static long formatDate2MilliSeconds(String date) {
        if (date != null) {
            SimpleDateFormat original = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()); //"yyyy-MM-dd HH:mm:ss", Locale.US);
            try {
                Date d = original.parse(date);
                return d.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static int getColor(Context context, int id) {
        return ContextCompat.getColor(context, id);
    }

    private static String convertedToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte aData : data) {
            int halfOfByte = (aData >>> 4) & 0x0F;
            int twoHalfBytes = 0;
            do {
                if ((0 <= halfOfByte) && (halfOfByte <= 9)) {
                    buf.append((char) ('0' + halfOfByte));
                } else {
                    buf.append((char) ('a' + (halfOfByte - 10)));
                }
                halfOfByte = aData & 0x0F;
            }
            while (twoHalfBytes++ < 1);
        }
        return buf.toString();
    }

    public static String toMD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5;
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md5 = md.digest();
        return convertedToHex(md5);
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, @IdRes int layer, int count, @Nullable String formater) {

        BadgeDrawable badge;

        // Reusar drawable
        Drawable reuse = icon.findDrawableByLayerId(layer);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        if (formater != null)
            badge.setCount(count, formater);
        else
            badge.setCount(count);

        icon.mutate();
        icon.setDrawableByLayerId(layer, badge);
    }

    public static int dp(Context context, int inPixels) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, inPixels, context.getResources().getDisplayMetrics());
    }
}
