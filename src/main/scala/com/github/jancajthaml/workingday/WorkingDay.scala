package com.github.jancajthaml.workingday

import java.time.{DayOfWeek, LocalDate, MonthDay}
import java.time.temporal.ChronoUnit.DAYS

private[workingday] object calendar {

  val week = """([Mm]ond?a?y?|[Tt]ues?d?a?y?|[Ww]edn?e?s?d?a?y?|[Tt]hursd?a?y?|[Ff]rid?a?y?|[Ss]atu?r?d?a?y?|[Ss]und?a?y?)""".r
  val month = """(\d+)\/(\d+)""".r
  val year = """(\d+)\/(\d+)\/(\d+)""".r
  val res = """Ressurection([-+]\d+)""".r

  type Holidays = (List[DayOfWeek], List[MonthDay], List[LocalDate], List[Integer])

  def normalize(days: List[String]): Holidays = {
    val dayOfWeek = scala.collection.mutable.ListBuffer.empty[DayOfWeek]
    val monthDay = scala.collection.mutable.ListBuffer.empty[MonthDay]
    val localDate = scala.collection.mutable.ListBuffer.empty[LocalDate]
    val integer = scala.collection.mutable.ListBuffer.empty[Integer]

    days.foreach { 
      case week(d)       => dayOfWeek += DayOfWeek.valueOf(d.toUpperCase) 
      case month(d, m)   => monthDay += MonthDay.of(m.toInt, d.toInt)
      case year(d, m, y) => localDate += LocalDate.of(y.toInt, m.toInt, d.toInt) 
      case res(shift)    => integer += Integer.valueOf(shift)
    }

    (dayOfWeek.toList, monthDay.toList, localDate.toList, integer.toList)
  }
}

object WorkingDays extends (List[String] => (LocalDate => Boolean)) {

  import calendar.normalize

  def apply(daysOff: List[String]): (LocalDate => Boolean) = {
    val (voidWD, voidM, voidD, ressurection) = calendar.normalize(daysOff)

    (date: LocalDate) => {
      !voidD.contains(date) &&
      !voidM.contains(MonthDay.from(date)) &&
      !voidWD.contains(DayOfWeek.from(date)) &&
      !ressurection.contains(DAYS.between({
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
  }
}
