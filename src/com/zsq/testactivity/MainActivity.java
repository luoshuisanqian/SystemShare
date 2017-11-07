package com.zsq.testactivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tools.MarketUtils;

import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class MainActivity extends Activity {

    private TextView ylt_btn;
    private PackageManager mPackageManager;  
	private List<ResolveInfo> mAllApps;
	private TextView yhsc_btn; 
	private ArrayList<String> list=new ArrayList<String>();
	private TextView share_btn;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveDrawableById(R.drawable.alerweima,"test1.jpg",Bitmap.CompressFormat.JPEG);
        initview();
    }
	 
	private void initview() {
		// 跳转到智慧市政app
		ylt_btn=(TextView)findViewById(R.id.ylt_btn);
		ylt_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openApp();
				
			}
		});
		//跳转到应用市场
		yhsc_btn=(TextView)findViewById(R.id.yhsc_btn);
		yhsc_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				list=MarketUtils.queryInstalledMarketPkgs(MainActivity.this);
				if(list.size()>0){
					for (int i = 0; i < list.size(); i++) {
						System.out.println("=-===="+list.get(i));
					}
					//跳转
					MarketUtils.launchAppDetail(MainActivity.this,"yys.NMCF_RTYH3App",list.get(0));
				}else{
					Toast.makeText(MainActivity.this, "您的手机未安装应用市场",2000).show();
				}
			}
		});
		
		//分享
		share_btn=(TextView)findViewById(R.id.share_btn);
		share_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				shareSingleImage(arg0);
				
			}
		});
		
	}
	
	
	 /** 
	    * 检查系统应用程序，并打开 
	    */  
	   private void openApp(){  
	       //应用过滤条件  
	       Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);  
	       mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
	       mPackageManager = this.getPackageManager();  
	       mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);  
	       //按包名排序  
	       Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(mPackageManager));  
	       int i=0;
	       for(ResolveInfo res : mAllApps){  
	           //该应用的包名和主Activity  
	           String pkg = res.activityInfo.packageName;  
	           String cls = res.activityInfo.name;  
	  
	           if(pkg.contains("com.yys.generalmaintenance")){  
	        	   i++;//能跳转的话则i+1
	               ComponentName componet = new ComponentName(pkg, cls);  
	               Intent intent = new Intent();  
	               intent.setComponent(componet);  
	               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	               this.startActivity(intent);  
	           } 
	       } 
	       if(i==0){//i=0代表未能找到该包名
            	Toast.makeText(this, "您未能成功打开XXX,请下载或手动进入XXX进行业务操作",2000).show();  
	       }
	   } 
	   //分享文字
	    public void shareText(View view) {
	        Intent shareIntent = new Intent();
	        shareIntent.setAction(Intent.ACTION_SEND);
	        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my Share text.");
	        shareIntent.setType("text/plain");
	 
	        //设置分享列表的标题，并且每次都显示分享列表
	        startActivity(Intent.createChooser(shareIntent, "分享到"));
	    }
	 
	    //分享单张图片
	    public void shareSingleImage(View view) {
	    	String imagePath = Environment.getExternalStorageDirectory().getPath()  + "/test1.jpg";
	        System.out.println("=-========"+imagePath);
	        //由文件得到uri
	        Uri imageUri = Uri.fromFile(new File(imagePath));
	        Log.d("share", "uri:=-=====" + imageUri);  //输出：file:///storage/emulated/0/test.jpg
	 
	        Intent shareIntent = new Intent();
	        shareIntent.setAction(Intent.ACTION_SEND);
	        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
	        shareIntent.setType("image/*");
	        startActivity(Intent.createChooser(shareIntent, "分享到"));
	    }
	  
	    //分享多张图片
	    public void shareMultipleImage(View view) {
	        ArrayList<Uri> uriList = new ArrayList();
	 
	        String path = Environment.getExternalStorageDirectory() + File.separator;
	        uriList.add(Uri.fromFile(new File(path+"australia_1.jpg")));
	        uriList.add(Uri.fromFile(new File(path+"australia_2.jpg")));
	        uriList.add(Uri.fromFile(new File(path+"australia_3.jpg")));
	 
	        Intent shareIntent = new Intent();
	        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
	        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
	        shareIntent.setType("image/*");
	        startActivity(Intent.createChooser(shareIntent, "分享到"));
	    }
	    /**
	     * 存储资源为ID的图片
	     * @param id
	     * @param name
	     */
	    public void saveDrawableById(int id, String name, Bitmap.CompressFormat format) {
	        Drawable drawable = idToDrawable(id);
	        Bitmap bitmap = drawableToBitmap(drawable);
	        saveBitmap(bitmap, name, format);
	    }

	    /**
	     * 将资源ID转化为Drawable
	     * @param id
	     * @return
	     */
	    public Drawable idToDrawable(int id) {
	        return this.getResources().getDrawable(id);
	    }

	    /**
	     * 将Drawable转化为Bitmap
	     * @param drawable
	     * @return
	     */
	    public Bitmap drawableToBitmap(Drawable drawable) {
	        if(drawable == null)
	            return null;
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    /**
	     * 将Bitmap以指定格式保存到指定路径
	     * @param bitmap
	     * @param path
	     */
	    public void saveBitmap(Bitmap bitmap, String name, Bitmap.CompressFormat format) {
	        // 创建一个位于SD卡上的文件
	        File file = new File(Environment.getExternalStorageDirectory().getPath(),
	                name);
	        FileOutputStream out = null;
	        try{
	            // 打开指定文件输出流
	            out = new FileOutputStream(file);
	            // 将位图输出到指定文件
	            bitmap.compress(format, 100,
	                    out);
	            out.close();
	            Toast.makeText(this, "sssss", 202).show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	  
}
