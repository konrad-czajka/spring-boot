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

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link HealthIndicatorTimer}.
 *
 * @author Konrad Czajka
 */
public class HealthIndicatorTimerTests {

	@Test
	public void extendsDetailsWithMeasurementDuration() {
		Health wrappedHealth = Health.up().withDetail("detail-a", "value-a").build();
		DelayedHealthIndicator wrappedIndicator = new DelayedHealthIndicator(10,
				wrappedHealth);

		Health result = new HealthIndicatorTimer(wrappedIndicator).health();

		Assertions.assertThat(result.getDetails())
				.containsOnlyKeys(HealthIndicatorTimer.DURATION_DETAIL, "detail-a");
		Assertions.assertThat(
				(long) result.getDetails().get(HealthIndicatorTimer.DURATION_DETAIL))
				.isBetween(10L, 100L);

		Assertions.assertThat(result.getStatus()).isEqualTo(Status.UP);
	}

	static class DelayedHealthIndicator implements HealthIndicator {

		private long delay;

		private final Health health;

		DelayedHealthIndicator(long delay, Health health) {
			this.delay = delay;
			this.health = health;
		}

		@Override
		public Health health() {
			try {
				Thread.sleep(this.delay);
			}
			catch (InterruptedException ex) {
				Assert.fail("Thread not expected to be interrupted");
			}
			return this.health;
		}

	}

}
