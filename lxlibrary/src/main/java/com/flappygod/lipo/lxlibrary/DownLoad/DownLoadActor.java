package com.flappygod.lipo.lxlibrary.DownLoad;

import android.os.Handler;
import android.os.Message;

import com.flappygod.lipo.lxlibrary.Tools.CreateDirTool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


/***************************
 * 下载工具
 */
public class DownLoadActor {

	//当前的状态
	private int  downLoadState=-1;
	//下载失败
	public final static int ERROR = 3;
    //下载完成
	public final static int DONE = 2;
    //下载取消
	public final static int CANCEL = 1;
    //正在下载
	public final static int DOWNLOADING = 0;
	// 下载进度
	private int progress = 0;
	// 终止下载线程的标志
	private boolean threadStopFlag = false;
	// 线程是否忙碌
	private boolean ThreadBusy = false;
	// 下载的地址
	private String urlPath;
	// 下载保存的路径
	private String dirpath;
	// 下载保存的文件名称
	private String fileName;
	// 下载的监听
	private DownLoadListener listener;

	/***********
	 * 构造器
	 *
	 * @param urlPath
	 *            需要下载的地址
	 * @param dirpath
	 *            需要保存的地址
	 */
	public DownLoadActor(String urlPath, String dirpath) {
		this.urlPath = urlPath;
		this.dirpath = dirpath;
		initFileName();
	}

	/***********
	 * 构造器
	 *
	 * @param urlPath
	 *            需要下载的地址
	 * @param dirpath
	 *            需要保存的地址
	 * @param dirpath
	 *            需要保存的名称
	 */
	public DownLoadActor(String urlPath, String dirpath, String fileName) {
		this.urlPath = urlPath;
		this.dirpath = dirpath;
		this.fileName = fileName;
		if (fileName == null) {
			initFileName();
		}
	}

	public int getDownLoadState() {
		return downLoadState;
	}

	public void setDownLoadState(int downLoadState) {
		this.downLoadState = downLoadState;
	}

	/***********
	 * 获取下载的地址
	 * 
	 * @return
	 */
	public String getDownLoadUrl() {
		return urlPath;
	}

	/*******
	 * 设置下载的监听
	 */
	public void setDownLoadListener(DownLoadListener li) {
		listener = li;
	}

	/************
	 * 取消下载，是否取消成功需要在监听中获得回调
	 */
	public void cancle() {
		threadStopFlag = true;
	}

	/*************
	 * 获取progress
	 * 
	 * @return
	 */
	public int getProgress() {
		return progress;
	}

	/******
	 * 初始化设置文件的名称
	 */
	private void initFileName() {
		if (urlPath == null || "".equals(urlPath)) {
			return;
		}
		// 替换掉四个斜杠的情况
		String men = urlPath.replace("\\\\", "/");
		String strs[] = men.split("/");
		if (strs.length > 0) {
			fileName = strs[strs.length - 1];
		}
	}

