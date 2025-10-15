package com.imp.all.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

class DownLoadThread extends Thread{
	int startIndex;
	int endIndex;
	int threadId;
	
	public DownLoadThread(int startIndex, int endIndex, int threadId) {
		super();
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.threadId = threadId;
	}

	@Override
	public void run(){
		//使用http请求下载安装包文件
		URL url;
		try {
			url = new URL(Multidownload.path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			//设置请求数据的区间
			conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
			//请求部分数据的响应码是206
			if(conn.getResponseCode() == 206){
				//获取一部分数据来读取
				InputStream is = conn.getInputStream();
				byte[] b = new byte[1024];
				int len = 0;
				int total = 0;
				//拿到临时文件的引用
				File file = new File("QQ.exe");
				RandomAccessFile raf = new RandomAccessFile(file, "rwd");
				//更新文件的写入位置，startIndex
				raf.seek(startIndex);
				while((len = is.read(b)) != -1 ){
					//每次读取流里面的数据，同步吧数据写入临时文件
					raf.write(b, 0, len);
					total += len;
//					System.out.println("线程" + threadId + "下载了" + total);
				}
				System.out.println("线程" + threadId + "下载过程结束===========================");
				raf.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	};
}