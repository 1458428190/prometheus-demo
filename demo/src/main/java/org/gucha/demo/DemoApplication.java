package org.gucha.demo;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws IOException {
        String url = "10.224.192.113:9091";
        CollectorRegistry registry = new CollectorRegistry();
        Gauge guage = Gauge.build("my_custom_metric", "This is my custom metric.").labelNames("app", "date").create();
        String date = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(new Date());
        guage.labels("my-pushgateway-test-0", date).set(25);
        guage.labels("my-pushgateway-test-1", date).dec();
        guage.labels("my-pushgateway-test-2", date).dec(2);
        guage.labels("my-pushgateway-test-3", date).inc();
        guage.labels("my-pushgateway-test-4", date).inc(5);
        guage.register(registry);
        PushGateway pg = new PushGateway(url);
        Map<String, String> groupingKey = new HashMap<>();
        groupingKey.put("instance", "my_instance");
        pg.pushAdd(registry, "my_job", groupingKey);
    }

}
