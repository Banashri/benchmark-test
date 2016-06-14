package de.darmstadt.tu.dvs.benchmark.set.collection;

import de.darmstadt.tu.dvs.benchmark.utils.BenchmarkUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
@Warmup(iterations=10, time=1, timeUnit=TimeUnit.SECONDS)
@Measurement(iterations=5, time=1, timeUnit=TimeUnit.SECONDS)
@Fork(5)
@BenchmarkMode({org.openjdk.jmh.annotations.Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class GetSetBenchmark
{
  @Param({"100", "1000", "10000", "100000"})
  public int size;
  @Param({"HS", "LHS", "TS"})
  public String type;
  Set<Integer> set = null;
  int[] randomNos;
  
  @Setup
  public void setup()
  {
    switch (this.type)
    {
    case "HS": 
      this.set = new HashSet(); break;
    case "LHS": 
      this.set = new HashSet(); break;
    case "TS": 
      this.set = new HashSet();
    }
    for (int i = 0; i < this.size; i++) {
      this.set.add(Integer.valueOf(i));
    }
    this.randomNos = BenchmarkUtils.populateRandomNos(this.size);
  }
  
  @Benchmark
  public void measureByIterator(Blackhole bh)
  {
    Iterator<Integer> it;
    for (int i = 0; i < this.randomNos.length; i++) {
      for (it = this.set.iterator(); it.hasNext();)
      {
        Integer elem = (Integer)it.next();
        if (elem.equals(Integer.valueOf(this.randomNos[i])))
        {
          bh.consume(elem);
          break;
        }
      }
    }
  }
  
  @TearDown
  public void teardown()
  {
    this.set.clear();
  }
  
  public static void main(String[] args)
    throws RunnerException
  {
    Options opt = new OptionsBuilder().include(GetSetBenchmark.class.getSimpleName()).result("results_set_get.csv").resultFormat(ResultFormatType.CSV).build();
    
    new Runner(opt).run();
  }
}
