package de.darmstadt.tu.dvs.benchmark.stream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.collections4.list.TreeList;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class MapStreamBenchmark {
	
	@Param({"100", "1000", "10000", "100000"})
    public int elementsSize;
	
	private List<Integer> linkedList;
    private List<Integer> arrayList;
	private List<Integer> treeList;
	
	private Set<Integer> hashSet;
	private Set<Integer> linkedHashSet;
	private Set<Integer> treeSet;
	
	
	private Map<String, Integer> hashmap;
	private Map<String, Integer> treemap;
	private Map<String, Integer> linkedHashmap;
	
	@Setup
    public void setUp() {
		arrayList = IntStream.generate(() -> 1).limit(elementsSize).boxed().collect(Collectors.toCollection(ArrayList::new));
		linkedList = new LinkedList<Integer>(arrayList);
        treeList = new TreeList<Integer>(arrayList);

        hashSet = new HashSet<>(arrayList);
        linkedHashSet = new LinkedHashSet<>(arrayList);
        treeSet = new TreeSet<>(arrayList);
    }
	
	@Benchmark
    public int sumLinkedList() {
        return linkedList.stream().mapToInt(i -> i).sum();
    }
	
    @Benchmark
    public int sumArrayList() {
    	return arrayList.stream().mapToInt(i -> i).sum();
    }
    
    @Benchmark
    public int sumTreeList() {
    	return treeList.stream().mapToInt(i -> i).sum();
    }
    
    @Benchmark
    public int sumParallelLinkedList() {
        return linkedList.parallelStream().mapToInt(i -> i).sum();
    }
    
    @Benchmark
    public int sumParallelArrayList() {
    	return arrayList.parallelStream().mapToInt(i -> i).sum();
    }

    @Benchmark
    public int sumParallelTreeList() {
    	return treeList.parallelStream().mapToInt(i -> i).sum();
    }
    
	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
                .include(MapStreamBenchmark.class.getSimpleName())
                .warmupIterations(10)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
	}
}
