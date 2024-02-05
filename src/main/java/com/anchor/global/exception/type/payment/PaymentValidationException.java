package com.anchor.global.exception.type.payment;

import com.anchor.global.exception.ServiceException;
import com.anchor.global.exception.error.AnchorErrorCode;
import com.anchor.global.exception.response.SimpleErrorDetail;

public class PaymentValidationException extends ServiceException {

  public PaymentValidationException(String message) {
    super(AnchorErrorCode.INVALID_PAYMENT, new SimpleErrorDetail(message));
  }

}
