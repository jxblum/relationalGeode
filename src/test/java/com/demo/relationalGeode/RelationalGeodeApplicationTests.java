package com.demo.relationalGeode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.gemfire.GemfireUtils;
import org.springframework.data.gemfire.mapping.MappingPdxSerializer;
import org.springframework.geode.config.annotation.ClusterAwareConfiguration;

@SpringBootTest
@SuppressWarnings("unused")
class RelationalGeodeApplicationTests {

	@Autowired
	private Environment environment;

	@Autowired
	private GemFireCache cache;

	@Autowired
	private SomeObjectRepository repository;

	@BeforeEach
	public void assertCacheConfiguration() {

		assertThat(this.environment).isNotNull();
		assertThat(this.repository).isNotNull();

		boolean isClientCache = Arrays.asList(this.environment.getActiveProfiles()).contains("client-cache");

		Predicate<GemFireCache> cacheTypePredicate = isClientCache ? GemfireUtils::isClient : GemfireUtils::isPeer;

		assertThat(this.cache).isNotNull();
		assertThat(this.cache.getName()).isEqualTo("RelationalGeode");
		assertThat(cacheTypePredicate.test(this.cache)).isTrue();
		assertThat(this.cache.getPdxSerializer()).isInstanceOf(MappingPdxSerializer.class);
		assertThat(this.cache.getPdxReadSerialized()).isFalse();

		Predicate<DataPolicy> regionDataPolicyPredicate = isClientCache
			? ClusterAwareConfiguration.ClusterAwareCondition.isAvailable()
				? DataPolicy.EMPTY::equals
				: DataPolicy.NORMAL::equals
			: DataPolicy.PARTITION::equals;

		Region<?, ?> someObjectRegion = this.cache.getRegion(GemfireUtils.toRegionPath("some-object-region"));

		assertThat(someObjectRegion).isNotNull();
		assertThat(someObjectRegion.getName()).isEqualTo("some-object-region");
		assertThat(someObjectRegion.getAttributes()).isNotNull();
		assertThat(regionDataPolicyPredicate.test(someObjectRegion.getAttributes().getDataPolicy())).isTrue();
	}

	@Test
	public void cacheRegionIsInitializedWithData() {

		SomeObject someObject = this.repository.findById("id1").orElse(null);

		assertThat(someObject).isNotNull();
		assertThat(someObject.getSomeId()).isEqualTo("id1");

		List<CustomFirstObject> customFirstObjects = someObject.getCustomFirstObjects();

		assertThat(customFirstObjects).isNotNull();
		assertThat(customFirstObjects).hasSize(1);
		assertThat(customFirstObjects).contains(CustomFirstObject.builder().id(11).amount(2.0d).build());
	}
}
