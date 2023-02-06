package com.kafkadummy;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerDemoWithCallback
{
    public static void main( String[] args )
    {
        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);
        String bootstrapServers = "127.0.0.1:9092";

        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (int i=0; i < 10; i++) {
        // 2 sec pause to use different partitions
        try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }

        // create records
        ProducerRecord<String, String> record =
        new ProducerRecord<String,String>("first_topic_2", "Hello World - " + Integer.toString(i));
            // send data (asynchronous)
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        // executes every time a record is successfully sent 
                        logger.info("\n\n => Receive new metadata. \n" +
                        "Topic: " + recordMetadata.topic() + "\n" +
                        "Partition " + recordMetadata.partition() + "\n" +
                        "Offset: " + recordMetadata.offset() + "\n" + 
                        "Timestamp: " + recordMetadata.timestamp());
                    } else {
                        logger.error("error while producing", e);
                    }
                }
            });
        }

        // flush data
        producer.flush();

        // close produser
        producer.close();
    }
}
