package models

import "time"

type Schedule struct {
	Id          int         `json:"-" db:"id"`
	Client      Client      `json:"client"`
	Master      Users       `json:"master"`
	Appointment Appointment `json:"appointment"`
	Status      string      `json:"status"`
	Start       time.Time   `json:"start"`
	End         time.Time   `json:"end"`
}
