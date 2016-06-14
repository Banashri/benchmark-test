package de.darmstadt.tu.dvs.benchmark.list.collection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.list.TreeList;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
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

@State(Scope.Thread)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class CopyListBenchmark {
	
	@Param({"100","1000","10000","100000"})
	int size;
	
	@Param({"AL-LL", "AL-TL", "LL-AL", "LL-TL", "TL-AL", "TL-LL"})
	String changeTypes;

	private List<Integer> src, dst;
	
	@Setup
    public void setup(){
		
		String[] str = changeTypes.split("-");
		String srcCollection = str[0];
		String dstCollection = str[1];
		
		switch (srcCollection) {
			case "LL" : 	src = new LinkedList<>();; break;
			case "AL" : 	src = new ArrayList<>(); break;
			case "TL" : 	src = new TreeList<>(); break;
    	}
		
		switch (dstCollection) {
			case "LL" : 	dst = new LinkedList<>();; break;
			case "AL" : 	dst = new ArrayList<>(); break;
			case "TL" : 	dst = new TreeList<>(); break;
		}

		for (int i = 0; i < size; i++) {
			src.add(i);
		}
    }

	@TearDown(Level.Invocation)
	public void tearDown() {
		dst.clear();
	}
	
	@Benchmark
	public void copyList(Blackhole bh) {
		bh.consume(dst.addAll(src));
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
                .include(CopyListBenchmark.class.getSimpleName())
                .result("results_list_copy.csv")
                .resultFormat(ResultFormatType.CSV)
                .build();

        new Runner(opt).run();
	}
}
