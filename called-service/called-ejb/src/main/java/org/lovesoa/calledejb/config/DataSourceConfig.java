package org.lovesoa.calledejb.config;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
@DataSourceDefinition(
        name = "java:global/jdbc/calledDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "called_user",
        password = "called_pass",
        url = "jdbc:postgresql://postgres:5432/called_db"
)
public class DataSourceConfig {
}
