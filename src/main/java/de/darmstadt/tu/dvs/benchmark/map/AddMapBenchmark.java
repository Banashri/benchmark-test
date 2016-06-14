package de.darmstadt.tu.dvs.benchmark.map;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
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
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class AddMapBenchmark
{
  @Param({"HM", "LHM", "TM"})
  public String type;
  private int value = 0;
  Map<String, Integer> map;
  
  @Setup(Level.Iteration)
  public void setup()
  {
    switch (this.type)
    {
    case "HM": 
      this.map = new HashMap(); break;
    case "LHM": 
      this.map = new LinkedHashMap(); break;
    case "TM": 
      this.map = new TreeMap();
    }
    this.value = 0;
  }
  
  @Benchmark
  @Warmup(iterations=10, batchSize=100)
  @Measurement(iterations=5, batchSize=100)
  @BenchmarkMode({org.openjdk.jmh.annotations.Mode.SingleShotTime})
  public Map<String, Integer> add_100()
  {
    this.value += 1;
    this.map.put(String.valueOf(this.value), Integer.valueOf(this.value));
    return this.map;
  }
  
  @Benchmark
  @Warmup(iterations=20, batchSize=1000)
  @Measurement(iterations=10, batchSize=1000)
  @BenchmarkMode({org.openjdk.jmh.annotations.Mode.SingleShotTime})
  public Map<String, Integer> add_1000()
  {
    this.value += 1;
    this.map.put(String.valueOf(this.value), Integer.valueOf(this.value));
    return this.map;
  }
  
  @Benchmark
  @Warmup(iterations=10, batchSize=10000)
  @Measurement(iterations=5, batchSize=10000)
  @BenchmarkMode({org.openjdk.jmh.annotations.Mode.SingleShotTime})
  public Map<String, Integer> add_10000()
  {
    this.value += 1;
    this.map.put(String.valueOf(this.value), Integer.valueOf(this.value));
    return this.map;
  }
  
  @Benchmark
  @Warmup(iterations=10, batchSize=100000)
  @Measurement(iterations=5, batchSize=100000)
  @BenchmarkMode({org.openjdk.jmh.annotations.Mode.SingleShotTime})
  public Map<String, Integer> add_100000()
  {
    this.value += 1;
    this.map.put(String.valueOf(this.value), Integer.valueOf(this.value));
    return this.map;
  }
  
  @TearDown(Level.Iteration)
  public void tearDown()
  {
    this.map.clear();
  }
  
  public static void main(String[] args)
    throws RunnerException
  {
    Options opt = new OptionsBuilder().include(AddMapBenchmark.class.getSimpleName()).forks(5).result("results_map_add.csv").resultFormat(ResultFormatType.CSV).build();
    
    new Runner(opt).run();
  }
}