	/***********
	 * 开始下载的任务
	 */
	public synchronized void excute() {
		if (ThreadBusy) {
			return;
		} else {
			ThreadBusy = true;
		}

		progress = 0;
		final Handler proHanlder = new Handler() {
			public void handleMessage(Message msg) {
				//正在下载
				if (msg.what == DOWNLOADING && msg.arg1 != progress) {
					progress = msg.arg1;
					if (listener != null) {
						listener.downLoading(progress);
					}
					downLoadState=DOWNLOADING;
				} else if (msg.what == CANCEL) {
					// 下载取消
					if (listener != null) {
						listener.downloadCancled();
					}
					downLoadState=CANCEL;
				} else if (msg.what == DONE) {
					// 下载完成
					if (listener != null) {
						listener.downLoadSuccess(dirpath + fileName, fileName);
					}
					downLoadState=DONE;
				} else if (msg.what == ERROR) {
					// 下载失败
					if (listener != null) {
						listener.downloadError((Exception) msg.obj);
					}
					downLoadState=ERROR;
				}

			}
		};

		new Thread() {
			public void run() {
				RandomAccessFile rafileApk = null;
				InputStream inputStream = null;
				int fileSize = 0;
				try {
					/**** 起始字节设置为零 ****/
					long start = 0;
					/**** 创建文件夹,保证需要保存到的文件夹存在 ****/
					CreateDirTool.createDir(dirpath);
					/**** 获取断点续传文件 ****/
					File logfile = new File(dirpath + fileName + ".cfg");
					/**** 获取下载的apk文件 ****/
					File file = new File(dirpath + fileName + ".data");
					/**** 判断apkfile是否存在 ****/
					File apkfile = new File(dirpath + fileName);
					/**** 如果已经存在了这个文件 就不要再去下载了 ****/
					if (apkfile.exists()) {
						Message m = new Message();
						m.what = DONE;
						proHanlder.sendMessage(m);
						return;
					}
					/**** 如果两个文件都存在那么就读取log中的断点数据 ****/
					if (logfile.exists() && file.exists()) {
						DataInputStream datain = null;
						FileInputStream login = null;
						try {
							/**** 获取到已经存储的长度数据 ****/
							login = new FileInputStream(logfile);
							datain = new DataInputStream(login);
							start = datain.readLong();// 读取到已经写入了多少
						} catch (Exception e) {
							/**** 进行初始化 ****/
							start = 0;
							logfile.createNewFile();
							file.createNewFile();
						} finally {
							/**** 关闭流 ****/
							if (login != null)
								login.close();
							if (datain != null)
								datain.close();
						}
					} else {
						/**** 进行初始化 ****/
						start = 0;
						logfile.createNewFile();
						file.createNewFile();
					}
					/**** 取得apk文件的写入 ****/
					rafileApk = new RandomAccessFile(file, "rw");
					/**** 定位到开始的地方 ****/
					rafileApk.seek(start);
					/**** url开始连接 ****/
					URL url = new URL(urlPath);
					/**** 获取HttpURLConnection ****/
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestProperty("Accept-Encoding", "identity");
					/**** 设置 User-Agent ****/
					conn.setRequestProperty("User-Agent", "NetFox");
					/**** 设置断点续传的开始位置 ****/
					conn.setRequestProperty("RANGE",
							"bytes=" + Long.toString(start) + "-");

					/**** 设置总大小 ****/
					fileSize = (int) (conn.getContentLength() + start);
					int responseCode = conn.getResponseCode();
					/**** 失败抛异常 ****/
					if (!(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_PARTIAL)) {
						throw new Exception("connection error" + responseCode
								+ " url:" + url);
					}
					/**** 成功继续执行 ****/
					else {
						inputStream = conn.getInputStream();
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = inputStream.read(buffer)) != -1) {
							/****如果说线程取消了，就不在写入了****/
							if (threadStopFlag)
								break;
							rafileApk.write(buffer, 0, len);
							/****新写入了len个字节****/
							start = start + len;
							Message m = new Message();
							m.what = DOWNLOADING;
							m.arg1 = (int) (start * 100 / fileSize);
							proHanlder.removeMessages(0);
							proHanlder.sendMessage(m);
						}
						/****把信息存了****/
						{
							FileOutputStream logout = null;
							DataOutputStream outlogdata = null;
							try {
								logout = new FileOutputStream(logfile);
								outlogdata = new DataOutputStream(logout);
								outlogdata.writeLong(start);
								outlogdata.close();
							} catch (Exception e) {
								throw new Exception("log error" + responseCode
										+ " url:" + url);
							} finally {
								if (outlogdata != null)
									outlogdata.close();
								if (logout != null)
									logout.close();
							}
						}
						/****如果说已经全部下载完了****/
						if (len == -1)
						{
							file.renameTo(apkfile);
							Message m = new Message();
							m.what = DONE;
							proHanlder.sendMessage(m);
							return;
						}
						/****如果还没有下载完就代表是取消的****/
						else {
							Message m = new Message();
							m.what = CANCEL;
							proHanlder.sendMessage(m);
						}
					}
					ThreadBusy = false;
				} catch (Exception e) {
					Message m = proHanlder.obtainMessage(ERROR, e);
					proHanlder.sendMessage(m);
					ThreadBusy = false;
				} finally {
					/****关闭RandomAccessFile****/
					if (rafileApk != null)
						try {
							rafileApk.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					/****关闭inputStream****/
					if (inputStream != null)
						try {
							inputStream.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}

				}
			}
		}.start();
	}

}
