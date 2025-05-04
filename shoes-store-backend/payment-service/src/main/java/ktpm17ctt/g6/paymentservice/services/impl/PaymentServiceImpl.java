package ktpm17ctt.g6.paymentservice.services.impl;

import com.google.gson.JsonObject;
import ktpm17ctt.g6.paymentservice.configurations.VNPayConfig;
import ktpm17ctt.g6.paymentservice.dtos.responses.PaymentResponse;
import ktpm17ctt.g6.paymentservice.dtos.responses.RefundResponse;
import ktpm17ctt.g6.paymentservice.entities.Payment;
import ktpm17ctt.g6.paymentservice.entities.PaymentStatus;
import ktpm17ctt.g6.paymentservice.repositories.PaymentRepository;
import ktpm17ctt.g6.paymentservice.repositories.httpClients.OrderClient;
import ktpm17ctt.g6.paymentservice.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    PaymentRepository paymentRepository;
//    KafkaTemplate<String, String> kafkaTemplate; // KafkaTemplate để gửi sự kiện
//    private static final String PAYMENT_SUCCESS_TOPIC = "payment_success"; // Định nghĩa topic Kafka
    OrderClient orderClient;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PaymentResponse save(String orderId, String transactionId, String responseCode, String amount, String transDate) {
        Payment paymentEntity = Payment.builder()
                .orderId(orderId)
                .transactionId(transactionId)
                .status(responseCode.equals("00") ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
                .amount(Long.parseLong(amount) / 100)
                .transactionDate(transDate)
                .build();



        try {
            if(!responseCode.equalsIgnoreCase("00")){
                orderClient.handleUpdateOrderForPaymentFailed(orderId);
                log.info("Payment failed, updating order status to FAILED for orderId: {}", orderId);
            }
            paymentEntity = paymentRepository.save(paymentEntity);
            orderClient.updatePaymentStatusForOrder(orderId, true);
        }catch (Exception e){
            log.error("Error saving payment: {}", e.getMessage());
            paymentRepository.deleteById(paymentEntity.getId());
        }


        log.info("Payment saved: {}", paymentEntity);


        // Gửi sự kiện Kafka nếu thanh toán thành công
//        if (responseCode.equals("00")) {
//            PaymentResponse paymentResponse = PaymentResponse.builder()
//                    .orderId(paymentEntity.getOrderId())
//                    .status(String.valueOf(paymentEntity.getStatus()))
//                    .transactionId(paymentEntity.getTransactionId())
//                    .amount(paymentEntity.getAmount())
////                    .userEmail(userEmail)  // Thêm thông tin email người dùng
//                    .build();
//
//            // Gửi sự kiện Kafka với thông tin thanh toán thành công
////            kafkaTemplate.send(PAYMENT_SUCCESS_TOPIC, paymentResponse.getUserEmail());
//        }
        return PaymentResponse.builder()
                .orderId(paymentEntity.getOrderId())
                .status(String.valueOf(paymentEntity.getStatus()))
                .transactionId(paymentEntity.getTransactionId())
                .amount(paymentEntity.getAmount())
                .build();
    }

    @Override
    public PaymentResponse getPaymentByOrderId(String orderId) {
        Payment paymentEntity = paymentRepository.findByOrderId(orderId).orElseThrow(() ->
                new RuntimeException("Payment not found"));

        return PaymentResponse.builder()
                .orderId(paymentEntity.getOrderId())
                .status(String.valueOf(paymentEntity.getStatus()))
                .transactionId(paymentEntity.getTransactionId())
                .amount(paymentEntity.getAmount())
                .transactionDate(paymentEntity.getTransactionDate())
                .build();
    }

   @Override
   public RefundResponse refundPayment(
           String orderId,
           String transactionType,
           String amountRequest,
           String user,
           String transDate,
           String ipAddress,
           String transactionNo
   ) throws Exception{
       String vnp_RequestId = VNPayConfig.getRandomNumber(8);
       String vnp_Version = "2.1.0";
       String vnp_Command = "refund";
       String vnp_TmnCode = VNPayConfig.getVnpTmnCode();
       String vnp_TransactionType = transactionType;
       String vnp_TxnRef = orderId;
       long amount = Long.parseLong(amountRequest) * 100L;
       String vnp_Amount = String.valueOf(amount);
       String vnp_OrderInfo = "Hoan tien GD OrderId:" + vnp_TxnRef;
       String vnp_TransactionNo = transactionNo;
       String vnp_TransactionDate = transDate;
       String vnp_CreateBy = user;

       Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
       SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
       String vnp_CreateDate = formatter.format(cld.getTime());

       String vnp_IpAddr = ipAddress;
       JsonObject vnp_Params = new JsonObject ();
       vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
       vnp_Params.addProperty("vnp_Version", vnp_Version);
       vnp_Params.addProperty("vnp_Command", vnp_Command);
       vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
       vnp_Params.addProperty("vnp_TransactionType", vnp_TransactionType);
       vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
       vnp_Params.addProperty("vnp_Amount", vnp_Amount);
       vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);

       if(vnp_TransactionNo != null && !vnp_TransactionNo.isEmpty())
       {
           vnp_Params.addProperty("vnp_TransactionNo", transactionNo);
       }

       vnp_Params.addProperty("vnp_TransactionDate", vnp_TransactionDate);
       vnp_Params.addProperty("vnp_CreateBy", vnp_CreateBy);
       vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
       vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

       String hash_Data= String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode,
               vnp_TransactionType, vnp_TxnRef, vnp_Amount, vnp_TransactionNo, vnp_TransactionDate,
               vnp_CreateBy, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);

       String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.getSecretKey(), hash_Data.toString());

       vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

       URL url = new URL(VNPayConfig.vnp_ApiUrl);
       HttpURLConnection con = (HttpURLConnection)url.openConnection();
       con.setRequestMethod("POST");
       con.setRequestProperty("Content-Type", "application/json");
       con.setDoOutput(true);
       DataOutputStream wr = new DataOutputStream(con.getOutputStream());
       wr.writeBytes(vnp_Params.toString());
       wr.flush();
       wr.close();
       int responseCode = con.getResponseCode();
       log.info("nSending 'POST' request to URL : {}", url);
       log.info("Post Data : {}", vnp_Params);
       log.info("Response Code : {}", responseCode);
       BufferedReader in = new BufferedReader(
               new InputStreamReader(con.getInputStream()));
       String output;
       StringBuffer response = new StringBuffer();

       while ((output = in.readLine()) != null) {
           response.append(output);
       }

       log.info("Response : {}", response.toString());

       JSONObject vnResObj = new JSONObject(response.toString());
       String vnp_ResponseCode = vnResObj.getString("vnp_ResponseCode");
       String vnp_Message = vnResObj.getString("vnp_Message");

       return RefundResponse.builder()
               .vnp_ResponseCode(vnp_ResponseCode)
               .vnp_Message(vnp_Message)
               .amountRequest(String.valueOf(amount / 100))
               .orderId(orderId)
               .transactionId(transactionNo)
               .build();
   }


}
