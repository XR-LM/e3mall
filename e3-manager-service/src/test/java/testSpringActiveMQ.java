import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class testSpringActiveMQ {
	
	
	@Test
	public void testQueueProducer() throws Exception{
		//加载spring配置文件
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//获得JmsTemplate模板对象
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		//获得队列目的地对象
		Destination destination = (Destination)applicationContext.getBean("queueDestination");
		//发送消息
		jmsTemplate.send(destination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO 自动生成的方法存根
				TextMessage message = session.createTextMessage("spring activemq queue");
				return message;
			}
		});
		
		
		
		
	}
}
