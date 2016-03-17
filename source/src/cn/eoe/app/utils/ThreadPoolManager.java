package cn.eoe.app.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description 线程池管理, 单件模式
 */
public class ThreadPoolManager {
	// 线程池
	private ExecutorService service;

	private ThreadPoolManager() {
		int num = Runtime.getRuntime().availableProcessors(); // 获取当前系统CPU数目
		service = Executors.newFixedThreadPool(num * 2); // 根据系统资源定义线程池大小:cpuNums*POOL_SIZE
	}

	private static ThreadPoolManager manager;

	public static ThreadPoolManager getInstance() {
		if (manager == null) {
			manager = new ThreadPoolManager();
		}
		return manager;
	}

	// 添加线程
	public void addTask(Runnable runnable) {
		service.submit(runnable);
	}

}
