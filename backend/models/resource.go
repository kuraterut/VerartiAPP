package models

type Resource struct {
	Id          int    `json:"-" db:"id"`
	Name        string `json:"name" binding:"required"`
	Description string `json:"description" binding:"required"`
	//Quantity    int    `json:"quantity"`
}
