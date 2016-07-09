package com.dropwizard.application;

import com.dropwizard.configuration.DropWizardConfiguration;
import com.dropwizard.core.Person;
import com.dropwizard.jdbi.UserDAO;
import com.dropwizard.resource.DropWizardRestApi;
import com.dropwizard.resource.DropWizardRestApi1;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

/**
 * Created by SauravKumar on 29-05-2016.
 */
public class DropWizardApplication extends Application<DropWizardConfiguration>
{
    public static void main(String ...args)
    {
        try {
            new DropWizardApplication().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(DropWizardConfiguration dropWizardConfiguration, Environment environment) throws Exception
    {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, dropWizardConfiguration.getDataSourceFactory(), "postgresql");
        final UserDAO dao = jdbi.onDemand(UserDAO.class);
        //final UserDAO dao1 = new UserDAO(hibernate.getSessionFactory());
        environment.jersey().register(new DropWizardRestApi(dropWizardConfiguration, dao));
        environment.jersey().register(new DropWizardRestApi1());
    }
}
