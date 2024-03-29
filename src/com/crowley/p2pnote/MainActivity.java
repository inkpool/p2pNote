package com.crowley.p2pnote;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.crowley.p2pnote.db.HttpUtils;
import com.crowley.p2pnote.fragment.AnalyzeFragment;
import com.crowley.p2pnote.fragment.IndexFragment;
import com.crowley.p2pnote.fragment.MoreFragment;
import com.crowley.p2pnote.fragment.PlatformFragment;
import com.crowley.p2pnote.fragment.WaterFragment;
import com.crowley.p2pnote.functions.Common;
import com.crowley.p2pnote.ui.SlidingMenu;

import android.R.integer;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	
	private int iCount = -1;
	
	private IndexFragment indexFragment;  
    private WaterFragment waterFragment;  
    private AnalyzeFragment analyzeFragment;  
    private PlatformFragment platformFragment;
    private MoreFragment moreFragment;
    
    private RelativeLayout tab_containerLayout;
    
    private LinearLayout tabIndex;  
    private LinearLayout tabWater;
    private LinearLayout tabAnalyze;
    private LinearLayout tabPlatform;
    //private LinearLayout tabMore;
    
    private SlidingMenu mLeftMenu;
    
    private FragmentManager fragmentManager;
    
    private TextView title;
    
    private TextView newItem;
    private TextView login;
    private RelativeLayout checkTextView;
    //private TextView backupTextView;
    private RelativeLayout adviceTextView;
    private RelativeLayout aboutTextView;
    private RelativeLayout securityTextView;
    //private TextView shareTextView;
    
    private int indexNumber=0;   
    private String versionCode="0";
    
    private String updateUrlString="http://120.27.44.42/p2pbeerich/index.php/update";
    private String downUrlString="";
    PackageInfo pi;
    
    private int state = 0;
    private SweetAlertDialog pDialog;
    
    private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==0x123){
				switch (state) {
				case 0:{
					pDialog.setTitleText("当前已为最新版本!")
	                    .setConfirmText("确定")
	                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
					break;					
				}
				case 1:{
					pDialog.setTitleText("有新的版本可供更新!")
                    .setConfirmText("前去下载")
                    .setCancelText("暂不更新")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			                @Override
			                public void onClick(SweetAlertDialog sDialog) {
			                	Uri uri = Uri.parse(downUrlString);  
			                	Intent it = new Intent(Intent.ACTION_VIEW, uri);  
			                	startActivity(it);
			                }
			            })
                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
					break;
				}
				default:
					break;
				}				
			}
		}		
	};
    	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);   
        try {
			pi=this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			versionCode=Integer.valueOf(pi.versionCode-1).toString();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        initViews();  
        fragmentManager = getFragmentManager();  
        setTabSelection(indexNumber);
    }
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	SharedPreferences preferences=getSharedPreferences("user", MODE_PRIVATE);
		boolean isLogined = preferences.getBoolean("isLogined", false);
		if(isLogined){
			login.setText(preferences.getString("account", "出错啦"));
		}else{
			login.setText("点击登录");
		}
    	super.onStart();
    	switch (indexNumber) {
		case 0:{
			try {
				indexFragment.reflash();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		case 1:{
			waterFragment.reflash();
			break;
		}
		case 2:{
			analyzeFragment.reflash();
			break;
		}
		case 3:{
			platformFragment.reflash();
			break;
		}
		default:
			break;
		}
    }
    
    public void toggleMenu(View view){
    	mLeftMenu.toggle();
    }

	private void setTabSelection(int index) {
		// TODO Auto-generated method stub 
        resetBtn();
        FragmentTransaction transaction = fragmentManager.beginTransaction();  
        hideFragments(transaction);  
        switch (index)  
        {  
        case 0:   
        	((TextView)findViewById(R.id.new_item)).setVisibility(View.VISIBLE);
        	title.setText(R.string.tab_index);
            ((ImageView)tabIndex.findViewById(R.id.tab_index_icon)).setImageResource(R.drawable.index_focus);
            ((TextView)tabIndex.findViewById(R.id.tab_index_text)).setTextColor(getResources().getColor(R.color.tab_text_chosen));
            if (indexFragment == null)  
            {   
            	indexFragment = new IndexFragment();  
                transaction.add(R.id.content, indexFragment);
            } else  
            {   
                transaction.show(indexFragment);
                try {
					indexFragment.reflash();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }  
            break;  
        case 1:  
        	((TextView)findViewById(R.id.new_item)).setVisibility(View.VISIBLE);
        	title.setText(R.string.tab_water);
        	((ImageView)tabWater.findViewById(R.id.tab_water_icon)).setImageResource(R.drawable.water_focus);
            ((TextView)tabWater.findViewById(R.id.tab_water_text)).setTextColor(getResources().getColor(R.color.tab_text_chosen));  
            if (waterFragment == null)  
            {   
            	waterFragment = new WaterFragment();  
                transaction.add(R.id.content, waterFragment);  
            } else  
            {   
                transaction.show(waterFragment); 
                waterFragment.reflash();
            }  
            break;  
        case 2:  
        	((TextView)findViewById(R.id.new_item)).setVisibility(View.VISIBLE);
        	title.setText(R.string.tab_analyze);
        	((ImageView)tabAnalyze.findViewById(R.id.tab_analyze_icon)).setImageResource(R.drawable.analyze_focus);
            ((TextView)tabAnalyze.findViewById(R.id.tab_analyze_text)).setTextColor(getResources().getColor(R.color.tab_text_chosen));  
            if (analyzeFragment == null)  
            {   
            	analyzeFragment = new AnalyzeFragment();  
                transaction.add(R.id.content, analyzeFragment);  
            } else  
            {   
                transaction.show(analyzeFragment);  
                analyzeFragment.reflash();
            }  
            break;  
        case 3: 
        	((TextView)findViewById(R.id.new_item)).setVisibility(View.VISIBLE);
        	title.setText(R.string.tab_platform);
        	((ImageView)tabPlatform.findViewById(R.id.tab_platform_icon)).setImageResource(R.drawable.platform_focus); 
            ((TextView)tabPlatform.findViewById(R.id.tab_platform_text)).setTextColor(getResources().getColor(R.color.tab_text_chosen)); 
            if (platformFragment == null)  
            {   
            	platformFragment = new PlatformFragment();  
                transaction.add(R.id.content, platformFragment); 
            } else  
            {   
                transaction.show(platformFragment);  
                platformFragment.reflash();
            }  
            break;
		/*case 4:  
        	((TextView)findViewById(R.id.new_item)).setVisibility(View.GONE);
			title.setText(R.string.tab_news);
	    	((ImageView)tabMore.findViewById(R.id.tab_more_icon)).setImageResource(R.drawable.more_focus);  
            ((TextView)tabMore.findViewById(R.id.tab_more_text)).setTextColor(getResources().getColor(R.color.tab_text_chosen));
	        if (moreFragment == null)  
	        {   
	        	moreFragment = new MoreFragment();  
	            transaction.add(R.id.content, moreFragment);  
	        } else  
	        {   
	            transaction.show(moreFragment);  
	        }
	        break;  */
	    }  
        transaction.commit();  
		
	}

	private void hideFragments(FragmentTransaction transaction) {
		// TODO Auto-generated method stub
		if (indexFragment != null)  
        {  
            transaction.hide(indexFragment);  
        }  
		if (waterFragment != null)  
        {  
            transaction.hide(waterFragment);  
        }  
		if (analyzeFragment != null)  
        {  
            transaction.hide(analyzeFragment);  
        }  
		if (platformFragment != null)  
        {  
            transaction.hide(platformFragment);  
        }  
		if (moreFragment != null)  
        {  
            transaction.hide(moreFragment);  
        }  		
	}

	private void resetBtn() {
		// TODO Auto-generated method stub
        ((ImageView)tabIndex.findViewById(R.id.tab_index_icon)).setImageResource(R.drawable.index); 
    	((ImageView)tabWater.findViewById(R.id.tab_water_icon)).setImageResource(R.drawable.water);
    	((ImageView)tabAnalyze.findViewById(R.id.tab_analyze_icon)).setImageResource(R.drawable.analyze);  
    	((ImageView)tabPlatform.findViewById(R.id.tab_platform_icon)).setImageResource(R.drawable.platform);  
		//((ImageView)tabMore.findViewById(R.id.tab_more_icon)).setImageResource(R.drawable.more);
		((TextView)tabIndex.findViewById(R.id.tab_index_text)).setTextColor(getResources().getColor(R.color.tab_text));
		((TextView)tabWater.findViewById(R.id.tab_water_text)).setTextColor(getResources().getColor(R.color.tab_text));
		((TextView)tabAnalyze.findViewById(R.id.tab_analyze_text)).setTextColor(getResources().getColor(R.color.tab_text));
		((TextView)tabPlatform.findViewById(R.id.tab_platform_text)).setTextColor(getResources().getColor(R.color.tab_text));
		//((TextView)tabMore.findViewById(R.id.tab_more_text)).setTextColor(getResources().getColor(R.color.tab_text));
	}

	private void initViews() {
		// TODO Auto-generated method stub
		tab_containerLayout = (RelativeLayout) findViewById(R.id.tab_container);
		
		tabIndex = (LinearLayout) findViewById(R.id.tab_index);
		tabWater = (LinearLayout) findViewById(R.id.tab_water);
		tabAnalyze = (LinearLayout) findViewById(R.id.tab_analyze);
		tabPlatform = (LinearLayout) findViewById(R.id.tab_platform);
		//tabMore = (LinearLayout) findViewById(R.id.tab_more);
		
		newItem = (TextView) findViewById(R.id.new_item);
		
		title = (TextView) findViewById(R.id.main_tab_banner_title);		

        mLeftMenu=(SlidingMenu) findViewById(R.id.id_menu);
        login=(TextView) findViewById(R.id.login);   
        //backupTextView=(TextView) findViewById(R.id.backup);
        checkTextView=(RelativeLayout) findViewById(R.id.check_update);
        adviceTextView=(RelativeLayout) findViewById(R.id.advice);
        aboutTextView=(RelativeLayout) findViewById(R.id.about);
        securityTextView=(RelativeLayout) findViewById(R.id.security);
        //shareTextView=(TextView) findViewById(R.id.share);
        
		tab_containerLayout.setOnClickListener(this);
        
		tabIndex.setOnClickListener(this);
		tabWater.setOnClickListener(this);
		tabAnalyze.setOnClickListener(this);
		tabPlatform.setOnClickListener(this);
		//tabMore.setOnClickListener(this);
		newItem.setOnClickListener(this);
		login.setOnClickListener(this);
		//backupTextView.setOnClickListener(this);
		checkTextView.setOnClickListener(this);
		adviceTextView.setOnClickListener(this);
		aboutTextView.setOnClickListener(this);
		securityTextView.setOnClickListener(this);
		//shareTextView.setOnClickListener(this);
		
		SharedPreferences preferences=getSharedPreferences("user", MODE_PRIVATE);
		boolean isLogined = preferences.getBoolean("isLogined", false);
		if(isLogined){
			login.setText(preferences.getString("account", "出错啦"));
		}else{
			login.setText("点击登录");
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId())  
        {
        case R.id.tab_index:{
        	indexNumber=0;
            setTabSelection(0);  
            break;       	
        }
        case R.id.tab_water:{
        	indexNumber=1;
            setTabSelection(1);  
            break;        	
        } 
        case R.id.tab_analyze:{
        	indexNumber=2;
        	setTabSelection(2);  
            break;        	
        }             
        case R.id.tab_platform:{
        	indexNumber=3;
        	setTabSelection(3);  
            break;
        }             
        case R.id.tab_more:{
        	indexNumber=4;
        	setTabSelection(4);  
            break;        	
        }                         
        case R.id.new_item:{
        	Intent intent=new Intent(this,NewItemActivity.class);
        	//模式0表示新建记录
			intent.putExtra("model", "0");
			intent.putExtra("id", "");
			intent.putExtra("platform", "");
            startActivity(intent);
            break;        	
        }        	
        case R.id.login:{
        	if(login.getText().toString().equals("点击登录")){
            	Intent intent2=new Intent(this,LoginActivity.class);
                startActivity(intent2);
        	}else{
        		Intent intent=new Intent(this,UserActivity.class);
                startActivity(intent);
        	}
            break;
        }
        /*case R.id.backup:{
        	Common.toBeContinuedDialog(this).show();
            break;
        }*/
        case R.id.check_update:{
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        	pDialog.setTitleText("检查更新...");
        	pDialog.show();
            pDialog.setCancelable(false);
            final Map<String, String> params = new HashMap<String, String>();
			params.put("version", versionCode);
            new Thread(){
				public void run(){
					String teString=HttpUtils.submitPostData(updateUrlString , params, "utf-8");
					try {
						JSONObject object=new JSONObject(teString);
						state = (Integer) object.get("state");
						if (state==1) {
							downUrlString = (String) object.get("url");
						}
						handler.sendEmptyMessage(0x123);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}.start();
            /*new CountDownTimer(800 * 2, 800) {
                public void onTick(long millisUntilFinished) {
                    // you can change the progress bar color by ProgressHelper every 800 millis
                	iCount++;
                    switch (iCount){
                        case 0:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                            break;
                        case 1:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                            break;
                    }
                }
                public void onFinish() {
                	iCount = -1;
                    pDialog.setTitleText("当前已为最新版本!")
                            .setConfirmText("确定")
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                }
            }.start();*/
        	break;
        }
        case R.id.advice:{
        	Intent intent=new Intent(this,AdviceActivity.class);
            startActivity(intent);
        	break;
        }
        case R.id.about:{
        	Intent intent=new Intent(this,AboutActivity.class);
            startActivity(intent);
        	break;
        }
        case R.id.security:{
        	SharedPreferences preferences=getSharedPreferences("user", MODE_PRIVATE);
    		boolean isLogined = preferences.getBoolean("isLogined", false);
    		if(isLogined){
            	Intent intent=new Intent(this,ModifyPasswordActivity.class);
                startActivity(intent);
    		}else{
    			Common.errorDialog(this, "启用失败", "请先登录账号").show();	
    		}
        	break;
        }/*
        case R.id.share:{
        	Common.toBeContinuedDialog(this).show();
        	break;
        }*/
        default:  
            break;  
        }  
	}
}
