package models

import "time"

type Feedback struct {
	Id      int       `json:"-" db:"id"`
	Client  Client    `json:"client" binding:"required"`
	User    Users     `json:"user" binding:"required"`
	Message string    `json:"message" binding:"required"`
	Date    time.Time `json:"date" binding:"required"`
}
