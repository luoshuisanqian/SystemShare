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
		// ��ת���ǻ�����app
		ylt_btn=(TextView)findViewById(R.id.ylt_btn);
		ylt_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openApp();
				
			}
		});
		//��ת��Ӧ���г�
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
					//��ת
					MarketUtils.launchAppDetail(MainActivity.this,"yys.NMCF_RTYH3App",list.get(0));
				}else{
					Toast.makeText(MainActivity.this, "�����ֻ�δ��װӦ���г�",2000).show();
				}
			}
		});
		
		//����
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
	    * ���ϵͳӦ�ó��򣬲��� 
	    */  
	   private void openApp(){  
	       //Ӧ�ù�������  
	       Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);  
	       mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
	       mPackageManager = this.getPackageManager();  
	       mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);  
	       //����������  
	       Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(mPackageManager));  
	       int i=0;
	       for(ResolveInfo res : mAllApps){  
	           //��Ӧ�õİ�������Activity  
	           String pkg = res.activityInfo.packageName;  
	           String cls = res.activityInfo.name;  
	  
	           if(pkg.contains("com.yys.generalmaintenance")){  
	        	   i++;//����ת�Ļ���i+1
	               ComponentName componet = new ComponentName(pkg, cls);  
	               Intent intent = new Intent();  
	               intent.setComponent(componet);  
	               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	               this.startActivity(intent);  
	           } 
	       } 
	       if(i==0){//i=0����δ���ҵ��ð���
            	Toast.makeText(this, "��δ�ܳɹ���XXX,�����ػ��ֶ�����XXX����ҵ�����",2000).show();  
	       }
	   } 
	   //��������
	    public void shareText(View view) {
	        Intent shareIntent = new Intent();
	        shareIntent.setAction(Intent.ACTION_SEND);
	        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my Share text.");
	        shareIntent.setType("text/plain");
	 
	        //���÷����б�ı��⣬����ÿ�ζ���ʾ�����б�
	        startActivity(Intent.createChooser(shareIntent, "����"));
	    }
	 
	    //������ͼƬ
	    public void shareSingleImage(View view) {
	    	String imagePath = Environment.getExternalStorageDirectory().getPath()  + "/test1.jpg";
	        System.out.println("=-========"+imagePath);
	        //���ļ��õ�uri
	        Uri imageUri = Uri.fromFile(new File(imagePath));
	        Log.d("share", "uri:=-=====" + imageUri);  //�����file:///storage/emulated/0/test.jpg
	 
	        Intent shareIntent = new Intent();
	        shareIntent.setAction(Intent.ACTION_SEND);
	        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
	        shareIntent.setType("image/*");
	        startActivity(Intent.createChooser(shareIntent, "����"));
	    }
	  
	    //�������ͼƬ
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
	        startActivity(Intent.createChooser(shareIntent, "����"));
	    }
	    /**
	     * �洢��ԴΪID��ͼƬ
	     * @param id
	     * @param name
	     */
	    public void saveDrawableById(int id, String name, Bitmap.CompressFormat format) {
	        Drawable drawable = idToDrawable(id);
	        Bitmap bitmap = drawableToBitmap(drawable);
	        saveBitmap(bitmap, name, format);
	    }

	    /**
	     * ����ԴIDת��ΪDrawable
	     * @param id
	     * @return
	     */
	    public Drawable idToDrawable(int id) {
	        return this.getResources().getDrawable(id);
	    }

	    /**
	     * ��Drawableת��ΪBitmap
	     * @param drawable
	     * @return
	     */
	    public Bitmap drawableToBitmap(Drawable drawable) {
	        if(drawable == null)
	            return null;
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    /**
	     * ��Bitmap��ָ����ʽ���浽ָ��·��
	     * @param bitmap
	     * @param path
	     */
	    public void saveBitmap(Bitmap bitmap, String name, Bitmap.CompressFormat format) {
	        // ����һ��λ��SD���ϵ��ļ�
	        File file = new File(Environment.getExternalStorageDirectory().getPath(),
	                name);
	        FileOutputStream out = null;
	        try{
	            // ��ָ���ļ������
	            out = new FileOutputStream(file);
	            // ��λͼ�����ָ���ļ�
	            bitmap.compress(format, 100,
	                    out);
	            out.close();
	            Toast.makeText(this, "sssss", 202).show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	  
}
