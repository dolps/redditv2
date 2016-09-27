package com.dolplads.helpers;

import lombok.extern.java.Log;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.logging.Level;

/**
 * Created by dolplads on 25/09/16.
 */
@RunWith(Arquillian.class)
public abstract class ArquillianTest {
    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive war = ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "com.dolplads")
                .addAsResource("META-INF/persistence.xml");

        System.out.println(war.toString(true));
        return war;
    }
}
