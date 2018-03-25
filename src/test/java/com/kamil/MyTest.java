package com.kamil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import application.AppConfig;
import application.Main;

@RunWith(SpringJUnit4ClassRunner.class)
@Import(AppConfig.class)
@ContextConfiguration(classes = Main.class)

public class MyTest {
	//
	// @Autowired
	// TimingService st;
	//
	// @Autowired
	// ProductionService ps;
	//
	// @Autowired
	// StationService ss;

	@Test
	public void check() {
		ZoneId defaultZoneId = ZoneId.systemDefault();
		LocalDateTime time = LocalDateTime.now();
		System.out.println(time);
		System.out.flush();

        Date date = Date.from(time.atZone(defaultZoneId).toInstant());

		System.out.println(date);
		System.out.flush();
	}

}
