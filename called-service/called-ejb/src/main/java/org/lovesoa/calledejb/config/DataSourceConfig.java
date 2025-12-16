package org.lovesoa.calledejb.config;


import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;

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
