/*
 * Copyright 2006-2012 the original author or authors.
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

package com.consol.citrus.dsl.definition;

import com.consol.citrus.TestCase;
import com.consol.citrus.actions.*;
import com.consol.citrus.container.Sequence;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.AbstractTestNGUnitTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SequenceDefinitionTest extends AbstractTestNGUnitTest {
    @Test
    public void testSequenceBuilder() {
        MockDesigner builder = new MockDesigner(applicationContext) {
            @Override
            public void configure() {
                sequential(echo("${var}"), sleep(5000L));
            }
        };

        builder.execute();

        TestCase test = builder.build();
        assertEquals(test.getActions().size(), 1);
        assertEquals(test.getActions().get(0).getClass(), Sequence.class);
        assertEquals(test.getActions().get(0).getName(), "sequential");
        
        Sequence container = (Sequence)test.getActions().get(0);
        assertEquals(container.getActions().size(), 2);
        assertEquals(container.getActions().get(0).getClass(), EchoAction.class);
    }

    @Test
    public void testSequenceBuilderWithAnonymousAction() {
        MockDesigner builder = new MockDesigner(applicationContext) {
            @Override
            public void configure() {
                sequential(
                        echo("${var}"),
                        new AbstractTestAction() {
                            @Override
                            public void doExecute(TestContext context) {
                                context.setVariable("anonymous", "anonymous");
                            }
                        },
                        sleep(5000L),
                        new AbstractTestAction() {
                            @Override
                            public void doExecute(TestContext context) {
                                context.getVariable("anonymous");
                            }
                        });
            }
        };

        builder.execute();

        TestCase test = builder.build();
        assertEquals(test.getActions().size(), 1);
        assertEquals(test.getActions().get(0).getClass(), Sequence.class);
        assertEquals(test.getActions().get(0).getName(), "sequential");

        Sequence container = (Sequence)test.getActions().get(0);
        assertEquals(container.getActions().size(), 4);
        assertEquals(container.getActions().get(0).getClass(), EchoAction.class);
        assertTrue(container.getActions().get(1).getClass().isAnonymousClass());
        assertEquals(container.getActions().get(2).getClass(), SleepAction.class);
        assertTrue(container.getActions().get(3).getClass().isAnonymousClass());
    }
}
