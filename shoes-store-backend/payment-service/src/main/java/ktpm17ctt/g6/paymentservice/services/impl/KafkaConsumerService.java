//package ktpm17ctt.g6.paymentservice.services.impl;
//
//import ktpm17ctt.g6.event.dto.NotificationEvent;
//import ktpm17ctt.g6.event.dto.PaymentUrlCreationReq;
//import ktpm17ctt.g6.paymentservice.services.PaymentService;
//import ktpm17ctt.g6.paymentservice.services.VNPayService;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.CountDownLatch;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class KafkaConsumerService {
//    private final CountDownLatch countDownLatch = new CountDownLatch(1);
//    private PaymentUrlCreationReq paymentUrlCreationReq;
//    private final VNPayService vnPayService;
//
//    public PaymentUrlCreationReq getPaymentUrlCreationReq() throws InterruptedException{
//        boolean success = countDownLatch.await(10, java.util.concurrent.TimeUnit.SECONDS);
//        if(!success){
//            log.error("Timeout while waiting for Kafka response.");
//            throw new RuntimeException("Payment service timeout.");
//        }
//        return paymentUrlCreationReq;
//    }
//
//    @KafkaListener(topics = "payment-request")
//    public void listenOrderSend(PaymentUrlCreationReq paymentUrlCreationReq) throws Exception {
//        log.info("Message received: {}", paymentUrlCreationReq);
//        this.paymentUrlCreationReq = paymentUrlCreationReq;
//        vnPayService.getPaymentUrl(paymentUrlCreationReq.getOrderId(), paymentUrlCreationReq.getAmount(),
//                paymentUrlCreationReq.getIpAddress(), "", "");
//        countDownLatch.countDown();
//    }
//}
