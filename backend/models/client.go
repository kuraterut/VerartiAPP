package models

import "time"

type Client struct {
	Id         int       `json:"-" db:"id"`
	Name       string    `json:"name" binding:"required"`
	Surname    string    `json:"username" binding:"required"`
	Patronymic string    `json:"patronymic" binding:"required"`
	Email      string    `json:"email" binding:"required"`
	Phone      string    `json:"phone" binding:"required"`
	Bio        string    `json:"bio"`
	Birthday   time.Time `json:"birthday"`
}
