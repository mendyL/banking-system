package com.bank.payment.infrastructure.config;

import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.ibm.msg.client.jakarta.wmq.WMQConstants;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

@Configuration
@Slf4j
@Profile("!test")
public class JmsConfig {

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer factoryConfigurer) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        //commit manuel
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factoryConfigurer.configure(factory, connectionFactory);

        factory.setErrorHandler(t -> {
            log.error("JMS listener error handler caught exception", t);
            // Ne pas relancer l'exception pour éviter les comportements imprévus
        });

        return factory;
    }

    @Bean
    public MQConnectionFactory mqConnectionFactory(
            @Value("${ibm.mq.queueManager}") String queueManager,
            @Value("${ibm.mq.channel}") String channel,
            @Value("${ibm.mq.connName}") String connName,
            @Value("${ibm.mq.user}") String user,
            @Value("${ibm.mq.password}") String password) throws JMSException {

        MQConnectionFactory mqConnectionFactory = new MQConnectionFactory();
        mqConnectionFactory.setQueueManager(queueManager);
        mqConnectionFactory.setChannel(channel);
        mqConnectionFactory.setConnectionNameList(connName);
        mqConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        mqConnectionFactory.setStringProperty(WMQConstants.USERID, user);
        mqConnectionFactory.setStringProperty(WMQConstants.PASSWORD, password);

        return mqConnectionFactory;

}

}
