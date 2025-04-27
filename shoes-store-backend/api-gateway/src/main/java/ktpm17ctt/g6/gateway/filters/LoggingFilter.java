package ktpm17ctt.g6.gateway.filters;

import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final String TRACE_ID = "traceId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = UUID.randomUUID().toString();

        // Set traceId vào header request
        exchange.getRequest().mutate()
                .header(TRACE_ID, traceId)
                .build();

        // Set traceId vào MDC để log
        MDC.put(TRACE_ID, traceId);

        // Log request
        System.out.println("[REQUEST] TraceId=" + traceId +
                ", Method=" + exchange.getRequest().getMethod() +
                ", Path=" + exchange.getRequest().getPath());

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    // Log response
                    System.out.println("[RESPONSE] TraceId=" + traceId +
                            ", Status=" + exchange.getResponse().getStatusCode());
                    MDC.clear();
                })
        );
    }

    @Override
    public int getOrder() {
        return -1; // Run early
    }
}

