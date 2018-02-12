package workqueue;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ReceiveMessage {

  private final static String QUEUE_NAME = "task_queue";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    boolean durable = true;
    channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    channel.basicQos(1);
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
      }
    };
    boolean autoAck = false;
    channel.basicConsume(QUEUE_NAME, autoAck, consumer);
  }
}