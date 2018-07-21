/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.health;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Tests for {@link HealthIndicatorTimer}.
 *
 * @author Konrad Czajka
 */
public class HealthIndicatorTimerTests {

	@Test
	public void extendsDetailsWithMeasurementDuration() {
		Health wrappedHealth = Health.up().withDetail("a", "a value").build();
		FixedHealthIndicator wrappedIndicator = new FixedHealthIndicator(wrappedHealth);
		Clock clock = new FakeClock(Instant.now(), Duration.ofMillis(10));

		Health result = new HealthIndicatorTimer(wrappedIndicator, clock).health();
		Assertions.assertThat(
				(long) result.getDetails().get(HealthIndicatorTimer.DURATION_DETAIL))
				.isEqualTo(10L);
		Assertions.assertThat(result.getDetails())
				.containsOnlyKeys(HealthIndicatorTimer.DURATION_DETAIL, "a");
		Assertions.assertThat(result.getStatus()).isEqualTo(Status.UP);
	}

	static class FixedHealthIndicator implements HealthIndicator {

		private final Health health;

		FixedHealthIndicator(Health health) {
			this.health = health;
		}

		@Override
		public Health health() {
			return this.health;
		}

	}

	/**
	 * Fake {@link Clock} implementation. Changes time by configured step each time the
	 * current time is requested.
	 */
	static class FakeClock extends Clock {

		private Instant now;

		private Duration step;

		FakeClock(Instant now, Duration step) {
			this.now = now;
			this.step = step;
		}

		@Override
		public Instant instant() {
			Instant result = this.now;
			this.now = this.now.plus(this.step);
			return result;
		}

		@Override
		public ZoneId getZone() {
			return ZoneId.systemDefault();
		}

		@Override
		public Clock withZone(ZoneId zone) {
			return this;
		}

	}

}
