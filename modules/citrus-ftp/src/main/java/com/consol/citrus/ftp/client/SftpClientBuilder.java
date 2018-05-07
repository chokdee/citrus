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

package com.consol.citrus.ftp.client;

import com.consol.citrus.endpoint.AbstractEndpointBuilder;
import com.consol.citrus.message.ErrorHandlingStrategy;
import com.consol.citrus.message.MessageCorrelator;

/**
 * @author Christoph Deppisch
 * @since 2.7.5
 */
public class SftpClientBuilder extends AbstractEndpointBuilder<SftpClient> {

    /** Endpoint target */
    private SftpClient endpoint = new SftpClient();

    @Override
    protected SftpClient getEndpoint() {
        return endpoint;
    }

    /**
     * Sets the host property.
     * @param host
     * @return
     */
    public SftpClientBuilder host(String host) {
        endpoint.getEndpointConfiguration().setHost(host);
        return this;
    }

    /**
     * Sets the port property.
     * @param port
     * @return
     */
    public SftpClientBuilder port(int port) {
        endpoint.getEndpointConfiguration().setPort(port);
        return this;
    }

    /**
     * Sets the auto read files property.
     * @param autoReadFiles
     * @return
     */
    public SftpClientBuilder autoReadFiles(boolean autoReadFiles) {
        endpoint.getEndpointConfiguration().setAutoReadFiles(autoReadFiles);
        return this;
    }

    /**
     * Sets the client username.
     * @param username
     * @return
     */
    public SftpClientBuilder username(String username) {
        endpoint.getEndpointConfiguration().setUser(username);
        return this;
    }

    /**
     * Sets the client password.
     * @param password
     * @return
     */
    public SftpClientBuilder password(String password) {
        endpoint.getEndpointConfiguration().setPassword(password);
        return this;
    }

    /**
     * Sets the strictHostChecking property.
     * @param strictHostChecking
     * @return
     */
    public SftpClientBuilder strictHostChecking(boolean strictHostChecking) {
        endpoint.getEndpointConfiguration().setStrictHostChecking(strictHostChecking);
        return this;
    }

    /**
     * Sets the message correlator.
     * @param correlator
     * @return
     */
    public SftpClientBuilder correlator(MessageCorrelator correlator) {
        endpoint.getEndpointConfiguration().setCorrelator(correlator);
        return this;
    }

    /**
     * Sets the error handling strategy.
     * @param errorStrategy
     * @return
     */
    public SftpClientBuilder errorHandlingStrategy(ErrorHandlingStrategy errorStrategy) {
        endpoint.getEndpointConfiguration().setErrorHandlingStrategy(errorStrategy);
        return this;
    }

    /**
     * Sets the polling interval.
     * @param pollingInterval
     * @return
     */
    public SftpClientBuilder pollingInterval(int pollingInterval) {
        endpoint.getEndpointConfiguration().setPollingInterval(pollingInterval);
        return this;
    }

    /**
     * Sets the default timeout.
     * @param timeout
     * @return
     */
    public SftpClientBuilder timeout(long timeout) {
        endpoint.getEndpointConfiguration().setTimeout(timeout);
        return this;
    }
}
