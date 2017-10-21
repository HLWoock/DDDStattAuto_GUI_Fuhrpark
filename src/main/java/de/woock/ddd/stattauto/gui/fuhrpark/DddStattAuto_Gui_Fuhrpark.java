package de.woock.ddd.stattauto.gui.fuhrpark;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.RestTemplate;

import de.woock.ddd.stattauto.gui.fuhrpark.service.RibbonConfiguration;
import de.woock.ddd.stattauto.gui.fuhrpark.views.FuhrparkView;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableCircuitBreaker
@EnableJms
@RibbonClient(name="DDD_StattAuto", configuration=RibbonConfiguration.class)
@SpringBootApplication
public class DddStattAuto_Gui_Fuhrpark extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Autowired FuhrparkView stationenView;

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
		
	
   @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
    		                                        DefaultJmsListenerContainerFactoryConfigurer configurer) throws JMSException {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        configurer.configure(factory, connectionFactory);
        
        return factory;
    }
   
    @Bean
    public ConnectionFactory connectionFactory() {
    	return new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
    }

    @Bean
    public JmsTemplate jmsDefektMeldung(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        return template;
    }
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(new Group());
		primaryStage.setTitle("StattAuto Fuhrpark");
		primaryStage.setWidth(1480);
		primaryStage.setHeight(910);

        ((Group) scene.getRoot()).getChildren().addAll(stationenView.initPane());
 
        primaryStage.setScene(scene);
        primaryStage.show();
		
    }
	
	@Override
	public void init() throws Exception {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(DddStattAuto_Gui_Fuhrpark.class).headless(false)
                                                                                                              .web(false)
                                                                                                              .run("");
		stationenView = context.getBean(FuhrparkView.class);
	}
	
}
