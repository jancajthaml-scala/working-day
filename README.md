## Working Day Calendar

Tiny library for working day calculation.

### How it works

Provided Set of human readable days-off function will create 3 partial functions

| day-off definition | resolved as | note                                                       |
| ------------------ |:-----------:|:-----------------------------------------------------------|
| Mon                | DayOfWeek   | every Mon, Tue ... Sun case insensitive                    |
| Monday             | DayOfWeek   | every Monday, Tuesday ... Sunday case insensitive          |
| dd/mm/yyyy         | LocalDate   | this given date                                            |
| dd/mm/yy           | LocalDate   | this given date                                            |
| dd/mm              | MonthDay    | given day given month                                      |
| Ressurection+1     | Int         | range ressurection of christ (easter) until 1 day after    |
| Ressurection-10    | Int         | range 10 days before until ressurection of christ (easter) |

### usage

```scala
val calendar = WorkingDays(List("Friday", "Saturday", "Sunday"))
val friday = LocalDate.of(2017, 4, 14)

calendar.is(friday)	// friday
> false

calendar.next(friday)
> 2017/4/17

calendar.shift(friday, 5)
> 2017/4/24
````

**is (LocalDate => Boolean)**

true if given day is a working day

**next (LocalDate => LocalDate)**

next working day

**shift ((LocalDate, Int) => LocalDate)**

next working day after x working days

### Performance

1x mean = 0.0 ms / 0.00 kB
100000x mean = 0.88 ms / 0.00 kB
