package fr.company.properties;

import java.util.Collections;

import org.apache.commons.configuration.AbstractConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicLongProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringListProperty;
import com.netflix.config.DynamicStringMapProperty;
import com.netflix.config.DynamicStringProperty;
import com.netflix.config.jmx.ConfigJMXManager;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
public class ArchaiusTest {

	@BeforeAll
	static void setup() {
		System.out.println("@BeforeAll - executes once before all test methods in this class");
		AbstractConfiguration configInstance = ConfigurationManager.getConfigInstance();
		ConfigJMXManager.registerConfigMbean(configInstance);
		
		DynamicStringProperty sampleProp = DynamicPropertyFactory.getInstance().getStringProperty("stringprop", "");
		sampleProp.addCallback(() -> System.out.println("Quelqun change la valeur"));
	}

	@BeforeEach
	void init() {
		System.out.println("@BeforeEach - executes before each test method in this class");
	}

	@DisplayName("Single test successful")
	@Disabled("Not implemented yet")
	@Test
	void testSingleSuccessTest() {
		System.out.println("Success");
	}

	@Test
	@Disabled("Not implemented yet")
	void testShowSomething() {
	}

	@Test
	public void testBasicStringProps() {
		DynamicStringProperty sampleProp = DynamicPropertyFactory.getInstance().getStringProperty("stringprop", "");
		assertThat(sampleProp.get(), equalTo("propvalue"));
	}

	@Test
	public void testBasicListProps() {
		DynamicStringListProperty listProperty = new DynamicStringListProperty("listprop", Collections.emptyList());
		assertThat(listProperty.get(), contains("value1", "value2", "value3"));
	}

	@Test
	public void testBasicMapProps() {
		DynamicStringMapProperty mapProperty = new DynamicStringMapProperty("mapprop", Collections.emptyMap());
		assertThat(mapProperty.getMap(), allOf(hasEntry("key1", "value1"), hasEntry("key2", "value2")));
	}

	@Test
	public void testBasicLongProperty() {
		DynamicLongProperty longProp = DynamicPropertyFactory.getInstance().getLongProperty("longprop", 1000);
		assertThat(longProp.get(), equalTo(100L));
	}

}
