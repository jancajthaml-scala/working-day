## Working Day Calendar

Tiny library for working day calculation.

### How it works

Provided Set of human readable days-off function will create 3 partial functions wrapped in fascade

| day-off definition | resolved as | note                                                       |
| ------------------ |:-----------:|:-----------------------------------------------------------|
| Mon                | DayOfWeek   | every Mon, Tue ... Sun case insensitive                    |
| Monday             | DayOfWeek   | every Monday, Tuesday ... Sunday case insensitive          |
| dd/mm/yyyy         | LocalDate   | this given date                                            |
| dd/mm/yy           | LocalDate   | this given date                                            |
| dd/mm              | MonthDay    | given day given month                                      |
| Ressurection+1     | Int         | range ressurection of christ (easter) until 1 day after    |
| Ressurection-10    | Int         | range 10 days before until ressurection of christ (easter) |

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

### Performance

1x mean = 0.0 ms / 0.00 kB
100000x mean = 0.88 ms / 0.00 kB
