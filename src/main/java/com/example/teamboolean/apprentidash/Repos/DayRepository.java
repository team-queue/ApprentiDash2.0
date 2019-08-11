package com.example.teamboolean.apprentidash.Repos;

import com.example.teamboolean.apprentidash.Models.Day;
import org.springframework.data.repository.CrudRepository;

public interface DayRepository extends CrudRepository <Day, Long> {


//    List<Day> findAllByClockIn(LocalDate clockIn);
//    List<Day> findAllByClockOut(LocalDate clockOut);
//    List<Day> findAllByLunchStart(LocalDate lunchStart);
//    List<Day> findAllByLunchEnd(LocalDate lunchEnd);
//    List<Day> findAllByClockInBetween(LocalDate clockIn1, LocalDate clockIn2);
}
