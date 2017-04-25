package com.github.jancajthaml.workingday

import org.scalameter.api.{Measurer, Bench, Gen, exec}

import java.time.LocalDate

object RegressionResources extends Bench.OfflineReport {

  override def measurer = new Measurer.MemoryFootprint

  val times = Gen.range("times")(0, 100000, 20000)
  val working_days = WorkingDays(List(
    "Saturday",
    "Ressurection+5",
    "Ressurection-5",
    "Sunday",
    "1/1",
    "7/4/2017"
  ))
  val day = LocalDate.of(2017, 4, 26)

  performance of "com.github.jancajthaml.WorkingDays" in {
    measure method "is" in {
      using(times) config (
        exec.minWarmupRuns -> 2,
        exec.maxWarmupRuns -> 5,
        exec.benchRuns -> 5,
        exec.independentSamples -> 1
      ) in { sz => { (0 to sz).foreach { _ => { working_days.is(day) } } } }
    }
  }
}


object RegressionPerformance extends Bench.OfflineReport {

  val times = Gen.range("times")(0, 100000, 20000)
  val working_days = WorkingDays(List(
    "Saturday",
    "Ressurection+5",
    "Ressurection-5",
    "Sunday",
    "1/1",
    "7/4/2017"
  ))
  val day = LocalDate.of(2017, 4, 26)

  performance of "com.github.jancajthaml.WorkingDays" in {
    measure method "is" in {
      using(times) config (
        exec.benchRuns -> 20,
        exec.independentSamples -> 1,
        exec.outliers.covMultiplier -> 1.5,
        exec.outliers.suspectPercent -> 40
      ) in { sz => { (0 to sz).foreach { _ => { working_days.is(day) } } } }
    }
  }

}
