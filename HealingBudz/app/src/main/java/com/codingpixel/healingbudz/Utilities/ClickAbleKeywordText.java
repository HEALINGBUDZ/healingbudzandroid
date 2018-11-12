package com.codingpixel.healingbudz.Utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codingpixel.healingbudz.activity.Wall.WallPostDetailActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.Post;
import com.codingpixel.healingbudz.network.BudzFeedModel.MentionTagJsonModel;

import java.util.List;

import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;

public class ClickAbleKeywordText {
    static KeywordClickDialog keywordClickDialog;
    static String color;

    public static void MakeKeywordClickableText(final Context context, String str, final TextView tv) {
        tv.setText("");
        if (str != null && str.length() > 0) {
            String[] devFull;
            //Strain #f4c42f Budz Adz #932a88 Q & A #0081ca Buzz #7CC244 Default #6d96ad

            color = "#6d96ad";
            devFull = str.split("<b ><font class='keyword_class' color=#6d96ad>");
            if (str.contains("'keyword_class'")) {

                if (str.contains("#f4c42f")) {
                    color = "#f4c42f";
                    devFull = str.split("<b ><font class='keyword_class' color=#f4c42f>");
                } else if (str.contains("#932a88")) {
                    color = "#932a88";
                    devFull = str.split("<b ><font class='keyword_class' color=#932a88>");
                } else if (str.contains("#0081ca")) {
                    color = "#0081ca";
                    devFull = str.split("<b ><font class='keyword_class' color=#0081ca>");
                } else if (str.contains("#7CC244")) {
                    color = "#7CC244";
                    devFull = str.split("<b ><font class='keyword_class' color=#7CC244>");
                } else {
                    color = "#6d96ad";
                    devFull = str.split("<b ><font class='keyword_class' color=#6d96ad>");
                }
            } else if (str.contains("\"keyword_class\"")) {
                if (str.contains("#f4c42f")) {
                    color = "#f4c42f";
                    devFull = str.split("<b ><font class=\"keyword_class\" color=#f4c42f>");
                } else if (str.contains("#932a88")) {
                    color = "#932a88";
                    devFull = str.split("<b ><font class=\"keyword_class\" color=#932a88>");
                } else if (str.contains("#0081ca")) {
                    color = "#0081ca";
                    devFull = str.split("<b ><font class=\"keyword_class\" color=#0081ca>");
                } else if (str.contains("#7CC244")) {
                    color = "#7CC244";
                    devFull = str.split("<b ><font class=\"keyword_class\" color=#7CC244>");
                } else {
                    color = "#6d96ad";
                    devFull = str.split("<b ><font class=\"keyword_class\" color=#6d96ad>");
                }

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tv.append(Html.fromHtml(devFull[0], Html.FROM_HTML_MODE_COMPACT));
            } else {
                tv.append(Html.fromHtml(devFull[0]));
            }
            SpannableString[] link = new SpannableString[devFull.length - 1];
            ClickableSpan[] cs = new ClickableSpan[devFull.length - 1];
            String linkWord;
            String[] devDevFull = new String[2];

            for (int i = 1; i < devFull.length; i++) {
                devDevFull = devFull[i].split("</font></b>");
                link[i - 1] = new SpannableString(devDevFull[0] + " ");
                linkWord = devDevFull[0] + " ";
                final String a = linkWord;
                cs[i - 1] = new ClickableSpan() {
                    private String w = a;

                    @Override
                    public void onClick(View widget) {
                        tv.setClickable(false);
                        if (keywordClickDialog != null && keywordClickDialog.dialog.isShowing()) {
//                            keywordClickDialog.dialog.dismiss();
                            Log.d("onClick: ", "Second Clicked");
                        } else {
                            keywordClickDialog = new KeywordClickDialog(w, context);
                        }
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                tv.setClickable(true);
                            }
                        }, 500);
                    }

                    @Override
                    public void updateDrawState(TextPaint tp) {
                        tp.bgColor = Color.TRANSPARENT;
                        tp.linkColor = Color.parseColor(color);
                        tp.setColor(Color.parseColor(color));
                    }
                };
                link[i - 1].setSpan(cs[i - 1], 0, linkWord.length(), 0);
                tv.append(link[i - 1]);
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Log.d("text == ", devDevFull[1]);
                        tv.append(Html.fromHtml(devDevFull[1], Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        Log.d("text == ", devDevFull[1]);
                        tv.append(Html.fromHtml(devDevFull[1]));
                    }
                } catch (Exception e) {
                }
            }
            makeLinksFocusable(tv);
        }
    }

    public static void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    public static TextView createLink(final TextView targetTextView, String completeString,
                                      MentionTagJsonModel[] arrayData) throws IndexOutOfBoundsException {

        SpannableString spannableString = new SpannableString(completeString);

        // make sure the String is exist, if it doesn't exist
        // it will throw IndexOutOfBoundException
        for (final MentionTagJsonModel temp : arrayData) {
            if (temp.getTrigger().equalsIgnoreCase("@")) {
                String tt = temp.getTrigger() + temp.getValue();
                if (completeString.contains(tt)) {
                    int startPosition = completeString.indexOf(tt);
                    int endPosition = completeString.lastIndexOf(tt) + (tt).length();

                    spannableString.setSpan(new ClickableSpan() {
                                                @Override
                                                public void updateDrawState(TextPaint ds) {
                                                    super.updateDrawState(ds);
                                                    int linkColor = Color.parseColor("#7CC245");
                                                    ds.setColor(linkColor);
                                                    ds.setUnderlineText(false);
                                                }

                                                @Override
                                                public void onClick(View widget) {
                                                    if (temp.getType().equalsIgnoreCase("budz")) {
                                                        Intent budzmap_intetn = new Intent(targetTextView.getContext(), BudzMapDetailsActivity.class);
                                                        budzmap_intetn.putExtra("budzmap_id", Integer.parseInt(temp.getId()));
                                                        targetTextView.getContext().startActivity(budzmap_intetn);
                                                    } else {
                                                        GoToProfile(targetTextView.getContext(), Integer.parseInt(temp.getId()));
                                                    }
                                                }
                                            }, startPosition, endPosition,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            } else if (temp.getTrigger().equalsIgnoreCase("!")) {
                String tt = temp.getValue();
                int startPosition = completeString.indexOf(tt);
                int endPosition = completeString.lastIndexOf(temp.getTrigger() + temp.getValue()) + (temp.getTrigger() + temp.getValue()).length();

                spannableString.setSpan(new ClickableSpan() {
                                            @Override
                                            public void updateDrawState(TextPaint ds) {
                                                super.updateDrawState(ds);
                                                int linkColor = Color.parseColor("#7CC245");
                                                ds.setColor(linkColor);
                                                ds.setUnderlineText(false);
                                            }

                                            @Override
                                            public void onClick(View widget) {
                                                if (temp.getType().equalsIgnoreCase("budz")) {
                                                    Intent budzmap_intetn = new Intent(targetTextView.getContext(), BudzMapDetailsActivity.class);
                                                    budzmap_intetn.putExtra("budzmap_id", Integer.parseInt(temp.getId()));
                                                    targetTextView.getContext().startActivity(budzmap_intetn);
                                                } else {
                                                    GoToProfile(targetTextView.getContext(), Integer.parseInt(temp.getId()));
                                                }
                                            }
                                        }, startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else if (temp.getTrigger().equalsIgnoreCase("#")) {
                String tt = temp.getTrigger() + temp.getValue();
                if (completeString.toLowerCase().contains(tt.toLowerCase())) {
                    int startPosition = completeString.indexOf(tt);
                    int endPosition = completeString.lastIndexOf(tt) + (tt).length();

                    spannableString.setSpan(new ClickableSpan() {
                                                @Override
                                                public void updateDrawState(TextPaint ds) {
                                                    super.updateDrawState(ds);
                                                    int linkColor = Color.parseColor("#7CC245");
                                                    ds.setColor(linkColor);
                                                    ds.setUnderlineText(false);
                                                }

                                                @Override
                                                public void onClick(View widget) {
                                                    new KeywordClickDialog(temp.getValue(), widget.getContext());
                                                }
                                            }, startPosition, endPosition,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                // new KeywordClickDialog(extraJson.getValue(), getContext());
            }

        }
        String[] arrayT = completeString.trim().split(" ");
        for (final String temp : Splash.keywordList) {

//
            if (completeString.toLowerCase().contains("#" + temp.toLowerCase())) {
                int startPosition = completeString.toLowerCase().indexOf("#" + temp.toLowerCase());
                int endPosition = completeString.toLowerCase().lastIndexOf("#" + temp.toLowerCase()) + ("#" + temp.toLowerCase()).length();

                spannableString.setSpan(new ClickableSpan() {
                                            @Override
                                            public void updateDrawState(TextPaint ds) {
                                                super.updateDrawState(ds);
                                                int linkColor = Color.parseColor("#7CC245");
                                                ds.setColor(linkColor);
                                                ds.setUnderlineText(false);
                                            }

                                            @Override
                                            public void onClick(View widget) {
                                                new KeywordClickDialog(temp, widget.getContext());
                                            }
                                        }, startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            for (String anArrayT : arrayT) {
                if (anArrayT.equalsIgnoreCase(temp)) {
                    int startPosition = completeString.indexOf(anArrayT);
                    int endPosition = completeString.lastIndexOf(anArrayT) + (anArrayT).length();

                    spannableString.setSpan(new ClickableSpan() {
                                                @Override
                                                public void updateDrawState(TextPaint ds) {
                                                    super.updateDrawState(ds);
                                                    int linkColor = Color.parseColor("#7CC245");
                                                    ds.setColor(linkColor);
                                                    ds.setUnderlineText(false);
                                                }

                                                @Override
                                                public void onClick(View widget) {
                                                    new KeywordClickDialog(temp, widget.getContext());
                                                }
                                            }, startPosition, endPosition,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }


        }
        List<String> urlsArray = Utility.extractURL(completeString);
        if (urlsArray != null) {
            for (final String anArrayT : urlsArray) {
                if (anArrayT.length() > 8 && (anArrayT.contains(".com") || anArrayT.contains(".be") || anArrayT.contains("http") || anArrayT.contains("https")))  {
                    if (completeString.contains(anArrayT)) {
                        int startPosition = completeString.indexOf(anArrayT);
                        int endPosition = completeString.lastIndexOf(anArrayT) + (anArrayT).length();

                        spannableString.setSpan(new ClickableSpan() {
                                                    @Override
                                                    public void updateDrawState(TextPaint ds) {
                                                        super.updateDrawState(ds);
                                                        int linkColor = Color.parseColor("#808080");
                                                        ds.setColor(linkColor);
                                                        ds.setUnderlineText(false);
                                                    }

                                                    @Override
                                                    public void onClick(View widget) {
                                                        if (anArrayT.contains("youtube.com") || anArrayT.contains("youtu.be")) {
                                                            Intent intent = new Intent(targetTextView.getContext(), MediPreview.class);
                                                            intent.putExtra("path", anArrayT);
                                                            intent.putExtra("isvideo", true);
                                                            intent.putExtra("isvideoyoutube", true);
                                                            targetTextView.getContext().startActivity(intent);
                                                        } else {
                                                            Utility.launchWebUrl(targetTextView.getContext(), anArrayT);
                                                        }
                                                    }
                                                }, startPosition, endPosition,
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        URLSpan[] urls = spannableString.getSpans(0, completeString.trim().length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(spannableString, span);
        }
        targetTextView.setText(spannableString);
        targetTextView.setMovementMethod(LinkMovementMethod.getInstance());
        targetTextView.setLinkTextColor(Color.parseColor("#808080"));
        return targetTextView;
    }

    public static TextView createLink(final TextView targetTextView, String completeString,
                                      MentionTagJsonModel[] arrayData, final Post post) throws IndexOutOfBoundsException {

        SpannableString spannableString = new SpannableString(completeString);

        // make sure the String is exist, if it doesn't exist
        // it will throw IndexOutOfBoundException
        for (final MentionTagJsonModel temp : arrayData) {

            if (temp.getTrigger().equalsIgnoreCase("@")) {
                String tt = temp.getTrigger() + temp.getValue();
                if (completeString.contains(tt)) {
                    int startPosition = completeString.indexOf(tt);
                    int endPosition = completeString.lastIndexOf(tt) + (tt).length();

                    spannableString.setSpan(new ClickableSpan() {
                                                @Override
                                                public void updateDrawState(TextPaint ds) {
                                                    super.updateDrawState(ds);
                                                    int linkColor = Color.parseColor("#7CC245");
                                                    ds.setColor(linkColor);
                                                    ds.setUnderlineText(false);
                                                }

                                                @Override
                                                public void onClick(View widget) {
                                                    if (temp.getType().equalsIgnoreCase("budz")) {
                                                        Intent budzmap_intetn = new Intent(targetTextView.getContext(), BudzMapDetailsActivity.class);
                                                        budzmap_intetn.putExtra("budzmap_id", Integer.parseInt(temp.getId()));
                                                        targetTextView.getContext().startActivity(budzmap_intetn);
                                                    } else {
                                                        GoToProfile(targetTextView.getContext(), Integer.parseInt(temp.getId()));
                                                    }
                                                }
                                            }, startPosition, endPosition,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            } else if (temp.getTrigger().equalsIgnoreCase("!")) {
                String tt = temp.getValue();
                int startPosition = completeString.indexOf(tt);
                int endPosition = completeString.lastIndexOf(temp.getValue()) + (temp.getValue()).length();

                spannableString.setSpan(new ClickableSpan() {
                                            @Override
                                            public void updateDrawState(TextPaint ds) {
                                                super.updateDrawState(ds);
                                                int linkColor = Color.parseColor("#6d96ad");
                                                ds.setColor(linkColor);
                                                ds.setUnderlineText(false);
                                            }

                                            @Override
                                            public void onClick(View widget) {
                                                Bundle b = new Bundle();
                                                b.putSerializable(Constants.POST_EXTRA, post);
                                                Utility.launchActivityForResult((AppCompatActivity) widget.getContext(), WallPostDetailActivity.class, b, Flags.ACTIVITIES_COMMUNICATION_FLAG);

                                            }
                                        }, startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else if (temp.getTrigger().equalsIgnoreCase("#")) {
                String tt = temp.getTrigger() + temp.getValue();
                if (completeString.toLowerCase().contains(tt.toLowerCase())) {
                    int startPosition = completeString.indexOf(tt);
                    int endPosition = completeString.lastIndexOf(tt) + (tt).length();

                    spannableString.setSpan(new ClickableSpan() {
                                                @Override
                                                public void updateDrawState(TextPaint ds) {
                                                    super.updateDrawState(ds);
                                                    int linkColor = Color.parseColor("#7CC245");
                                                    ds.setColor(linkColor);
                                                    ds.setUnderlineText(false);
                                                }

                                                @Override
                                                public void onClick(View widget) {
                                                    new KeywordClickDialog(temp.getValue(), widget.getContext());
                                                }
                                            }, startPosition, endPosition,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                // new KeywordClickDialog(extraJson.getValue(), getContext());
            }

        }
        for (final String temp : Splash.keywordList) {


            if (completeString.toLowerCase().contains("#" + temp.toLowerCase())) {
                int startPosition = completeString.toLowerCase().indexOf("#" + temp.toLowerCase());
                int endPosition = completeString.toLowerCase().lastIndexOf("#" + temp.toLowerCase()) + ("#" + temp.toLowerCase()).length();

                spannableString.setSpan(new ClickableSpan() {
                                            @Override
                                            public void updateDrawState(TextPaint ds) {
                                                super.updateDrawState(ds);
                                                int linkColor = Color.parseColor("#7CC245");
                                                ds.setColor(linkColor);
                                                ds.setUnderlineText(false);
                                            }

                                            @Override
                                            public void onClick(View widget) {
                                                new KeywordClickDialog(temp, widget.getContext());
                                            }
                                        }, startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            String[] arrayT = completeString.trim().split(" ");
            for (String anArrayT : arrayT) {
                if (anArrayT.equalsIgnoreCase(temp)) {
                    int startPosition = completeString.indexOf(anArrayT);
                    int endPosition = completeString.lastIndexOf(anArrayT) + (anArrayT).length();

                    spannableString.setSpan(new ClickableSpan() {
                                                @Override
                                                public void updateDrawState(TextPaint ds) {
                                                    super.updateDrawState(ds);
                                                    int linkColor = Color.parseColor("#7CC245");
                                                    ds.setColor(linkColor);
                                                    ds.setUnderlineText(false);
                                                }

                                                @Override
                                                public void onClick(View widget) {
                                                    new KeywordClickDialog(temp, widget.getContext());
                                                }
                                            }, startPosition, endPosition,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }


        }
        List<String> urlsArray = Utility.extractURL(completeString);
        if (urlsArray != null) {
            for (final String anArrayT : urlsArray) {
                if (anArrayT.length() > 8 && (anArrayT.contains(".com") || anArrayT.contains(".be") || anArrayT.contains("http") || anArrayT.contains("https"))) {
                    if (completeString.contains(anArrayT)) {
                        int startPosition = completeString.indexOf(anArrayT);
                        int endPosition = completeString.lastIndexOf(anArrayT) + (anArrayT).length();

                        spannableString.setSpan(new ClickableSpan() {
                                                    @Override
                                                    public void updateDrawState(TextPaint ds) {
                                                        super.updateDrawState(ds);
                                                        int linkColor = Color.parseColor("#808080");
                                                        ds.setColor(linkColor);
                                                        ds.setUnderlineText(false);
                                                    }

                                                    @Override
                                                    public void onClick(View widget) {
                                                        if (anArrayT.contains("youtube.com") || anArrayT.contains("youtu.be")) {
                                                            Intent intent = new Intent(targetTextView.getContext(), MediPreview.class);
                                                            intent.putExtra("path", anArrayT);
                                                            intent.putExtra("isvideo", true);
                                                            intent.putExtra("isvideoyoutube", true);
                                                            targetTextView.getContext().startActivity(intent);
                                                        } else {
                                                            Utility.launchWebUrl(targetTextView.getContext(), anArrayT);
                                                        }
                                                    }
                                                }, startPosition, endPosition,
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        URLSpan[] urls = spannableString.getSpans(0, completeString.trim().length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(spannableString, span);
        }
        targetTextView.setText(spannableString);
        targetTextView.setMovementMethod(LinkMovementMethod.getInstance());
        targetTextView.setLinkTextColor(Color.parseColor("#808080"));
        return targetTextView;
    }

    public static TextView createLink(final TextView targetTextView, String completeString) throws IndexOutOfBoundsException {

        SpannableString spannableString = new SpannableString(completeString);

        // make sure the String is exist, if it doesn't exist
        // it will throw IndexOutOfBoundException

        for (final String temp : Splash.keywordList) {


            if (completeString.toLowerCase().contains("#" + temp.toLowerCase())) {
                int startPosition = completeString.toLowerCase().indexOf("#" + temp.toLowerCase());
                int endPosition = completeString.toLowerCase().lastIndexOf("#" + temp.toLowerCase()) + ("#" + temp.toLowerCase()).length();

                spannableString.setSpan(new ClickableSpan() {
                                            @Override
                                            public void updateDrawState(TextPaint ds) {
                                                super.updateDrawState(ds);
                                                int linkColor = Color.parseColor("#7CC245");
                                                ds.setColor(linkColor);
                                                ds.setUnderlineText(false);
                                            }

                                            @Override
                                            public void onClick(View widget) {
                                                new KeywordClickDialog(temp, widget.getContext());
                                            }
                                        }, startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            String[] arrayT = completeString.trim().split(" ");
            for (String anArrayT : arrayT) {
                if (anArrayT.equalsIgnoreCase(temp)) {
                    int startPosition = completeString.indexOf(anArrayT);
                    int endPosition = completeString.lastIndexOf(anArrayT) + (anArrayT).length();

                    spannableString.setSpan(new ClickableSpan() {
                                                @Override
                                                public void updateDrawState(TextPaint ds) {
                                                    super.updateDrawState(ds);
                                                    int linkColor = Color.parseColor("#7CC245");
                                                    ds.setColor(linkColor);
                                                    ds.setUnderlineText(false);
                                                }

                                                @Override
                                                public void onClick(View widget) {
                                                    new KeywordClickDialog(temp, widget.getContext());
                                                }
                                            }, startPosition, endPosition,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }


        }
        List<String> urlsArray = Utility.extractURL(completeString);
        if (urlsArray != null) {
            for (final String anArrayT : urlsArray) {
                if (anArrayT.length() > 8 && (anArrayT.contains(".com") || anArrayT.contains(".be") || anArrayT.contains("http") || anArrayT.contains("https"))) {
                    if (completeString.contains(anArrayT)) {
                        int startPosition = completeString.indexOf(anArrayT);
                        int endPosition = completeString.lastIndexOf(anArrayT) + (anArrayT).length();

                        spannableString.setSpan(new ClickableSpan() {
                                                    @Override
                                                    public void updateDrawState(TextPaint ds) {
                                                        super.updateDrawState(ds);
                                                        int linkColor = Color.parseColor("#808080");
                                                        ds.setColor(linkColor);
                                                        ds.setUnderlineText(false);
                                                    }

                                                    @Override
                                                    public void onClick(View widget) {
                                                        if (anArrayT.contains("youtube.com") || anArrayT.contains("youtu.be")) {
                                                            Intent intent = new Intent(targetTextView.getContext(), MediPreview.class);
                                                            intent.putExtra("path", anArrayT);
                                                            intent.putExtra("isvideo", true);
                                                            intent.putExtra("isvideoyoutube", true);
                                                            targetTextView.getContext().startActivity(intent);
                                                        } else {
                                                            Utility.launchWebUrl(targetTextView.getContext(), anArrayT);
                                                        }
                                                    }
                                                }, startPosition, endPosition,
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        URLSpan[] urls = spannableString.getSpans(0, completeString.trim().length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(spannableString, span);
        }
        targetTextView.setText(spannableString);
        targetTextView.setMovementMethod(LinkMovementMethod.getInstance());
        targetTextView.setLinkTextColor(Color.parseColor("#808080"));
        return targetTextView;
    }

    public static void makeLinkClickable(SpannableString strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                int linkColor = Color.parseColor("#808080");
                ds.setColor(linkColor);
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                if (span.getURL().contains("youtube.com") || span.getURL().contains("youtu.be")) {
                    Intent intent = new Intent(widget.getContext(), MediPreview.class);
                    intent.putExtra("path", span.getURL());
                    intent.putExtra("isvideo", true);
                    intent.putExtra("isvideoyoutube", true);
                    widget.getContext().startActivity(intent);
                } else {
                    Utility.launchWebUrl(widget.getContext(), span.getURL());
                }
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
//        strBuilder.removeSpan(span);
    }

}
