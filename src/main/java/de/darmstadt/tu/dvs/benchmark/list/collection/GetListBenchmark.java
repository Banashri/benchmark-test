package de.darmstadt.tu.dvs.benchmark.list.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.list.TreeList;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
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
@Fork(5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class GetListBenchmark {
	
	@Param({"100","1000","10000","100000"})
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
	public void getByIndex(Blackhole bh) {
		for (int x : randomNos)
			bh.consume(list.get(x));
	}
	
	@Benchmark
	public void getByIterator(Blackhole bh) {
		Integer elem = null;
		
		for (int x : randomNos) {
			Iterator<Integer> it = list.iterator();
			while (it.hasNext()) {
				elem = it.next();
				if (elem.equals(x)) {
					bh.consume(elem);
					break;
				}
			}
		}
	}

	@TearDown
	public void teardown() {
		list.clear();
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
                .include(GetListBenchmark.class.getSimpleName())
                .result("results_list_get.csv")
                .resultFormat(ResultFormatType.CSV)
                .build();

        new Runner(opt).run();
	}
}
