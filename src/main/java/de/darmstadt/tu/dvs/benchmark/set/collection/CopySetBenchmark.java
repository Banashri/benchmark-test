package de.darmstadt.tu.dvs.benchmark.set.collection;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
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
@Warmup(iterations=10, time=1, timeUnit=TimeUnit.SECONDS)
@Measurement(iterations=5, time=1, timeUnit=TimeUnit.SECONDS)
@Fork(5)
@BenchmarkMode({org.openjdk.jmh.annotations.Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class CopySetBenchmark
{
  static HashSet<Integer> hs = new HashSet();
  static LinkedHashSet<Integer> lhs = new LinkedHashSet();
  static TreeSet<Integer> ts = new TreeSet();
  @Param({"100", "1000", "10000", "100000"})
  int size;
  @Param({"HS-LHS", "HS-TS", "LHS-HS", "LHS-TS", "TS-HS", "TS-LHS"})
  String changeTypes;
  private Set<Integer> src;
  private Set<Integer> dst;
  
  @Setup
  public void setup()
  {
    String[] str = this.changeTypes.split("-");
    String srcCollection = str[0];
    String dstCollection = str[1];
    switch (srcCollection)
    {
    case "HS": 
      this.src = hs; break;
    case "LHS": 
      this.src = lhs; break;
    case "TS": 
      this.src = ts;
    }
    switch (dstCollection)
    {
    case "HS": 
      this.dst = hs; break;
    case "LHS": 
      this.dst = lhs; break;
    case "TS": 
      this.dst = ts;
    }
    for (int i = 0; i < this.size; i++) {
      this.src.add(Integer.valueOf(i));
    }
  }
  
  @TearDown(Level.Invocation)
  public void tearDown()
  {
    this.dst.clear();
  }
  
  @Benchmark
  public void copyCollection(Blackhole bh)
  {
    bh.consume(this.dst.addAll(this.src));
  }
  
  public static void main(String[] args)
    throws RunnerException
  {
    Options opt = new OptionsBuilder().include(CopySetBenchmark.class.getSimpleName()).result("results_set_copy.csv").resultFormat(ResultFormatType.CSV).build();
    
    new Runner(opt).run();
  }
}
