package domain

import (
	"errors"
	"time"
)

var (
	ErrInvalidDateFormat      = errors.New("invalid date format, expected YYYY-MM-DD")
	ErrInvalidTimeFormat      = errors.New("invalid time format, expected HH:MM")
	ErrInvalidPaymentMethod   = errors.New("invalid payment method")
	ErrInvalidTransactionType = errors.New("invalid transaction type")
)

func ValidateDateOnly(date string) error {
	t, err := time.Parse("2006-01-02", date)
	if err != nil {
		return ErrInvalidDateFormat
	}

	if t.Format("2006-01-02") != date {
		return ErrInvalidDateFormat
	}

	return nil
}

func ValidateTimeOnly(timeStr string) error {
	t, err := time.Parse("15:04", timeStr)
	if err != nil {
		return ErrInvalidTimeFormat
	}

	if t.Format("15:04") != timeStr {
		return ErrInvalidTimeFormat
	}

	return nil
}

func ValidatePaymentMethod(paymentMethod string) error {
	for _, p := range PaymentMethods {
		if p == paymentMethod {
			return nil
		}
	}

	return ErrInvalidPaymentMethod
}

func ValidateTransactionType(transactionType string) error {
	for _, t := range TransactionTypes {
		if t == transactionType {
			return nil
		}
	}

	return ErrInvalidTransactionType
}
