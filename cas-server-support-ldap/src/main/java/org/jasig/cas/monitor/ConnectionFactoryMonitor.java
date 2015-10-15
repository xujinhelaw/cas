/*
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.monitor;

import org.ldaptive.Connection;
import org.ldaptive.ConnectionFactory;
import org.ldaptive.LdapException;
import org.ldaptive.pool.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * Monitors an ldaptive {@link ConnectionFactory}.  While this class can be used with instances of
 * {@link org.ldaptive.pool.PooledConnectionFactory}, the {@link PooledConnectionFactoryMonitor} class is preferable.
 *
 * @author Marvin S. Addison
 * @since 4.0.0
 */
@Component("ldapConnectionFactoryMonitor")
public class ConnectionFactoryMonitor extends AbstractNamedMonitor<Status> {

    /** OK status. */
    private static final Status OK = new Status(StatusCode.OK);

    /** Error status. */
    private static final Status ERROR = new Status(StatusCode.ERROR);

    /** Source of connections to validate. */
    @Nullable
    @Autowired(required=false)
    @Qualifier("connectionFactoryMonitorConnectionFactory")
    private ConnectionFactory connectionFactory;

    /** Connection validator. */
    @Nullable
    @Autowired(required=false)
    @Qualifier("connectionFactoryMonitorValidator")
    private Validator<Connection> validator;


    /**
     * Instantiates a new Connection factory monitor.
     */
    public ConnectionFactoryMonitor() {}

    /**
     * Creates a new instance that monitors the given connection factory.
     *
     * @param  factory  Connection factory to monitor.
     * @param  validator  Validates connections from the factory.
     */
    public ConnectionFactoryMonitor(final ConnectionFactory factory, final Validator<Connection> validator) {
        this.connectionFactory = factory;
        this.validator = validator;
    }


    /**
     * Gets a connection from the underlying connection factory and attempts to validate it.
     *
     * @return  Status with code {@link StatusCode#OK} on success otherwise {@link StatusCode#ERROR}.
     */
    @Override
    public Status observe() {
        if (this.connectionFactory != null && this.validator != null) {
            try (final Connection conn = this.connectionFactory.getConnection()) {
                if (!conn.isOpen()) {
                    conn.open();
                }
                return this.validator.validate(conn) ? OK : ERROR;
            } catch (final LdapException e) {
                logger.warn("Validation failed with error.", e);
            }
            return ERROR;
        }
        return Status.UNKNOWN;
    }
}
