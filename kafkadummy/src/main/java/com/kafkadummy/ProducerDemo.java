package com.kafkadummy;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerDemo 
{
    public static void main( String[] args )
    {
        String bootstrapServers = "127.0.0.1:9092";

        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        // create the record
        ProducerRecord<String, String> record =
            new ProducerRecord<String,String>("first_topic_2", "Hello World!");

        // send data (asynchronous)
        producer.send(record);

        // flush data
        producer.flush();

        // close produser
        producer.close();

        SomeClass MyClass = new SomeClass();

    }
}

class SomeClass {

}
