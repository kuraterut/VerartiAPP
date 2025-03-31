package models

type Product struct {
	Id    int    `json:"id" db:"id"`
	Name  string `json:"name" db:"name" binding:"required"`
	Price int    `json:"price" db:"price" binding:"required"`
	Count int    `json:"count" db:"count"`
}

type ProductUpdate struct {
	Name  *string `json:"name"`
	Price *int    `json:"price"`
	Count *int    `json:"count"`
}
