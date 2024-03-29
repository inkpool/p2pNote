package com.crowley.p2pnote.ui;

import java.util.List;

import com.crowley.p2pnote.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class HorizontalScrollViewAdapter extends SimpleAdapter{
	private Context mContext;  
    private LayoutInflater mInflater;  
    private List<Integer> mDatas; 
    private List<String> mDatas2;
  
    public HorizontalScrollViewAdapter(Context context, List<Integer> mDatas, List<String> mDatas2)  
    {  
    	super(context, null, 0, null, null);
        this.mContext = context;  
        mInflater = LayoutInflater.from(context);  
        this.mDatas = mDatas; 
        this.mDatas2 = mDatas2;
    }  
  
    public int getCount()  
    {  
        return mDatas.size();  
    }  
  
    public Object getItem(int position)  
    {  
        return mDatas.get(position);  
    }  
  
    public long getItemId(int position)  
    {  
        return position;  
    }  
  
    public View getView(int position, View convertView, ViewGroup parent)  
    {  
        ViewHolder viewHolder = null;  
        if (convertView == null)  
        {  
            viewHolder = new ViewHolder();  
            convertView = mInflater.inflate(R.layout.platform_tab, parent, false);  
            viewHolder.mImg = (ImageView) convertView.findViewById(R.id.platform_tab_img);  
            viewHolder.mText = (TextView) convertView.findViewById(R.id.platform_name);  
            convertView.setTag(viewHolder);  
        } else  
        {  
            viewHolder = (ViewHolder) convertView.getTag();  
        }  
        if(mDatas.get(position)==R.drawable.company_icon32_big){
        	viewHolder.mImg.setImageResource(mDatas.get(position));  
            viewHolder.mText.setText(mDatas2.get(position));
            viewHolder.mText.setVisibility(View.VISIBLE);
        }else{
        	viewHolder.mImg.setImageResource(mDatas.get(position));  
            viewHolder.mText.setText(mDatas2.get(position));
            viewHolder.mText.setVisibility(View.GONE);
        }
        
  
        return convertView;  
    }  
  
    private class ViewHolder  
    {  
        ImageView mImg;  
        TextView mText;  
    } 
    
    @Override
    public void notifyDataSetChanged() {
    	// TODO Auto-generated method stub
    	super.notifyDataSetChanged();
    }
}
