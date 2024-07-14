package com.sauron.radium.heistron;

import com.fasterxml.jackson.databind.ObjectMapper;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class CrewPipeline implements Pipeline {

    private ObjectMapper objectMapper = new ObjectMapper();

    private int cnt = 0;

    @Override
    public void process( ResultItems resultItems, Task task ) {

    }
}