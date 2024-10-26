package models

type Resource struct {
	Id          int    `json:"-" db:"id"`
	Name        string `json:"name"`
	Description string `json:"description"`
	//Quantity    int    `json:"quantity"`
}
