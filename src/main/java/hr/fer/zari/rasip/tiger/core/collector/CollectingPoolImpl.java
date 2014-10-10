package hr.fer.zari.rasip.tiger.core.collector;

import hr.fer.zari.rasip.tiger.domain.Measurement;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CollectingPoolImpl implements CollectingPool {

	private ExecutorService executorService;
	
	private static CollectingPoolImpl instance;
	
	private CollectingPoolImpl() {
		executorService = Executors.newCachedThreadPool();
	}
	
	public static CollectingPoolImpl instance() {
		if(instance == null){
			instance = new CollectingPoolImpl();
		}
		
		return instance;
	}
	
	@Override
	public Future<List<Measurement>> submit(Callable<List<Measurement>> task) {
		return executorService.submit(task);
	}
}
