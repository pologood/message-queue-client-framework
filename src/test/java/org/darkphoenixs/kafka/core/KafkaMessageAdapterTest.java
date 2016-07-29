package org.darkphoenixs.kafka.core;

import java.util.HashMap;
import java.util.List;

import kafka.message.MessageAndMetadata;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.darkphoenixs.kafka.codec.KafkaMessageDecoderImpl;
import org.darkphoenixs.kafka.listener.MessageConsumerListener;
import org.darkphoenixs.mq.consumer.Consumer;
import org.darkphoenixs.mq.exception.MQException;
import org.darkphoenixs.mq.message.MessageBeanImpl;
import org.junit.Assert;
import org.junit.Test;

public class KafkaMessageAdapterTest {

    @Test
    public void test() throws Exception {

        KafkaMessageAdapter<Integer, MessageBeanImpl> adapter = new KafkaMessageAdapter<Integer, MessageBeanImpl>();

        Assert.assertNull(adapter.getDecoder());
        adapter.setDecoder(new KafkaMessageDecoderImpl());

        Assert.assertNull(adapter.getDestination());
        adapter.setDestination(new KafkaDestination("QUEUE.TEST"));

        Assert.assertNull(adapter.getMessageListener());

        MessageConsumerListener<Integer, MessageBeanImpl> listener = new MessageConsumerListener<Integer, MessageBeanImpl>();
        listener.setConsumer(new Consumer<MessageBeanImpl>() {

            @Override
            public void receive(MessageBeanImpl message) throws MQException {
                System.out.println(message);
            }

            @Override
            public String getConsumerKey() throws MQException {
                return "";
            }
        });

        adapter.setMessageListener(listener);

        MessageBeanImpl messageBean = new MessageBeanImpl();

        long date = System.currentTimeMillis();
        messageBean.setMessageNo("MessageNo");
        messageBean.setMessageType("MessageType");
        messageBean.setMessageAckNo("MessageAckNo");
        messageBean.setMessageDate(date);
        messageBean.setMessageContent("MessageContent".getBytes("UTF-8"));

        try {
            adapter.messageAdapter(new MessageAndMetadata<Object, Object>("QUEUE.TEST", 0, null, 0, null, null));
        } catch (Exception e) {

            Assert.assertTrue(e instanceof NullPointerException);
        }

        try {
            adapter.messageAdapter(new ConsumerRecords<Object, Object>(new HashMap<TopicPartition, List<ConsumerRecord<Object, Object>>>()));
        } catch (Exception e) {

        }
    }
}
