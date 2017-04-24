package com.github.jancajthaml.workingday

import org.scalatest.{FlatSpec, Matchers}

import java.time.LocalDate

class WorkingDaySpecs extends FlatSpec with Matchers {

  val working_days = WorkingDays(List(
    "Saturday",
    "Ressurection+5",
    "Ressurection-5",
    "Sunday",
    "1/1",
    "7/4/2017"
  ))

  "WorkingDays" should "reject predefined holiday" in {
    val holiday_1 = LocalDate.of(2017, 4, 7)
    val holiday_2 = LocalDate.of(2016, 1, 1)
    val holiday_3 = LocalDate.of(8016, 1, 1)
    val working   = LocalDate.of(8016, 4, 7)

    working_days.is(holiday_1) should === (false)
    working_days.is(holiday_2) should === (false)
    working_days.is(holiday_3) should === (false)
    working_days.is(working) should === (true)
  }

  it should "allow valid working day" in {
    val monday = LocalDate.of(2017, 4, 24)

    working_days.is(monday) should === (true)
  }

  it should "reject sunday in future" in {
    val sunday = LocalDate.of(5000, 5, 18)
    working_days.is(sunday) should === (false)
  }

  it should "reject range around ressurection day / easter" in {
    // 7/4/2020
    val before_out   = LocalDate.of(2020, 4, 2)
    val before_range = LocalDate.of(2020, 4, 5)
    val after_range  = LocalDate.of(2020, 4, 7)
    val after_out    = LocalDate.of(2020, 4, 13)

    working_days.is(before_out) should === (true)
    working_days.is(before_range) should === (false)
    working_days.is(after_range) should === (false)
    working_days.is(after_out) should === (true)
  }

  "Calendar" should "find next working day" in {
    val holiday_1 = LocalDate.of(2017, 4, 7)
    working_days.next(holiday_1) should === (LocalDate.of(2017, 4, 10))
  }
}