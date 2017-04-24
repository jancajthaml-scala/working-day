package com.github.jancajthaml.workingday

import java.time.{DayOfWeek, LocalDate, MonthDay}
import java.time.temporal.ChronoUnit.DAYS

import scala.collection.mutable.{Set => Stash}

private[workingday] object helper {

  val week = """([Mm]ond?a?y?|[Tt]ues?d?a?y?|[Ww]edn?e?s?d?a?y?|[Tt]hursd?a?y?|[Ff]rid?a?y?|[Ss]atu?r?d?a?y?|[Ss]und?a?y?)""".r
  val month = """([1-9]|[12]\d|3[01])\/(1[0-2]|[1-9])""".r
  val year = """([1-9]|[12]\d|3[01])\/(1[0-2]|[1-9])\/(\d{4})""".r
  val res = """Ressurection([-+]\d+)""".r

  type DaysOff = (Stash[DayOfWeek], Stash[MonthDay], Stash[LocalDate], Stash[Int])

  def normalize(days: Iterable[String]): DaysOff = {
    val dayOfWeek = Stash.empty[DayOfWeek]
    val monthDay = Stash.empty[MonthDay]
    val localDate = Stash.empty[LocalDate]
    val integer = Stash.empty[Int]

    days.foreach { 
      case week(d)       => dayOfWeek += DayOfWeek.valueOf(d.toUpperCase) 
      case month(d, m)   => monthDay  += MonthDay.of(m.toInt, d.toInt)
      case year(d, m, y) => localDate += LocalDate.of(y.toInt, m.toInt, d.toInt) 
      case res(shift)    => integer   += shift.toInt
    }

    (dayOfWeek, monthDay, localDate, integer)
  }
}

case class WorkingDaysCalendar(check: (LocalDate => Boolean), find: (LocalDate => LocalDate)) {
  def is(date: LocalDate) = check(date)
  def next(date: LocalDate) = find(date)
}

object WorkingDays extends (Iterable[String] => WorkingDaysCalendar) {

  def apply(daysOff: Iterable[String]): WorkingDaysCalendar = {
    val (week, month, day, shift) = helper.normalize(daysOff)

    val check = (date: LocalDate) => {
      !day.contains(date) &&
      !month.contains(MonthDay.from(date)) &&
      !week.contains(DayOfWeek.from(date)) &&
      !shift.contains(DAYS.between({
        // https://en.wikipedia.org/wiki/Computus
        val year = date.getYear
        val a = year % 19
        val b = year / 100
        val c = year % 100
        val d = b / 4
        val e = b % 4
        val f = (b + 8) / 25
        val g = (b - f + 1) / 3
        val h = (19 * a + b - d - g + 15) % 30
        val i = c / 4
        val k = c % 4
        val l = (32 + 2 * e + 2 * i - h - k) % 7
        val m = (a + 11 * h + 22 * l) / 451
        val n = (h + l - 7 * m + 114) / 31
        val p = (h + l - 7 * m + 114) % 31
        LocalDate.of(year, n, p + 1)
      }, date).toInt)
    }

    val nearest = (pivot: LocalDate) => {
      var found = check(pivot)
      var copy = pivot
      while (!found) {
        copy = copy.plusDays(1) 
        found = check(copy)
      }
      copy
    }

    WorkingDaysCalendar(check, nearest)
  }
}
