package com.kafkadummy;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerDemoWithKeys
{
    public static void main( String[] args ) throws ExecutionException, InterruptedException
    {
        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithKeys.class);
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
        // try {
        //     Thread.sleep(2000);
        //   } catch (InterruptedException e) {
        //     Thread.currentThread().interrupt();
        //   }

        String topic = "first_topic_2";
        String value = "Hello World " + Integer.toString(i);
        String key = "id_" + Integer.toString(i);

        // key 1 =  par 1
        // key 2 =  par 0
        // key 3 =  par 2
        // key 4 =  par 0
        // key 5 =  par 2
        // key 6 =  par 2
        // key 7 =  par 0
        // key 8 =  par 2
        // key 9 =  par 1


        // create the producer
        ProducerRecord<String, String> record =
        new ProducerRecord<String,String>(topic, key, value);

        logger.info("Key: " + key); // log the key

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
            }).get(); // block the .send() to make it syncronous (don`t do this in production!)
        }

        // flush data
        producer.flush();

        // close produser
        producer.close();
    }
}
