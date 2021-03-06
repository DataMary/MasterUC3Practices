package com.cenebrera.uc3.tech.lesso10.acceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.ApplVerID;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by alexvarela on 19/12/16.
 */
public class AcceptorRunner
{
    /** a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory */
    private final static Logger LOGGER = LoggerFactory.getLogger(AcceptorRunner.class);

    public static void main(String[] args)
    {
        //Read the info from a xml and populate the class
        URL url = AcceptorRunner.class.getClassLoader().getResource("acceptor.cfg");
        // FooApplication is your class that implements the Application interface
        Application application = new QuickFixAcceptor();

        SessionSettings settings = null;
        try
        {
            settings = new SessionSettings(new FileInputStream(new File(url.toURI())));
        }
        catch (ConfigError configError)
        {
            LOGGER.error("Config Error ", configError);
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("File [{}] nto found", url,e);
        }
        catch (URISyntaxException e)
        {
            LOGGER.error("Uri bad format  url [{}]", url, e);
        }

        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        DefaultDataDictionaryProvider  dac = new DefaultDataDictionaryProvider();
        dac.getApplicationDataDictionary(new ApplVerID("9"));
        MessageFactory messageFactory = new DefaultMessageFactory();

        Acceptor acceptor = null;
        try
        {
            acceptor = new SocketAcceptor
                    (application, storeFactory, settings, logFactory, messageFactory);
            acceptor.start();
        }
        catch (ConfigError configError)
        {
            configError.printStackTrace();
        }
        LOGGER.debug("Start acceptor");

    }
}
