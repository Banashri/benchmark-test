package de.darmstadt.tu.dvs.benchmark.list.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.list.TreeList;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import de.darmstadt.tu.dvs.benchmark.utils.BenchmarkUtils;

@State(Scope.Thread)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class RemoveListBenchmark {

	@Param({"100","1000","10000", "100000"})
	public int size;
	
	@Param({"LL", "AL", "TL"})
	public String type;
	
	List<Integer> list = null;
	int[] randomNos;
	
	@Setup
	public void setup() {
		switch (type) {
			case "LL": list = new LinkedList<>(); break;
			case "AL": list = new ArrayList<>(); break;
			case "TL": list = new TreeList<>(); break;
		}
		
		for (int i = 0; i < size; i++)
			list.add(i);
		
		randomNos = BenchmarkUtils.populateRandomNos(size);
	}

	@Benchmark
	public void removeObjectByForLoop(Blackhole bh) {
		for (int no: randomNos) {
			bh.consume(list.remove(new Integer(no)));
		}
	}

	@Benchmark
	public void removeObjectByIterator(Blackhole bh) {
		Integer elem = null;
		for (int no: randomNos) {
			Iterator<Integer> it = list.iterator();
			while (it.hasNext()) {
				elem = it.next();
				if (elem.equals(no)) {
					it.remove();
					bh.consume(elem);
					break;
				}
			}
		}
	}
	
	/**
	 * Benchmark for removing objects : remove(int index)
	 * @param bh
	 */
	@Benchmark
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public void removeObjectByIndex(Blackhole bh) {
		Random r = new Random(12345678);
		
		while (!list.isEmpty()) {
			int index = r.nextInt(list.size());
			bh.consume(list.remove(index));
		}
	}

	@TearDown
    public void tearDown(){
		list.clear();
    }

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
                .include(RemoveListBenchmark.class.getSimpleName())
                .forks(5)
                .result("results_list_remove.csv")
                .resultFormat(ResultFormatType.CSV)
                .build();

        new Runner(opt).run();
	}
}