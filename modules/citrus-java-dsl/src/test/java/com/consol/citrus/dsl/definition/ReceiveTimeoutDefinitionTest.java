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
import com.consol.citrus.actions.ReceiveTimeoutAction;
import com.consol.citrus.container.SequenceAfterTest;
import com.consol.citrus.container.SequenceBeforeTest;
import com.consol.citrus.endpoint.Endpoint;
import com.consol.citrus.report.TestActionListeners;
import com.consol.citrus.testng.AbstractTestNGUnitTest;
import org.easymock.EasyMock;
import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

import static org.easymock.EasyMock.*;

public class ReceiveTimeoutDefinitionTest extends AbstractTestNGUnitTest {
    
    private Endpoint messageEndpoint = EasyMock.createMock(Endpoint.class);
    
    private ApplicationContext applicationContextMock = EasyMock.createMock(ApplicationContext.class);

    @Test
    public void testReceiveTimeoutBuilder() {
        MockDesigner builder = new MockDesigner(applicationContext) {
            @Override
            public void configure() {
                expectTimeout(messageEndpoint)
                    .timeout(5000)
                    .selector("TestMessageSelectorString");
            }
        };

        builder.execute();

        TestCase test = builder.build();
        Assert.assertEquals(test.getActions().size(), 1);
        Assert.assertEquals(test.getActions().get(0).getClass(), ReceiveTimeoutAction.class);
         
        ReceiveTimeoutAction action = (ReceiveTimeoutAction)test.getActions().get(0);
        Assert.assertEquals(action.getName(), "receive-timeout");
        Assert.assertEquals(action.getEndpoint(), messageEndpoint);
        Assert.assertEquals(action.getMessageSelector(),"TestMessageSelectorString"); 
        Assert.assertEquals(action.getTimeout(), 5000);
    }
    
    @Test
    public void testReceiveTimeoutBuilderWithEndpointName() {
        reset(applicationContextMock);
        expect(applicationContextMock.getBean(TestActionListeners.class)).andReturn(new TestActionListeners()).once();
        expect(applicationContextMock.getBeansOfType(SequenceBeforeTest.class)).andReturn(new HashMap<String, SequenceBeforeTest>()).once();
        expect(applicationContextMock.getBeansOfType(SequenceAfterTest.class)).andReturn(new HashMap<String, SequenceAfterTest>()).once();
        replay(applicationContextMock);

        MockDesigner builder = new MockDesigner(applicationContextMock) {
            @Override
            public void configure() {
                expectTimeout("fooMessageEndpoint")
                    .timeout(500);
            }
        };

        builder.execute();

        TestCase test = builder.build();
        Assert.assertEquals(test.getActions().size(), 1);
        Assert.assertEquals(test.getActions().get(0).getClass(), ReceiveTimeoutAction.class);
         
        ReceiveTimeoutAction action = (ReceiveTimeoutAction)test.getActions().get(0);
        Assert.assertEquals(action.getName(), "receive-timeout");
        Assert.assertEquals(action.getEndpointUri(), "fooMessageEndpoint");
        Assert.assertEquals(action.getTimeout(), 500);
        
        verify(applicationContextMock);
    }
}
