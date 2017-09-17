package com.example.ubuntu.qc_scanner.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ubuntu.qc_scanner.R;
import com.example.ubuntu.qc_scanner.fragment.QRDataFragment;
import com.example.ubuntu.qc_scanner.mode.QRDataItem;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

import org.w3c.dom.Text;

public class CardStackAdapter extends StackAdapter<QRDataItem> {

    private static final String TAG = "CardStackAdapter";

    public CardStackAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindView(QRDataItem data, int position, CardStackView.ViewHolder holder) {
        if (holder instanceof ColorItemViewHolder) {
            ColorItemViewHolder h = (ColorItemViewHolder) holder;
            h.onBind(data, position);
        }
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view;
        view = getLayoutInflater().inflate(R.layout.list_card_item, parent, false);
        return new ColorItemViewHolder(view);

    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.list_card_item;
    }

    class ColorItemViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextNumber;
        TextView mTextTitle;
        TextView mTextGroupName;
        TextView mTextValleyValue;
        TextView mTextPeakValue;
        TextView mTextTotalValue;
        TextView mTextCaseName;
        TextView mTextCaseType;

        public ColorItemViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            //mTextTitle = (TextView) view.findViewById(R.id.text_list_card_case_number_value);
            mTextNumber = (TextView) view.findViewById(R.id.text_list_card_number);
            mTextGroupName = (TextView) view.findViewById(R.id.text_list_card_case_number_value);
            mTextValleyValue = (TextView) view.findViewById(R.id.text_list_card_valley_value);
            //mTextPeakValue = (TextView)view.findViewById(R.id.text_list_card_peak_value);
            mTextTotalValue = (TextView)view.findViewById(R.id.text_list_card_total_amount);
            mTextCaseName = (TextView)view.findViewById(R.id.text_list_card_case_name_value);
            mTextCaseType = (TextView)view.findViewById(R.id.text_list_card_case_type_value);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(QRDataItem data, int position) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), colors[position % colors.length]), PorterDuff.Mode.SRC_IN);
            QRDataItem item = data;
            Log.d(TAG, "item = " + item.toString());
            mTextGroupName.setText(String.valueOf(item.getGroupID()));
            mTextNumber.setText(item.getNumberID());
            //mTextTitle.setText(item.getmQRDataTime());
            mTextValleyValue.setText(item.getValleyValue());
            mTextTotalValue.setText(item.getTotalValue());
            mTextCaseName.setText(String.valueOf(item.getCaseName()));
            mTextCaseType.setText(String.valueOf(item.getCaseType()));
        }

        public  Integer[] colors = new Integer[]{
                R.color.color_1,
                R.color.color_2,
                R.color.color_3,
                R.color.color_4,
                R.color.color_5,
                R.color.color_6,
                R.color.color_7,
                R.color.color_8,
                R.color.color_9
        };
    }

}
