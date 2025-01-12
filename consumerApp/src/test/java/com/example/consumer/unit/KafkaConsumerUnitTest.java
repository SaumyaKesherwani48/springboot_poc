package com.example.consumer.unit;

import com.example.consumer.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerUnitTest {

    @InjectMocks
    private KafkaConsumer kafkaConsumer;


    @Test
    public void testKafkaConsumer() throws InterruptedException{
        String msg = "CarModel created successfully!!";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = System.out;
        System.setOut(new PrintStream(outputStream));

        kafkaConsumer.updateCarModel(msg);

        assertEquals(msg,outputStream.toString().trim());

        System.setOut(printStream);
    }
}
