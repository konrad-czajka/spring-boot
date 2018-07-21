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
import java.util.Map;

import org.springframework.util.Assert;

/**
 * Decorator for {@link HealthIndicatorRegistry} that wraps each register indicator with
 * time measuring.
 *
 * @author Konrad Czajka
 */
class TimingHealthIndicatorRegistry implements HealthIndicatorRegistry {

	private final HealthIndicatorRegistry delegate;

	private final Clock clock;

	TimingHealthIndicatorRegistry(HealthIndicatorRegistry delegate) {
		this.delegate = delegate;
		this.clock = Clock.systemUTC();
	}

	@Override
	public void register(String name, HealthIndicator healthIndicator) {
		Assert.notNull(healthIndicator, "HealthIndicator must not be null");
		this.delegate.register(name,
				new HealthIndicatorTimer(healthIndicator, this.clock));
	}

	@Override
	public HealthIndicator unregister(String name) {
		return this.delegate.unregister(name);
	}

	@Override
	public HealthIndicator get(String name) {
		return this.delegate.get(name);
	}

	@Override
	public Map<String, HealthIndicator> getAll() {
		return this.delegate.getAll();
	}

}
