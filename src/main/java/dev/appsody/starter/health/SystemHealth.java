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

import dev.appsody.starter.service.InventoryService;
import dev.appsody.starter.utils.JDBCConnection;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;

/**
 * Class is responsible for determining the system health by checking sources are ready
 * such as database.
 */
@Readiness
@ApplicationScoped
public class SystemHealth implements HealthCheck {

	/**
	 * Method is responsible for checking if the inventory database is ready
	 * @return true when database is ready, otherwise returns false
	 */
	public boolean isInventoryDbReady() {
        JDBCConnection jdbcConnection = new JDBCConnection();
        java.sql.Connection connection = jdbcConnection.getConnection();
		return connection != null;
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
