package kz.abylkhaiyrov.unirateplatformeurekaserver.Listiner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EurekaEventListener {

    @EventListener
    public void handleEurekaInstanceRegistered(EurekaInstanceRegisteredEvent event) {
        log.info("Instance registered: " + event.getInstanceInfo().getAppName());
    }

    @EventListener
    public void handleEurekaInstanceCanceled(EurekaInstanceCanceledEvent event) {
        log.error("Instance canceled: " + event.getAppName() + " with ID " + event.getServerId());
    }

    @EventListener
    public void handleEurekaInstanceRenewed(EurekaInstanceRenewedEvent event) {
        log.info("Instance renewed: " + event.getInstanceInfo().getAppName());
    }

    @EventListener
    public void handleHeartbeatEvent(HeartbeatEvent event) {
        log.info("Eureka heartbeat: " + event.getValue());
    }
}
