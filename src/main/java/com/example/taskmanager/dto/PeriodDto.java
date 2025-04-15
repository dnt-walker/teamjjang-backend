package com.example.taskmanager.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class PeriodDto {
    /**
     * 두 날짜 사이의 차이를 일(day) 단위로 계산하는 함수.
     *
     * @param start 시작 날짜
     * @param end   종료 날짜
     * @return 두 날짜 사이의 일 수 (종료 날짜가 시작 날짜보다 이후일 경우 양수, 반대의 경우 음수)
     */
    protected long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 입력된 날짜로부터 현재 날짜까지 경과한 일 수를 계산하는 함수.
     *
     * @param date 기준 날짜
     * @return 기준 날짜로부터 현재 날짜까지 경과한 일 수 (현재 날짜가 기준 날짜보다 이후이면 양수)
     */
    protected long elapsedDaysFrom(LocalDate date) {
        LocalDate now = LocalDate.now();
        return ChronoUnit.DAYS.between(date, now);
    }


    /**
     * 두 날짜 사이의 평일(주말 제외) 수를 계산하는 함수.
     *
     * @param start 시작 날짜
     * @param end   종료 날짜
     * @return 두 날짜 사이의 평일 수 (종료 날짜가 시작 날짜보다 이후일 경우 양수, 반대면 음수)
     */
    protected long workingDaysBetween(LocalDate start, LocalDate end) {
        long workingDays = 0;
        LocalDate date = start;
        // 시작일과 종료일 모두 포함하여 순회합니다.
        while (!date.isAfter(end)) {
            DayOfWeek day = date.getDayOfWeek();
            // 토요일과 일요일이 아니면 평일에 해당하므로 카운트합니다.
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            } else {
                workingDays++;
            }
            date = date.plusDays(1);
        }
        return workingDays;
    }

    /**
     * 기준 날짜로부터 현재 날짜까지의 평일(주말 제외) 경과 일 수를 계산하는 함수.
     *
     * @param date 기준 날짜
     * @return 기준 날짜부터 현재 날짜까지의 평일 수 (오늘이 기준 날짜 이후면 양수)
     */
    protected long elapsedWorkingDaysFrom(LocalDate date) {
        return workingDaysBetween(date, LocalDate.now());
    }
}
