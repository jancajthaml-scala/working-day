## Working Day Calendar

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2b57e64cb7b3446086fbfd8006ea3842)](https://www.codacy.com/app/jan-cajthaml/working-day?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jancajthaml-scala/working-day&amp;utm_campaign=Badge_Grade) [![Build Status](https://travis-ci.org/jancajthaml-scala/working-day.svg?branch=master)](https://travis-ci.org/jancajthaml-scala/working-day)

Tiny library for working day calculation.

### How it works

Provided Set of human readable days-off function will create 3 partial functions wrapped in fascade

### Usage

```scala
val calendar = WorkingDays(List("Friday", "Saturday", "Sunday"))
val friday = LocalDate.of(2017, 4, 14)

// is this date working day?
calendar.is(friday)
> false

// what is next working day?
calendar.next(friday)
> 2017/4/17

// what is date after 5 working days from this date?
calendar.shift(friday, 5)
> 2017/4/24
````

### Days off dictionary

| defined as      | resolved as | note                                                       |
| :-------------- |:-----------:|:-----------------------------------------------------------|
| Mon             | DayOfWeek   | every Mon, Tue ... Sun case insensitive                    |
| Monday          | DayOfWeek   | every Monday, Tuesday ... Sunday case insensitive          |
| dd/mm/yyyy      | LocalDate   | this given date                                            |
| dd/mm/yy        | LocalDate   | this given date                                            |
| dd/mm           | MonthDay    | given day given month                                      |
| Ressurection+1  | Int         | range ressurection of christ (easter) until 1 day after    |
| Ressurection-10 | Int         | range 10 days before until ressurection of christ (easter) |

### Performance

- 1x mean = 0.0 ms / 0.00 kB
- 100 000x mean = 0.88 ms / 0.00 kB
