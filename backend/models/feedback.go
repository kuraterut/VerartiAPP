package models

import "time"

type Feedback struct {
	Id      int       `json:"-" db:"id"`
	Client  Client    `json:"client"`
	User    Users     `json:"user"`
	Message string    `json:"message"`
	Date    time.Time `json:"date"`
}
