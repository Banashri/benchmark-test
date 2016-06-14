package de.darmstadt.tu.dvs.benchmark.set.collection;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
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
public class AddSetBenchmark
{
  @Param({"HS", "LHS", "TS"})
  public String type;
  private int value = 0;
  Set<Integer> set;
  
  @Setup(Level.Iteration)
  public void setup()
  {
    switch (this.type)
    {
    case "HS": 
      this.set = new HashSet(); break;
    case "LHS": 
      this.set = new LinkedHashSet(); break;
    case "TS": 
      this.set = new TreeSet();
    }
    this.value = 0;
  }
  
  @Benchmark
  @Warmup(iterations=10, batchSize=100)
  @Measurement(iterations=5, batchSize=100)
  @BenchmarkMode({org.openjdk.jmh.annotations.Mode.SingleShotTime})
  public Set<Integer> add_100()
  {
    this.value += 1;
    this.set.add(Integer.valueOf(this.value));
    return this.set;
  }
  
  @Benchmark
  @Warmup(iterations=10, batchSize=1000)
  @Measurement(iterations=5, batchSize=1000)
  @BenchmarkMode({org.openjdk.jmh.annotations.Mode.SingleShotTime})
  public Set<Integer> add_1000()
  {
    this.value += 1;
    this.set.add(Integer.valueOf(this.value));
    return this.set;
  }
  
  @Benchmark
  @Warmup(iterations=10, batchSize=10000)
  @Measurement(iterations=5, batchSize=10000)
  @BenchmarkMode({org.openjdk.jmh.annotations.Mode.SingleShotTime})
  public Set<Integer> add_10000()
  {
    this.value += 1;
    this.set.add(Integer.valueOf(this.value));
    return this.set;
  }
  
  @Benchmark
  @Warmup(iterations=10, batchSize=100000)
  @Measurement(iterations=5, batchSize=100000)
  @BenchmarkMode({org.openjdk.jmh.annotations.Mode.SingleShotTime})
  public Set<Integer> add_100000()
  {
    this.value += 1;
    this.set.add(Integer.valueOf(this.value));
    return this.set;
  }
  
  @TearDown(Level.Iteration)
  public void tearDown()
  {
    this.set.clear();
  }
  
  public static void main(String[] args)
    throws RunnerException
  {
    Options opt = new OptionsBuilder().include(AddSetBenchmark.class.getSimpleName()).forks(5).result("results_set_add.csv").resultFormat(ResultFormatType.CSV).build();
    
    new Runner(opt).run();
  }
}
