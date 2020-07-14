/*******************************************************************************
 * Copyright (c) 2018, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
package dev.appsody.starter.health;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import dev.appsody.starter.service.InventoryService;
import dev.appsody.starter.utils.JDBCConnection;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Readiness
@ApplicationScoped
public class SystemHealth implements HealthCheck {

    private final static String QUEUE_NAME = "health";
    String health = "health_check";

	/**
	 * Method is responsible for checking if the inventory database is ready
	 * @return true when database is ready, otherwise returns false
	 */
	public boolean isInventoryDbReady() {
        JDBCConnection jdbcConnection = new JDBCConnection();
        java.sql.Connection connection = jdbcConnection.getConnection();
		return connection != null;
    }

	/**
	 * Method is responsible for determining if RabbigMQ Service is ready
	 * @return true if RabbitMQ is able to send a message
	 * @throws IOException if there is an error
	 * @throws TimeoutException or if we run out of time
	 */
    public boolean isRabbitMQReady() throws IOException, TimeoutException {
		return sendMessage();
	}

    /**
     * Method is responsible for sending a message
     * @return true if is able to send a message
     */
    public boolean sendMessage() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            Config config = ConfigProvider.getConfig();
            String rabbit_host = config.getValue("rabbit", String.class);
            String sentMsg = null;
            factory.setHost(rabbit_host);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            channel.basicPublish("", QUEUE_NAME, null, health.getBytes());

            boolean autoAck = true;
            GetResponse response = channel.basicGet(QUEUE_NAME, autoAck);
            if (response == null) {
                // There are no messages to retrieve
            } else {
                byte[] body = response.getBody();
                sentMsg = new String(body);
            }

            if (sentMsg.equals(health)) {
                return true;
            }

            channel.close();
            connection.close();
            return false;
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public HealthCheckResponse call() {
        if (!isInventoryDbReady()) {
            return HealthCheckResponse.named(InventoryService.class.getSimpleName())
                    .withData("Inventory Database", "DOWN").down()
                    .build();
        }
        return HealthCheckResponse.named(InventoryService.class.getSimpleName()).withData("Inventory Service", "UP").up().build();
    }
}
