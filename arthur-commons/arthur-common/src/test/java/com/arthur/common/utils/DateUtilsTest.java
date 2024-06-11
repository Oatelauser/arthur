package com.arthur.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

class DateUtilsTest {

	@Test
	void parseDateYMDHMS19() {
		Date date = DateUtils.parseDateYMDHMS19("2023-02-27 13:12:00");
		String s = DateUtils.formatYMDHMS19(date);
		Assertions.assertEquals("2023-02-27 13:12:00", s);
	}

	@Test
	void parseDate() {
		Date date1 = DateUtils.parseDate("2023-02-27 13:12:00", "yyyy-MM-dd HH:mm:ss");
		String s = DateUtils.formatYMDHMS19(date1);
		Assertions.assertEquals("2023-02-27 13:12:00", s);
	}

	@Test
	void parseLocalDateTime() {
		LocalDateTime dateTime = DateUtils.parseLocalDateTime("2023-02-27 13:12:00");
		String s = DateUtils.formatYMDHMS19(dateTime);
		Assertions.assertEquals("2023-02-27 13:12:00", s);
	}

}
