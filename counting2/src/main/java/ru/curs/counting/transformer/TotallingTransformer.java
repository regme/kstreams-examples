package ru.curs.counting.transformer;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueStore;
import ru.curs.counting.model.Bet;
import ru.curs.counting.util.StatefulTransformer;

import java.util.Optional;

public class TotallingTransformer implements
        StatefulTransformer<String, Long,
                String, Bet,
                String, Long> {

    public static final String STORE_NAME = "totalling-store";

    @Override
    public String storeName() {
        return STORE_NAME;
    }

    @Override
    public Serde<String> storeKeySerde() {
        return Serdes.String();
    }

    @Override
    public Serde<Long> storeValueSerde() {
        return Serdes.Long();
    }

    @Override
    public KeyValue<String, Long> transform(String key, Bet value,
                                            KeyValueStore<String, Long> stateStore) {
        long current = Optional.ofNullable(stateStore.get(key)).orElse(0L);
        current += value.getAmount();
        stateStore.put(key, current);
        return KeyValue.pair(key, current);
    }
}
