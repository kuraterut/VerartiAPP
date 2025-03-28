package domain

import (
	"errors"
	"time"
)

var (
	ErrInvalidDateFormat = errors.New("invalid date format, expected YYYY-MM-DD")
	ErrInvalidTimeFormat = errors.New("invalid time format, expected HH:MM")
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
