package com.github.jancajthaml.workingday

import org.scalatest.{FlatSpec, Matchers}

import java.time.LocalDate

class WorkingDaySpecs extends FlatSpec with Matchers {

  val calendar = WorkingDays(List(
    "Saturday",
    "Ressurection+5",
    "Ressurection-5",
    "Sunday",
    "1/1",
    "7/4/2017"
  ))

  "WorkingDays" should "reject predefined holiday" in {
    val holiday1 = LocalDate.of(2017, 4, 7)
    val holiday2 = LocalDate.of(2016, 1, 1)
    val holiday3 = LocalDate.of(8016, 1, 1)
    val workday   = LocalDate.of(8016, 4, 7)

    calendar.is(holiday1) should === (false)
    calendar.is(holiday2) should === (false)
    calendar.is(holiday3) should === (false)
    calendar.is(workday) should === (true)
  }

  it should "allow valid working day" in {
    val monday = LocalDate.of(2017, 4, 24)
    calendar.is(monday) should === (true)
  }

  it should "reject sunday in future" in {
    val sunday = LocalDate.of(5000, 5, 18)
    calendar.is(sunday) should === (false)
  }

  it should "reject range around ressurection day / easter" in {
    // 7/4/2020
    val beforeOut   = LocalDate.of(2020, 4, 2)
    val beforeRange = LocalDate.of(2020, 4, 5)
    val afterRange  = LocalDate.of(2020, 4, 7)
    val afterOut    = LocalDate.of(2020, 4, 13)

    calendar.is(beforeOut) should === (true)
    calendar.is(beforeRange) should === (false)
    calendar.is(afterRange) should === (false)
    calendar.is(afterOut) should === (true)
  }

  "Calendar" should "find next working day" in {
    val holiday1 = LocalDate.of(2017, 4, 7)
    calendar.next(holiday1) should === (LocalDate.of(2017, 4, 10))
  }

  it should "find tuesday of next week after 6 working days from monday" in {
    val monday = LocalDate.of(2017, 4, 24)
    calendar.shift(monday, 6) should === (LocalDate.of(2017, 5, 2))
  }
}