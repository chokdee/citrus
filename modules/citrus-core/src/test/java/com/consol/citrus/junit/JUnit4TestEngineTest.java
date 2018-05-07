/*
 * Copyright 2006-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.junit;

import com.consol.citrus.TestClass;
import com.consol.citrus.junit.scan.SampleJUnit4Test;
import com.consol.citrus.main.TestRunConfiguration;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;

/**
 * @author Christoph Deppisch
 * @since 2.7
 */
public class JUnit4TestEngineTest {

    @Test
    public void testRunPackage() {
        TestRunConfiguration configuration = new TestRunConfiguration();
        configuration.setIncludes(new String[] { ".*Test" });
        configuration.setPackages(Collections.singletonList(SampleJUnit4Test.class.getPackage().getName()));

        runTestEngine(configuration, 0L, 1L);
    }

    @Test
    public void testRunClass() {
        TestRunConfiguration configuration = new TestRunConfiguration();
        configuration.setTestClasses(Collections.singletonList(new TestClass(SampleJUnit4Test.class.getName())));
        
        runTestEngine(configuration, 0L, 1L);
    }

    @Test
    public void testRunNoMatch() {
        TestRunConfiguration configuration = new TestRunConfiguration();
        configuration.setIncludes(new String[] { ".*Foo" });
        configuration.setPackages(Collections.singletonList(SampleJUnit4Test.class.getPackage().getName()));

        runTestEngine(configuration, 0L, 0L);
    }

    private void runTestEngine(TestRunConfiguration configuration, long failure, long passed) {
        JUnit4TestEngine engine = new JUnit4TestEngine(configuration);
        engine.addRunListener(new RunListener() {
            @Override
            public void testRunFinished(Result result) throws Exception {
                Assert.assertEquals(result.getFailureCount(), failure);
                Assert.assertEquals(result.getRunCount(), passed);
            }
        });
        engine.run();
    }
}