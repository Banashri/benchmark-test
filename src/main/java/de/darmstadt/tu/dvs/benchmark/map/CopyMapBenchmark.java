package de.darmstadt.tu.dvs.benchmark.map;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
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
public class CopyMapBenchmark
{
  static HashMap<String, Integer> hm = new HashMap();
  static LinkedHashMap<String, Integer> lhm = new LinkedHashMap();
  static TreeMap<String, Integer> tm = new TreeMap();
  @Param({"100", "1000", "10000", "100000"})
  int size;
  @Param({"HM-LHM", "HM-TM", "LHM-HM", "LHM-TM", "TM-HM", "TM-LHM"})
  String changeTypes;
  private Map<String, Integer> src;
  private Map<String, Integer> dst;
  
  @Setup
  public void setup()
  {
    String[] str = this.changeTypes.split("-");
    switch (str[0])
    {
    case "HM": 
      this.src = new HashMap(); break;
    case "LHM": 
      this.src = new LinkedHashMap(); break;
    case "TM": 
      this.src = new TreeMap();
    }
    switch (str[1])
    {
    case "HM": 
      this.dst = new HashMap(); break;
    case "LHM": 
      this.dst = new LinkedHashMap(); break;
    case "TM": 
      this.dst = new TreeMap();
    }
    for (int i = 0; i < this.size; i++) {
      this.src.put("" + i, Integer.valueOf(i));
    }
  }
  
  @Benchmark
  public void copyCollection(Blackhole bh)
  {
    this.dst.putAll(this.src);
    bh.consume(this.dst.size());
  }
  
  @TearDown(Level.Invocation)
  public void tearDown()
  {
    this.dst.clear();
  }
  
  public static void main(String[] args)
    throws RunnerException
  {
    Options opt = new OptionsBuilder().include(CopyMapBenchmark.class.getSimpleName()).result("results_map_copy.csv").resultFormat(ResultFormatType.CSV).build();
    
    new Runner(opt).run();
  }
}
