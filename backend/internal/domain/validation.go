package domain

import (
	"errors"
	"time"
)

var (
	ValidationErrDateFormat = errors.New("invalid date format")
)

func ValidatorDateAndTimeFormat(format, date string) error {
	_, err := time.Parse(format, date)
	if err != nil {
		return ValidationErrDateFormat
	}
	return nil
}
