package cn.edu.gdmec.android.BoXueGu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.edu.gdmec.android.BoXueGu.R;
import cn.edu.gdmec.android.BoXueGu.activity.ExercisesDetailActivity;
import cn.edu.gdmec.android.BoXueGu.bean.ExercisesBean;

/**
 * Created by ASUS on 2017/12/27.
 */

public class ExercisesAdapter extends BaseAdapter {
    private Context mContext;
    private List<ExercisesBean> ebl;
    public ExercisesAdapter(Context context){
        this.mContext = context;
    }
    public void setData(List<ExercisesBean> ebl){
        this.ebl = ebl;
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return ebl == null ? 0 : ebl.size();
    }

    @Override
    public ExercisesBean getItem(int position){
        return ebl == null ? null :ebl.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        final ViewHolder vh;
        if (converView == null) {
            vh = new ViewHolder();
            converView = LayoutInflater.from(mContext).inflate(
                    R.layout.exercises_list_item, null);
            vh.title = (TextView) converView.findViewById(R.id.tv_title);
            vh.content = (TextView) converView.findViewById(R.id.tv_content);
            vh.order = (TextView) converView.findViewById(R.id.tv_order);
            converView.setTag(vh);

        } else {
            vh = (ViewHolder) converView.getTag();
        }

        final ExercisesBean bean = getItem(position);
        if (bean != null){
            vh.order.setText(position + 1 + "");
            vh.title.setText(bean.title);
            vh.content.setText(bean.content);
            vh.order.setBackgroundResource(bean.background);
        }
        converView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (bean == null)
                    return;
                Intent intent = new Intent(mContext,
                        ExercisesDetailActivity.class);
                intent.putExtra("id",bean.id);
                intent.putExtra("title",bean.title);
                mContext.startActivity(intent);
            }
        });
        return converView;
    }

    class ViewHolder{
        public TextView title,content;
        public TextView order;
    }
}
