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

package com.consol.citrus.http.message;

import com.consol.citrus.message.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.servlet.http.Cookie;

/**
 * @author Christoph Deppisch
 * @since 2.7
 */
public class HttpMessageUtilsTest {

    @Test
    public void testCopy() {
        HttpMessage from = new HttpMessage("fooMessage")
                            .header("X-Foo", "foo")
                            .addHeaderData("HeaderData");

        from.setName("FooMessage");

        HttpMessage to = new HttpMessage();

        HttpMessageUtils.copy(from, to);

        Assert.assertNotEquals(from.getId(), to.getId());
        Assert.assertEquals(to.getName(), "FooMessage");
        Assert.assertEquals(to.getPayload(String.class), "fooMessage");
        Assert.assertEquals(to.getHeaders().size(), 3L);
        Assert.assertNotNull(to.getHeader(MessageHeaders.ID));
        Assert.assertNotNull(to.getHeader(MessageHeaders.TIMESTAMP));
        Assert.assertEquals(to.getHeader("X-Foo"), "foo");
        Assert.assertEquals(to.getHeaderData().size(), 1L);
        Assert.assertEquals(to.getHeaderData().get(0), "HeaderData");
    }

    @Test
    public void testCopyPreventExistingOverwritePayload() {
        HttpMessage from = new HttpMessage("fooMessage")
                            .header("X-Foo", "foo")
                            .cookie(new Cookie("Foo", "fooCookie"))
                            .addHeaderData("HeaderData");

        from.setName("FooMessage");

        HttpMessage to = new HttpMessage("existingPayload")
                            .header("X-Existing", "existing")
                            .cookie(new Cookie("Existing", "existingCookie"))
                            .addHeaderData("ExistingHeaderData");

        to.setName("ExistingMessage");

        HttpMessageUtils.copy(from, to);

        Assert.assertNotEquals(from.getId(), to.getId());
        Assert.assertEquals(to.getName(), "FooMessage");
        Assert.assertEquals(to.getPayload(String.class), "fooMessage");
        Assert.assertEquals(to.getHeaders().size(), 6L);
        Assert.assertNotNull(to.getHeader(MessageHeaders.ID));
        Assert.assertNotNull(to.getHeader(MessageHeaders.TIMESTAMP));
        Assert.assertEquals(to.getHeader("X-Foo"), "foo");
        Assert.assertEquals(to.getHeader("X-Existing"), "existing");
        Assert.assertEquals(to.getHeader(HttpMessageHeaders.HTTP_COOKIE_PREFIX + "Foo"), "Foo=fooCookie");
        Assert.assertEquals(to.getHeader(HttpMessageHeaders.HTTP_COOKIE_PREFIX + "Existing"), "Existing=existingCookie");
        Assert.assertEquals(to.getHeaderData().size(), 2L);
        Assert.assertEquals(to.getHeaderData().get(0), "ExistingHeaderData");
        Assert.assertEquals(to.getHeaderData().get(1), "HeaderData");
    }

    @Test
    public void testConvertAndCopy() {
        Message from = new DefaultMessage("fooMessage")
                            .setHeader("X-Foo", "foo")
                            .addHeaderData("HeaderData");

        from.setName("FooMessage");

        HttpMessage to = new HttpMessage();

        HttpMessageUtils.copy(from, to);

        Assert.assertNotEquals(from.getId(), to.getId());
        Assert.assertEquals(to.getName(), "FooMessage");
        Assert.assertEquals(to.getPayload(String.class), "fooMessage");
        Assert.assertEquals(to.getHeader("X-Foo"), "foo");
        Assert.assertEquals(to.getHeaderData().size(), 1L);
        Assert.assertEquals(to.getHeaderData().get(0), "HeaderData");
    }
}