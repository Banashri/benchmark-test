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
public class AggregateBenchmark {

	@Param({"100", "1000", "10000", "100000", "1000000"})
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
        linkedList = new LinkedList<>();
        arrayList = new ArrayList<>();
        treeList = new TreeList<>();
        
        hashSet = new HashSet<>();
        linkedHashSet = new LinkedHashSet<>();
        treeSet = new TreeSet<>();

        Integer tempObj = null;
        for (int idx = 0; idx < elementsSize; idx++) {

        	tempObj = new Integer(idx);
            linkedList.add(tempObj);
            arrayList.add(tempObj);
            treeList.add(tempObj);
            
            hashSet.add(tempObj);
            treeSet.add(tempObj);
            linkedHashSet.add(tempObj);
            
            hashmap.put(tempObj.toString(), tempObj);
            linkedHashmap.put(tempObj.toString(), tempObj);
            treemap.put(tempObj.toString(), tempObj);
        }
    }
	
	@Benchmark
    public int sumLinkedList() {
        int sum = 0;
        for (int val : linkedList) {
            sum += val;
        }
        return sum;
    }

    @Benchmark
    public int sumArrayList() {
        int sum = 0;
        for (int val : arrayList) {
            sum += val;
        }
        return sum;
    }
    
    @Benchmark
    public int sumTreeList() {
        int sum = 0;
        for (int val : treeList) {
            sum += val;
        }
        return sum;
    }
    
	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
                .include(AggregateBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
	}
}
