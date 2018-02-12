package com.vitalityactive.va.wellnessdevices.linking.containers;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class PointsEarningMetricsContainer extends GenericRecyclerViewAdapter.ViewHolder<String> {

    public PointsEarningMetricsContainer(Context context,
                                         View itemView,
                                         final OnClickListener onClickListener,
                                         boolean partnerHasURL) {
        super(itemView);

        setClickableSpan(context, onClickListener, partnerHasURL);
    }

    @Override
    public void bindWith(String dataItem) {
    }

    private void setClickableSpan(Context context,
                                  final OnClickListener onClickListener, boolean partnerHasURL) {
        String learnMoreText = partnerHasURL ? context.getString(R.string.WDA_points_earning_metrics_learn_more_link_text_437) : "";

        final String text = String.format(context.getString(R.string.WDA_points_earning_metrics_content_436), learnMoreText);
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (onClickListener != null) {
                    onClickListener.onClick(textView);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        int startPosition = text.indexOf(context.getString(R.string.WDA_points_earning_metrics_learn_more_link_text_437));
        if (startPosition >= 0) {
            ss.setSpan(clickableSpan, startPosition, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        TextView textView = (TextView) itemView.findViewById(R.id.tv_wd_linking_points_description);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<String, PointsEarningMetricsContainer> {
        private Context context;
        private OnClickListener onClickListener;
        private boolean partnerHasURL;

        public Factory(Context context, OnClickListener onClickListener, boolean partnerHasURL) {
            this.context = context;
            this.onClickListener = onClickListener;
            this.partnerHasURL = partnerHasURL;
        }

        @Override
        public PointsEarningMetricsContainer createViewHolder(View itemView) {
            return new PointsEarningMetricsContainer(context, itemView, onClickListener, partnerHasURL);
        }
    }

}