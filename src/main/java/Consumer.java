import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Timestamp;

public class Consumer {
    public static final String KAFKA_SERVER = "localhost:9092";
    private static final Logger logger = LogManager.getLogger(Consumer.class);
    private KafkaConsumer<String, String> consumer;

    public Consumer(Properties properties){
        this.consumer = new KafkaConsumer<String, String>(properties);
    }

    public void consume(String topicName){
        try {

            // subscribe consumer to our topic(s)
            consumer.subscribe(Arrays.asList(topicName));

            // poll for new data
            while (true) {
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    logger.info("Value:  {} topic: {} timestamp: {} in-time: {}",
                            record.value(), record.topic(), record.timestamp(), System.currentTimeMillis());
                }
            }

        } catch (Exception e) {
            System.out.println("Unexpected exception" + e.getMessage());
        } finally {
            consumer.close(); // this will also commit the offsets if need be.
            System.out.println("The consumer is now gracefully closed.");
        }
    }
}
