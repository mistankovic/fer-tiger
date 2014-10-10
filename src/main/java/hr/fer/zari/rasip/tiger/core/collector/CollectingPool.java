package hr.fer.zari.rasip.tiger.core.collector;

import hr.fer.zari.rasip.tiger.domain.Measurement;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface CollectingPool {

	Future<List<Measurement>> submit(Callable<List<Measurement>> task);

}
