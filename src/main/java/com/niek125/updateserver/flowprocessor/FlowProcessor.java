package com.niek125.updateserver.flowprocessor;

import com.niek125.updateserver.converters.IConverter;
import com.niek125.updateserver.dispatchers.Dispatcher;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@AllArgsConstructor
public class FlowProcessor<I, O> {
    private final Logger logger = LoggerFactory.getLogger(FlowProcessor.class);
    private final Dispatcher<O> dispatcher;
    private final List<IConverter<I, O>> converters;

    public void process(I input, Class eventClass){
        try {
            //move event parser here when front end switch to events
            logger.info("converting");
            final IConverter<I, O> converter = converters.stream()
                    .filter(x -> x.getConvertType() == eventClass)
                    .findFirst().orElseThrow(ClassNotFoundException::new);
            final O output = converter.convert(input);
            logger.info("dispatching");
            dispatcher.dispatch(output);
        } catch (ClassNotFoundException e) {
            logger.debug("event not found");
        }
        logger.info("successfully processed message");
    }
}
