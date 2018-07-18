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

/**
 * {@link HealthIndicator} decorator used to gather health measurement time. The result is
 * stored as a one of {@link Health}'s details.
 *
 * @author Konrad Czajka
 */
public class HealthIndicatorTimer implements HealthIndicator {

	/**
	 * {@link Health} detail's key used to store measurement duration.
	 */
	public static final String DURATION_DETAIL = "measurementDurationMs";

	private final HealthIndicator delegate;

	public HealthIndicatorTimer(HealthIndicator delegate) {
		this.delegate = delegate;
	}

	@Override
	public Health health() {
		long start = System.currentTimeMillis();
		Health delegateHealth = this.delegate.health();
		long end = System.currentTimeMillis();

		Health.Builder result = Health.status(delegateHealth.getStatus());
		delegateHealth.getDetails().forEach(result::withDetail);
		result.withDetail(DURATION_DETAIL, end - start);

		return result.build();
	}

}
