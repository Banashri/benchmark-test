package de.darmstadt.tu.dvs.benchmark.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
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

import de.darmstadt.tu.dvs.benchmark.utils.BenchmarkUtils;

@State(Scope.Thread)
@Warmup(iterations=10, time=1, timeUnit=TimeUnit.SECONDS)
@Measurement(iterations=5, time=1, timeUnit=TimeUnit.SECONDS)
@Fork(5)
@BenchmarkMode({org.openjdk.jmh.annotations.Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class GetMapBenchmark
{
  @Param({"100", "1000", "10000", "100000"})
  public int size;
  @Param({"HM", "LHM", "TM"})
  public String type;
  Map<String, Integer> map = null;
  int[] randomNos;
  
  @Setup
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
    for (int i = 0; i < this.size; i++) {
      this.map.put("" + i, Integer.valueOf(i));
    }
    this.randomNos = BenchmarkUtils.populateRandomNos(this.size);
  }
  
  @Benchmark
  public void measureMapGet(Blackhole bh)
  {
    for (int x : this.randomNos) {
      bh.consume(this.map.get("" + x));
    }
  }
  
  @Benchmark
  public void measureMapIteratorGet(Blackhole bh)
  {
    Map.Entry<String, Integer> entry = null;
    Iterator<Map.Entry<String, Integer>> it;
    for (int i = 0; i < this.randomNos.length; i++) {
      for (it = this.map.entrySet().iterator(); it.hasNext();)
      {
        entry = (Map.Entry)it.next();
        if (((String)entry.getKey()).equals("" + this.randomNos[i])) {
          bh.consume(entry.getValue());
        }
      }
    }
  }
  
  @TearDown
  public void teardown()
  {
    this.map.clear();
  }
  
  public static void main(String[] args)
    throws RunnerException
  {
    Options opt = new OptionsBuilder().include(GetMapBenchmark.class.getSimpleName()).result("results_map_get.csv").resultFormat(ResultFormatType.CSV).build();
    
    new Runner(opt).run();
  }
}
