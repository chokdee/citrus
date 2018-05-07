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

package com.consol.citrus.generate.provider.http;

import com.consol.citrus.generate.provider.MessageActionProvider;
import com.consol.citrus.http.message.HttpMessage;
import com.consol.citrus.message.MessageHeaders;
import com.consol.citrus.model.testcase.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Christoph Deppisch
 * @since 2.7.4
 */
public class ReceiveHttpRequestActionProvider implements MessageActionProvider<ReceiveRequestModel, HttpMessage> {

    @Override
    public ReceiveRequestModel getAction(String endpoint, HttpMessage message) {
        ReceiveRequestModel request = new ReceiveRequestModel();

        request.setServer(endpoint);

        ServerRequestType requestType = new ServerRequestType();
        ServerRequestType.Body body = new ServerRequestType.Body();
        body.setData(message.getPayload(String.class));
        requestType.setBody(body);

        requestType.setPath(Optional.ofNullable(message.getPath()).map(Object::toString).orElse(""));

        ServerRequestType.Headers requestHeaders = new ServerRequestType.Headers();
        requestHeaders.setContentType("application/json");

        message.getHeaders().entrySet().stream()
                .filter(entry -> !entry.getKey().startsWith(MessageHeaders.PREFIX))
                .forEach(entry -> {
                    ServerRequestType.Headers.Header header = new ServerRequestType.Headers.Header();
                    header.setName(entry.getKey());
                    header.setValue(Optional.ofNullable(entry.getValue()).map(Object::toString).orElse(""));
                    requestHeaders.getHeaders().add(header);
                });

        requestType.setHeaders(requestHeaders);

        if (StringUtils.hasText(message.getQueryParams())) {
            Stream.of(message.getQueryParams()
                    .split(","))
                    .map(nameValuePair -> nameValuePair.split("="))
                    .forEach(param -> {
                        ParamType paramType = new ParamType();
                        paramType.setName(param[0]);
                        paramType.setValue(param[1]);
                        requestType.getParams().add(paramType);
                    });
        }

        String method = Optional.ofNullable(message.getRequestMethod()).map(Object::toString).orElse(RequestMethod.POST.name());

        if (RequestMethod.GET.name().equals(method)) {
            request.setGET(requestType);
        } else if (RequestMethod.POST.name().equals(method)) {
            request.setPOST(requestType);
        } else if (RequestMethod.PUT.name().equals(method)) {
            request.setPUT(requestType);
        } else if (RequestMethod.DELETE.name().equals(method)) {
            request.setDELETE(requestType);
        } else if (RequestMethod.HEAD.name().equals(method)) {
            request.setHEAD(requestType);
        } else if (RequestMethod.OPTIONS.name().equals(method)) {
            request.setPOST(requestType);
        } else if (RequestMethod.PATCH.name().equals(method)) {
            request.setPATCH(requestType);
        } else if (RequestMethod.TRACE.name().equals(method)) {
            request.setTRACE(requestType);
        }

        return request;
    }
}
