package models

type Appointment struct {
	Id          int    `json:"id" db:"id"`
	Price       int    `json:"price" db:"price" binding:"required"`
	Name        string `json:"name" db:"name" binding:"required"`
	Description string `json:"description" db:"description" binding:"required"`
	Duration    string `json:"duration" db:"duration" binding:"required"`
}
