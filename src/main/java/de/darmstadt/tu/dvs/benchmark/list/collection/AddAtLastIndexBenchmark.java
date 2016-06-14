package de.darmstadt.tu.dvs.benchmark.list.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.list.TreeList;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
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
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class AddAtLastIndexBenchmark {
	
	@Param({"LL", "AL", "TL"})
	public String type;

	private int value = 0;

	List<Integer> list;
	
	@Setup(Level.Iteration)
    public void setup(){
		switch (type) {
			case "LL" : 	list = new LinkedList<>(); break;
			case "AL" : 	list = new ArrayList<>(); break;
			case "TL" : 	list = new TreeList<>(); break;
		}
		value = 0;
    }
	
	@Benchmark
    @Warmup(iterations = 10, batchSize = 100)
    @Measurement(iterations = 5, batchSize = 100)
	@BenchmarkMode({Mode.SingleShotTime})
    public List<Integer> add_100() {
		value++;
		list.add(new Integer(value));
        return list;
    }

	@Benchmark
    @Warmup(iterations = 10, batchSize = 1000)
    @Measurement(iterations = 5, batchSize = 1000)
	@BenchmarkMode({Mode.SingleShotTime})
    public List<Integer> add_1000() {
		value++;
		list.add(new Integer(value));
        return list;
    }
	
	@Benchmark
    @Warmup(iterations = 10, batchSize = 10000)
    @Measurement(iterations = 5, batchSize = 10000)
    @BenchmarkMode(Mode.SingleShotTime)
    public List<Integer> add_10000() {
		value++;
		list.add(new Integer(value));
        return list;
    }
	
	@Benchmark
    @Warmup(iterations = 10, batchSize = 100000)
    @Measurement(iterations = 5, batchSize = 100000)
    @BenchmarkMode(Mode.SingleShotTime)
    public List<Integer> add_100000() {
		value++;
		list.add(new Integer(value));
        return list;
    }

	/*@Benchmark
    @Warmup(iterations = 10, batchSize = 1000000)
    @Measurement(iterations = 5, batchSize = 1000000)
    @BenchmarkMode(Mode.SingleShotTime)
    public List<Integer> add_1000000() {
		value++;
		list.add(new Integer(value));
        return list;
    }*/
	
	/*@Benchmark
    @Warmup(iterations = 10, batchSize = 10000000)
    @Measurement(iterations = 5, batchSize = 10000000)
    @BenchmarkMode(Mode.SingleShotTime)
    public List<Integer> add_10000000() {
		value++;
		list.add(value);
        return list;
    }*/

	@TearDown(Level.Iteration)
    public void tearDown(){
		list = null;
    }

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
                .include(AddAtLastIndexBenchmark.class.getSimpleName())
                .forks(5)
                .result("results_list_add_last.csv")
                .resultFormat(ResultFormatType.CSV)
                .build();

        new Runner(opt).run();
	}
}
