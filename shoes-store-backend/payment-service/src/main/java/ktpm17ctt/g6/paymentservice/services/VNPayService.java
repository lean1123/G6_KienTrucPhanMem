package ktpm17ctt.g6.paymentservice.services;

import java.io.IOException;

public interface VNPayService {
    String getPaymentUrl(String orderId, String total, String ipAddress, String bankCode, String language) throws Exception;

    String getPaymentResult(String orderId, String transDate, String ipAddress) throws IOException;
}
